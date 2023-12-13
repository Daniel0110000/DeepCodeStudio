package ui.editor

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.TextFieldValue
import domain.model.SyntaxHighlightConfigModel
import domain.utilies.TextUtils
import ui.editor.autocompleteCode.KeywordAutoCompleteUtil

class EditorState {
    // The content of the code editor
    val codeText = mutableStateOf(TextFieldValue(""))
    // Count of lines in the code editor
    var linesCount = mutableStateOf(1)
    // File path associated with the editor content
    val filePath = mutableStateOf("")

    // Suggestions for auto-complete
    var autoCompleteSuggestions = mutableStateOf<List<String>>(emptyList())
    // Result of text layout calculations
    private var textLayoutResult = mutableStateOf<TextLayoutResult?>(null)
    // Index of the current line where the cursor is located
    private val cursorLineIndex = derivedStateOf { textLayoutResult.value?.getLineForOffset(codeText.value.selection.start) ?: 0 }
    // Y-coordinate of the cursor
    val cursorYCoordinate = derivedStateOf { (textLayoutResult.value?.getLineBottom(cursorLineIndex.value) ?: 0f).toInt() }
    // X-coordinate of teh cursor
    var cursorXCoordinate = mutableStateOf(0)

    // Flag indicating whether auto-complete suggestions are visible
    var isAutoCompleteVisible = mutableStateOf(false)
    // Index of the selected item in auto-complete suggestions
    var selectedItemIndex = mutableStateOf(0)
    // Flag indicating whether a key is currently being pressed
    var isKeyBeingPressed = mutableStateOf(false)
    // Focus requester for the text field
    val textFieldFocusRequester = mutableStateOf(FocusRequester())
    // The word to search within the editor content
    val wordToSearch = mutableStateOf("")

    // List of keywords for autocompletion
    var keywords = mutableStateOf<List<String>>(emptyList())
    // String with variable declaration directives for extracting variable names
    var variableDirectives = mutableStateOf("")

    // Syntax Highlighting Configuration for the Editor
    var syntaxHighlightConfig = mutableStateOf(SyntaxHighlightConfigModel())
    // Index of the currently selected line
    var lineIndex = mutableStateOf(0)

    // Indicates whether the editor is read-only or not
    var readOnly = mutableStateOf(false)

    // Visibility state of the view for selecting an autocomplete option
    var displayAutocompleteOptions = mutableStateOf(false)
    // Visibility state of the view for updating the selected autocomplete option
    var displayUpdateAutocompleteOption = mutableStateOf(false)

    fun onValueChange(value: TextFieldValue){
        if (value.text != codeText.value.text) {
            val selectedWord = TextUtils.extractSurroundingWord(value.selection.start, value.text)
            wordToSearch.value = selectedWord

            autoCompleteSuggestions.value =
                KeywordAutoCompleteUtil.autocompleteKeywords(selectedWord, keywords.value) +
                        KeywordAutoCompleteUtil.filterVariableNamesForAutocomplete(
                            TextUtils.extractVariableNames(codeText.value.text, variableDirectives.value),
                            selectedWord
                        ) +
                        KeywordAutoCompleteUtil.filterFunctionNamesForAutocomplete(
                            TextUtils.extractFunctionNames(codeText.value.text),
                            selectedWord
                        )

            isAutoCompleteVisible.value = autoCompleteSuggestions.value.isNotEmpty()
        } else {
            autoCompleteSuggestions.value = emptyList()
            isAutoCompleteVisible.value = false
        }

        lineIndex.value = getCursorLine(value)
        codeText.value = value
    }

    fun onTextLayout(layout: TextLayoutResult){
        val lineCount = layout.lineCount
        if (lineCount != linesCount.value) linesCount.value = lineCount
        textLayoutResult.value = layout
        cursorXCoordinate.value = layout.getHorizontalPosition(
            codeText.value.selection.start,
            true
        ).toInt()
    }

}