package ui.editor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.utilies.DocumentsManager
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.components.BottomActionsRow
import ui.editor.autocompleteCode.AutocompleteDropdownView
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorViewTab(
    modifier: Modifier,
    state: EditorState
){

    // Vertical scroll state for the editor
    val verticalScrollState = rememberScrollState()
    // Horizontal scroll state for the editor
    val horizontalScrollState = rememberScrollState()
    // Coroutine scope for handling coroutines within the Composable
    val coroutineScope = rememberCoroutineScope()

    // If the current file exists, write the editor content to the associated file
    if (File(state.filePath.value).exists()) DocumentsManager.writeFile(File(state.filePath.value), state.codeText.value.text)

    // LaunchedEffect to request focus for the TextField when the view is created
    LaunchedEffect(Unit){ state.textFieldFocusRequester.value.requestFocus() }

    Column(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScrollState)
            ) {
                Row {

                    LinesNumber(state.linesCount.value)

                    Spacer(modifier = Modifier.width(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        CompositionLocalProvider(LocalTextContextMenu provides EmptyContextMenu){
                            BasicTextField(
                                value = state.codeText.value,
                                onValueChange = { state.onValueChange(it) },
                                readOnly = state.readOnly.value,
                                onTextLayout = { state.onTextLayout(it){ coroutineScope.launch { verticalScrollState.scrollTo(state.lineIndex.value * 10) } } },
                                textStyle = TextStyle(
                                    fontSize = 13.sp,
                                    color = ThemeApp.colors.textColor,
                                    fontFamily = ThemeApp.text.codeTextFontFamily,
                                    fontWeight = FontWeight.W500
                                ),
                                cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                                visualTransformation = EditorVisualTransformation(state.syntaxHighlightConfig.value, state.syntaxHighlightRegexModel.value),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(state.textFieldFocusRequester.value)
                                    .onPreviewKeyEvent{ editorKeyEvents(it, state) }
                            )
                        }

                        Box(modifier = Modifier.fillMaxHeight().width(200.dp))

                    }

                }

                Box(modifier = Modifier.fillMaxWidth().height(200.dp))
            }

            // Displays the visual indicator of the selected line
            selectedLine(
                17 * (if(state.lineIndex.value != 0) state.lineIndex.value - 1 else state.lineIndex.value) - verticalScrollState.value,
            )

            // Display teh auto-complete dropdown if suggestions are available
            if (state.isAutoCompleteVisible.value) {
                AutocompleteDropdownView(
                    items = state.autoCompleteSuggestions.value,
                    editorState = state,
                    cursorX = state.cursorXCoordinate.value - horizontalScrollState.value,
                    cursorY = state.cursorYCoordinate.value - verticalScrollState.value,
                    selectedItemIndex = state.selectedItemIndex.value,
                    state.variableDirectives.value
                )
            } else state.selectedItemIndex.value = 0

            // Vertical scroll for the code editor
            VerticalScrollbar(
                ScrollbarAdapter(verticalScrollState),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                style = ThemeApp.scrollbar.scrollbarStyle
            )

            // Horizontal scroll for the code editor
            HorizontalScrollbar(
                ScrollbarAdapter(horizontalScrollState),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                style = ThemeApp.scrollbar.scrollbarStyle
            )

        }

        // Create the bottom actions row
        BottomActionsRow(state)
    }
}