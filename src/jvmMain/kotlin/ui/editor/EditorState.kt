package ui.editor

import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.TextFieldValue

class EditorState {
    // The content of the code editor
    val codeText = mutableStateOf(TextFieldValue(""))
    // Count of lines in the code editor
    var linesCount = mutableStateOf(0)
    // File path associated with the editor content
    val filePath = mutableStateOf("")

    // Suggestions for aut-complete
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

    // Flag indicating whether editor is visible
    val displayEditor = mutableStateOf(false)
    // Flag indicating whether all autocomplete options dialog are visible
    val displayAllAutocompleteOptions = mutableStateOf(false)

}