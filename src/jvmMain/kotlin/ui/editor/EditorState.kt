package ui.editor

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.TextFieldValue
import domain.model.SyntaxHighlightConfigModel

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
    var textLayoutResult = mutableStateOf<TextLayoutResult?>(null)
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

    // Stores the visibility of the error line in the editor
    var displayErrorLine = mutableStateOf(false)
    // Stores the visibility of the warning line in the editor
    var displayWarningLine = mutableStateOf(false)
    // Stores the index of the error line in the editor.
    var errorLineIndex = mutableStateOf(0)

    // Indicates whether the editor is read-only or not
    var readOnly = mutableStateOf(false)

}