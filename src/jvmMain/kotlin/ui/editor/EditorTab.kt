package ui.editor

import App
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import domain.repository.SettingRepository
import domain.utilies.DocumentsManager
import domain.utilies.TextUtils
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.components.BottomActionsRow
import ui.editor.codeAutoCompletion.AutoCompleteDropdown
import ui.editor.codeAutoCompletion.KeywordAutoCompleteUtil
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorTab(
    modifier: Modifier,
    state: EditorState
){

    // Inject [SettingRepository]
    val repository: SettingRepository = App().settingRepository

    // Vertical scroll state for the editor
    val verticalScrollState = rememberScrollState()
    // Horizontal scroll state for the editor
    val horizontalScrollState = rememberScrollState()
    // Coroutine scope for handling coroutines within the Composable
    val coroutineScope = rememberCoroutineScope()

    // If the current file exists, write the editor content to the associated file
    if (File(state.filePath.value).exists()) DocumentsManager.writeFile(File(state.filePath.value), state.codeText.value.text)

    Column(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScrollState)
            ) {
                Row {

                    LinesNumberView(state.linesCount.value)

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
                                onValueChange = {
                                    if (it.text != state.codeText.value.text) {
                                        val selectedWord = TextUtils.extractSurroundingWord(it.selection.start, it.text)
                                        state.wordToSearch.value = selectedWord

                                        state.autoCompleteSuggestions.value =
                                            KeywordAutoCompleteUtil.autocompleteKeywords(selectedWord, state.keywords.value) +
                                                    KeywordAutoCompleteUtil.filterVariableNamesForAutocomplete(
                                                        TextUtils.extractVariableNames(state.codeText.value.text, state.variableDirectives.value),
                                                        selectedWord
                                                    ) +
                                                    KeywordAutoCompleteUtil.filterFunctionNamesForAutocomplete(
                                                        TextUtils.extractFunctionNames(state.codeText.value.text),
                                                        selectedWord
                                                    )

                                        state.isAutoCompleteVisible.value = state.autoCompleteSuggestions.value.isNotEmpty()
                                    } else {
                                        state.autoCompleteSuggestions.value = emptyList()
                                        state.isAutoCompleteVisible.value = false
                                    }

                                    state.lineIndex.value = getCursorLine(it)
                                    state.codeText.value = it
                                },
                                readOnly = state.readOnly.value,
                                onTextLayout = {
                                    val lineCount = it.lineCount
                                    if (lineCount != state.linesCount.value) state.linesCount.value = lineCount
                                    state.textLayoutResult.value = it
                                    state.cursorXCoordinate.value = it.getHorizontalPosition(
                                        state.codeText.value.selection.start,
                                        true
                                    ).toInt()
                                },
                                textStyle = TextStyle(
                                    fontSize = 13.sp,
                                    color = ThemeApp.colors.textColor,
                                    fontFamily = ThemeApp.text.codeTextFontFamily,
                                    fontWeight = FontWeight.W500
                                ),
                                cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                                visualTransformation = EditorVisualTransformation(state.syntaxHighlightConfig.value),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(state.textFieldFocusRequester.value)
                                    .onPreviewKeyEvent{
                                        editorKeyEvents(it, state){ coroutineScope.launch { verticalScrollState.scrollTo(verticalScrollState.value  * 10) } }
                                    }
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
                AutoCompleteDropdown(
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
        BottomActionsRow(repository, state)
    }
}