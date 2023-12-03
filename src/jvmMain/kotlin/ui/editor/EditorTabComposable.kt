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

/**
 * A type alias for an Editor Composable function
 * It takes a [EditorState] and composes the editor UI
 */
typealias EditorComposable = @Composable (EditorState) -> Unit

@OptIn(ExperimentalFoundationApi::class)
val EditorTabComposable: EditorComposable = { editorState ->

    // Inject [SettingRepository]
    val repository: SettingRepository = App().settingRepository

    // Scroll state for the text editor
    val scrollState = rememberScrollState()
    // Coroutine scope for handling coroutines within the Composable
    val coroutineScope = rememberCoroutineScope()

    // If the current file exists, write the editor content to the associated file
    if (File(editorState.filePath.value).exists()) DocumentsManager.writeFile(File(editorState.filePath.value), editorState.codeText.value.text)

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Row {

                    LinesNumberView(editorState.linesCount.value)

                    Spacer(modifier = Modifier.width(10.dp))

                    CompositionLocalProvider(
                        LocalTextContextMenu provides EmptyContextMenu
                    ){
                        BasicTextField(
                            value = editorState.codeText.value,
                            onValueChange = {
                                if (it.text != editorState.codeText.value.text) {
                                    val selectedWord = TextUtils.extractSurroundingWord(it.selection.start, it.text)
                                    editorState.wordToSearch.value = selectedWord

                                    editorState.autoCompleteSuggestions.value =
                                        KeywordAutoCompleteUtil.autocompleteKeywords(selectedWord, editorState.keywords.value) +
                                                KeywordAutoCompleteUtil.filterVariableNamesForAutocomplete(
                                                    TextUtils.extractVariableNames(editorState.codeText.value.text, editorState.variableDirectives.value),
                                                    selectedWord
                                                ) +
                                                KeywordAutoCompleteUtil.filterFunctionNamesForAutocomplete(
                                                    TextUtils.extractFunctionNames(editorState.codeText.value.text),
                                                    selectedWord
                                                )

                                    editorState.isAutoCompleteVisible.value = editorState.autoCompleteSuggestions.value.isNotEmpty()
                                    editorState.displayErrorLine.value = false
                                    editorState.displayWarningLine.value = false
                                } else {
                                    editorState.autoCompleteSuggestions.value = emptyList()
                                    editorState.isAutoCompleteVisible.value = false
                                }

                                editorState.lineIndex.value = getCursorLine(it)
                                editorState.codeText.value = it

                            },
                            readOnly = editorState.readOnly.value,
                            onTextLayout = {
                                val lineCount = it.lineCount
                                if (lineCount != editorState.linesCount.value) editorState.linesCount.value = lineCount
                                editorState.textLayoutResult.value = it
                                editorState.cursorXCoordinate.value = it.getHorizontalPosition(
                                    editorState.codeText.value.selection.start,
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
                            visualTransformation = EditorVisualTransformation(editorState.syntaxHighlightConfig.value),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(editorState.textFieldFocusRequester.value)
                                .onPreviewKeyEvent{
                                    editorKeyEvents(it, editorState){ coroutineScope.launch { scrollState.scrollTo(scrollState.value  * 10) } }
                                }
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().height(200.dp))
            }

            // Displays the visual indicator of the selected line
            selectedLine(
                17 * (if(editorState.lineIndex.value != 0) editorState.lineIndex.value - 1 else editorState.lineIndex.value) - scrollState.value,
            )

            // If [displayErrorLine] is true, it displays the error line in the editor
            if(editorState.displayErrorLine.value){
                errorLine(
                    17 * (if(editorState.errorLineIndex.value != 0) editorState.errorLineIndex.value - 1 else editorState.errorLineIndex.value) - scrollState.value,
                )
            }

            // If [displayWarningLine] is true, it displays the warning line in the editor
            if(editorState.displayWarningLine.value){
                warningLine(
                    17 * (if(editorState.errorLineIndex.value != 0) editorState.errorLineIndex.value - 1 else editorState.errorLineIndex.value) - scrollState.value,
                    )
            }

            // Display teh auto-complete dropdown if suggestions are available
            if (editorState.isAutoCompleteVisible.value) {
                AutoCompleteDropdown(
                    items = editorState.autoCompleteSuggestions.value,
                    editorState = editorState,
                    cursorX = editorState.cursorXCoordinate.value,
                    cursorY = editorState.cursorYCoordinate.value - scrollState.value,
                    selectedItemIndex = editorState.selectedItemIndex.value,
                    editorState.variableDirectives.value
                )
            } else editorState.selectedItemIndex.value = 0

            // Vertical scroll for the code editor
            VerticalScrollbar(
                ScrollbarAdapter(scrollState),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                style = ThemeApp.scrollbar.scrollbarStyle
            )
        }

        // Create the bottom actions row
        BottomActionsRow(repository, editorState)
    }
}