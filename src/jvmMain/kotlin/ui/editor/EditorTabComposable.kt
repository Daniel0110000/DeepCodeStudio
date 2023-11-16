package ui.editor

import App
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.utilies.DocumentsManager
import domain.utilies.JsonUtils
import domain.utilies.TextUtils
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.components.bottomActionsRow
import ui.editor.codeAutoCompletion.AutoCompleteDropdown
import ui.editor.codeAutoCompletion.KeywordAutoCompleteUtil
import java.io.File

/**
 * A type alias for an Editor Composable function
 * It takes a [EditorState] and composes the editor UI
 */
typealias EditorComposable = @Composable (EditorState) -> Unit

@OptIn(ExperimentalComposeUiApi::class)
val EditorTabComposable: EditorComposable = { editorState ->

    // Inject [SettingRepository]
    val repository = App().settingRepository

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

                    BasicTextField(
                        value = editorState.codeText.value,
                        onValueChange = {
                            if (it.text != editorState.codeText.value.text) {
                                val selectedWord = TextUtils.extractSurroundingWord(
                                    it.selection.start,
                                    it.text
                                )
                                editorState.wordToSearch.value = selectedWord

                                editorState.autoCompleteSuggestions.value =
                                    KeywordAutoCompleteUtil.autocompleteKeywords(
                                        selectedWord,
                                        editorState.keywords.value
                                    ) +
                                            KeywordAutoCompleteUtil.filterVariableNamesForAutocomplete(
                                                TextUtils.extractVariableNames(
                                                    editorState.codeText.value.text,
                                                    editorState.variableDirectives.value
                                                ),
                                                selectedWord
                                            ) +
                                            KeywordAutoCompleteUtil.filterFunctionNamesForAutocomplete(
                                                TextUtils.extractFunctionNames(editorState.codeText.value.text),
                                                selectedWord
                                            )

                                editorState.isAutoCompleteVisible.value = editorState.autoCompleteSuggestions.value.isNotEmpty()
                            } else {
                                editorState.autoCompleteSuggestions.value = emptyList()
                                editorState.isAutoCompleteVisible.value = false
                            }

                            editorState.codeText.value = it

                        },
                        onTextLayout = {
                            val lineCount = it.lineCount
                            if (lineCount != editorState.linesCount.value) editorState.linesCount.value =
                                lineCount
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
                            .onPreviewKeyEvent {
                                if (it.key == Key.DirectionDown && editorState.isAutoCompleteVisible.value && !editorState.isKeyBeingPressed.value) {
                                    if (editorState.selectedItemIndex.value < editorState.autoCompleteSuggestions.value.size - 1) editorState.selectedItemIndex.value++
                                    editorState.isKeyBeingPressed.value = true
                                    true
                                } else if (it.key == Key.DirectionUp && editorState.isAutoCompleteVisible.value && !editorState.isKeyBeingPressed.value) {
                                    if (editorState.selectedItemIndex.value > 0) editorState.selectedItemIndex.value--
                                    editorState.isKeyBeingPressed.value = true
                                    true
                                } else if (it.key == Key.Enter && editorState.isAutoCompleteVisible.value && !editorState.isKeyBeingPressed.value) {
                                    val newText = TextUtils.insertTextAtCursorPosition(
                                        editorState.codeText.value.selection.start,
                                        editorState.codeText.value.text,
                                        editorState.autoCompleteSuggestions.value[editorState.selectedItemIndex.value]
                                    )
                                    editorState.codeText.value = TextFieldValue(
                                        newText,
                                        TextRange(editorState.codeText.value.selection.start + editorState.autoCompleteSuggestions.value[editorState.selectedItemIndex.value].length - editorState.wordToSearch.value.length)
                                    )
                                    editorState.selectedItemIndex.value = 0
                                    editorState.autoCompleteSuggestions.value = emptyList()
                                    editorState.isAutoCompleteVisible.value = false
                                    editorState.isKeyBeingPressed.value = true
                                    true
                                } else if (it.key == Key.Enter && !editorState.isKeyBeingPressed.value) {
                                    coroutineScope.launch { scrollState.scrollTo(scrollState.value + 10) }

                                    // Extract the current code text, cursor position, and text before the cursor
                                    val codeText = editorState.codeText.value.text
                                    val cursorPosition =
                                        editorState.codeText.value.selection.start
                                    val textBeforeCursor =
                                        codeText.substring(0, cursorPosition)

                                    // Extract the last line of text and split the text before the cursor into individual words
                                    val lastLine = textBeforeCursor.lines().last()
                                    val wordsInText =
                                        textBeforeCursor.split("\\s+".toRegex())

                                    // Check if specific conditions are met to determine whether to add spaces and newlines
                                    if (codeText.isNotBlank() &&
                                        textBeforeCursor.last() == ':' ||
                                        (lastLine.contains("   ") && lastLine != "   ") ||
                                        wordsInText.last() == ".data" ||
                                        wordsInText.last() == ".bss" ||
                                        wordsInText.last() == ".text"
                                    ) {
                                        editorState.codeText.value =
                                            TextUtils.insertSpacesInText(
                                                codeText,
                                                cursorPosition,
                                                "\n   "
                                            )
                                    } else {
                                        editorState.codeText.value =
                                            TextUtils.insertSpacesInText(
                                                codeText,
                                                cursorPosition,
                                                "\n"
                                            )
                                    }

                                    editorState.isKeyBeingPressed.value = true
                                    true
                                } else if (it.key == Key.Tab && !editorState.isKeyBeingPressed.value) {
                                    editorState.codeText.value =
                                        TextUtils.insertSpacesInText(
                                            editorState.codeText.value.text,
                                            editorState.codeText.value.selection.start,
                                            "   "
                                        )
                                    editorState.isKeyBeingPressed.value = true
                                    true
                                } else if (it.type == KeyEventType.KeyUp) {
                                    editorState.isKeyBeingPressed.value = false
                                    false
                                } else {
                                    false
                                }
                            }
                    )
                }

                Box(modifier = Modifier.fillMaxWidth().height(200.dp))
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
        bottomActionsRow(
            repository,
            editorState.filePath.value
        ){
            // Retrieve the selected autocomplete option for the current file
            val option = repository.getSelectedAutocompleteOption(editorState.filePath.value)

            // Set the autocomplete keywords, syntax highlight configuration and variable directives from the selected option
            editorState.syntaxHighlightConfig.value = repository.getSyntaxHighlightConfig(option.uuid)
            editorState.keywords.value = JsonUtils.jsonToListString(option.jsonPath)
            editorState.variableDirectives.value = JsonUtils.extractVariablesAndConstantsKeywordsFromJson(option.jsonPath)
        }

    }
}