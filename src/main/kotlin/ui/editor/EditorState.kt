package ui.editor

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.TextFieldValue
import domain.model.SyntaxHighlightConfigModel
import domain.model.SyntaxHighlightRegexModel
import domain.utilies.TextUtils
import ui.editor.autocompleteCode.KeywordAutoCompleteUtil

class EditorState {

    // UUID associated with the selected from the current file
    var uuid = mutableStateOf("")

    // The content of the code editor
    val codeText = mutableStateOf(TextFieldValue(""))
    // Count of lines in the code editor
    var linesCount = mutableStateOf(1)
    val fileName = mutableStateOf("")
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

    // Syntax highlighting configuration for the editor
    var syntaxHighlightConfig = mutableStateOf(SyntaxHighlightConfigModel())
    // Syntax highlighting regex for the editor
    val syntaxHighlightRegexModel = mutableStateOf(SyntaxHighlightRegexModel())
    // Index of the currently selected line
    var lineIndex = mutableStateOf(0)

    // Indicates whether the editor is read-only or not
    var readOnly = mutableStateOf(false)

    // Visibility state of the view for selecting an autocomplete option
    var displayAutocompleteOptions = mutableStateOf(false)
    // Visibility state of the view for updating the selected autocomplete option
    var displayUpdateAutocompleteOption = mutableStateOf(false)

    /**
     * Handles the change in value of the editor's TextField
     *
     * @param value The new [TextFieldValue] after the change
     */
    fun onValueChange(value: TextFieldValue){
        // Check of the text content has changed
        if (value.text != codeText.value.text) {
            //  Extract the currently selected word based on the cursor position
            val selectedWord = TextUtils.extractSurroundingWord(value.selection.start, value.text)
            wordToSearch.value = selectedWord

            // Generate autocomplete suggestions based on the selected word, keywords, variable names, and function names
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

            // Set the visibility of the autocomplete suggestions based on whether suggestions are available
            isAutoCompleteVisible.value = autoCompleteSuggestions.value.isNotEmpty()
        } else {
            // Clear autocomplete suggestions if the text content remains unchanged
            autoCompleteSuggestions.value = emptyList()
            isAutoCompleteVisible.value = false
        }

        // Update the line index based on the cursor position in the text
        lineIndex.value = getCursorLine(value)
        // Update the [codeText] value with the new [TextFieldValue]
        codeText.value = value
    }

    /**
     * Handles the layout result of the editor's text, updating relevant state values
     *
     * @param layout The [TextLayoutResult] containing information about the layout of the text
     */
    fun onTextLayout(layout: TextLayoutResult, onScrolling: () -> Unit){

        // Update the line index
        lineIndex.value = getCursorLine(codeText.value)

        // Extracts and update the line count
        val lineCount = layout.lineCount
        if (lineCount != linesCount.value) linesCount.value = lineCount

        // Updates the textLayoutResult
        textLayoutResult.value = layout

        // Gets the horizontal position
        cursorXCoordinate.value = layout.getHorizontalPosition(
            codeText.value.selection.start,
            true
        ).toInt()

        onScrolling()
    }

}