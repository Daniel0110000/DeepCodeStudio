package com.dr10.editor.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.editor.EditorVisualTransformation
import com.dr10.common.utilities.DocumentsManager
import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import com.dr10.editor.ui.autocompleteCode.AutocompleteDropdownView
import com.dr10.editor.ui.components.BottomActionsRow
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorViewTab(
    modifier: Modifier,
    state: EditorState,
    autocompleteSettingsRepository: AutocompleteSettingsRepository
){

    // Vertical scroll state for the editor
    val verticalScrollState = rememberScrollState()
    // Horizontal scroll state for the editor
    val horizontalScrollState = rememberScrollState()

    // If the current file exists, write the editor content to the associated file
    if (File(state.filePath.value).exists()) DocumentsManager.writeFile(File(state.filePath.value), state.codeText.value.text)

    // LaunchedEffect to request focus for the TextField when the view is created
    LaunchedEffect(Unit){ state.textFieldFocusRequester.value.requestFocus() }

    Column(modifier) {
        BoxWithConstraints(Modifier.weight(1f).fillMaxWidth()) {
            val boxHeight = this.maxHeight

            Column(
                Modifier
                    .height(boxHeight)
                    .fillMaxWidth()
                    .verticalScroll(verticalScrollState)
            ) {

                Row(Modifier.fillMaxWidth()) {

                    Spacer(Modifier.width(10.dp))

                    LinesNumber(
                        state.linesCount.value,
                        modifier = Modifier.fillMaxHeight()
                    )

                    Spacer(Modifier.width(10.dp))

                    Box(
                        Modifier
                            .width(1.dp)
                            .background(ThemeApp.colors.hoverTab)
                            .heightIn(min = boxHeight)
                    )

                    Spacer(Modifier.width(5.dp))


                    CompositionLocalProvider(LocalTextContextMenu provides EmptyContextMenu){
                        BasicTextField(
                            value = state.codeText.value,
                            onValueChange = { state.onValueChange(it) },
                            readOnly = state.readOnly.value,
                            onTextLayout = {
                                state.onTextLayout(it)
                                state.autoScroll(verticalScrollState, boxHeight)
                            },
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                                color = ThemeApp.colors.textColor,
                                fontFamily = ThemeApp.text.codeTextFontFamily,
                                fontWeight = FontWeight.W500
                            ),
                            cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                            visualTransformation = EditorVisualTransformation(state.syntaxHighlightConfig.value, state.syntaxHighlightRegexModel.value),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = boxHeight)
                                .focusRequester(state.textFieldFocusRequester.value)
                                .onPreviewKeyEvent{ editorKeyEvents(it, state) }
                        )
                    }
                }

                Box(Modifier.fillMaxWidth().height(150.dp))

            }

            // Displays the visual indicator of the selected line
            selectedLine(
                state.lineHeight * (if(state.lineIndex.value != 0) state.lineIndex.value - 1 else state.lineIndex.value) - verticalScrollState.value,
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

            VerticalScrollbar(
                ScrollbarAdapter(verticalScrollState),
                modifier = Modifier.align(Alignment.CenterEnd),
                style = ThemeApp.scrollbar.scrollbarStyle
            )

        }

        BottomActionsRow(
            state,
            autocompleteSettingsRepository,
            modifier = Modifier.fillMaxWidth()
        )
    }
}