package com.dr10.editor.ui

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.dr10.common.utilities.TextUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun editorKeyEvents(
    keyEvent: KeyEvent,
    editorState: EditorState
): Boolean{
    return when {
        (keyEvent.key == Key.DirectionDown && editorState.isAutoCompleteVisible.value && !editorState.isKeyBeingPressed.value) -> {
            if (editorState.selectedItemIndex.value < editorState.autoCompleteSuggestions.value.size - 1) editorState.selectedItemIndex.value++
            editorState.isKeyBeingPressed.value = true
            true
        }
        (keyEvent.key == Key.DirectionUp && editorState.isAutoCompleteVisible.value && !editorState.isKeyBeingPressed.value) -> {
            if (editorState.selectedItemIndex.value > 0) editorState.selectedItemIndex.value--
            editorState.isKeyBeingPressed.value = true
            true
        }
        ((keyEvent.key == Key.Enter || keyEvent.key == Key.Tab) && editorState.isAutoCompleteVisible.value && !editorState.isKeyBeingPressed.value) -> {
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
        }
        (keyEvent.key == Key.Enter && !editorState.isKeyBeingPressed.value) -> {
            editorState.codeText.value = TextUtils.insertSpacesAfterLineBreak(editorState.codeText.value)

            editorState.isKeyBeingPressed.value = true
            true
        }
        (keyEvent.key == Key.Tab && !editorState.isKeyBeingPressed.value) -> {
            editorState.codeText.value =
                TextUtils.insertSpacesInText(
                    editorState.codeText.value.text,
                    editorState.codeText.value.selection.start,
                    "   "
                )
            editorState.isKeyBeingPressed.value = true
            true
        }
        (keyEvent.isShiftPressed && keyEvent.key == Key(java.awt.event.KeyEvent.VK_QUOTE) && !editorState.isKeyBeingPressed.value) -> {
            CoroutineScope(Dispatchers.IO).launch {
                delay(15) // Delay for 15 milliseconds
                // Check of the [editorState.wordToSearch] is not empty and is equal to a double quote character
                if(editorState.wordToSearch.value.isNotEmpty() && editorState.wordToSearch.value == '"'.toString()){
                    // Insert double quotes at the cursor position in the [editorState.codeText]
                    val newText = TextUtils.insertTextAtCursorPosition(
                        editorState.codeText.value.selection.start,
                        editorState.codeText.value.text,
                        "\"\""
                    )
                    editorState.codeText.value = TextFieldValue(newText, TextRange(editorState.codeText.value.selection.start))
                }
            }

            editorState.isKeyBeingPressed.value = true
            false
        }
        (keyEvent.isCtrlPressed && keyEvent.key == Key.D && !editorState.isKeyBeingPressed.value) -> {
            val codeText = editorState.codeText.value.text
            // Splits the text into line breaks and then retrieve the text from the current line using the index
            // ... of the current line minus 1 [editorState.lineIndex.value - 1], then remove any trailing
            // ... whitespace from the text
            val lineToPaste = codeText.split('\n')[editorState.lineIndex.value - 1].trimEnd()

            // Using the function [insertCopyLine], insert the value of [lineToPaste] into the code and
            // ... save the result in the variable [newText]
            val newText = TextUtils.insertCopyLine(
                codeText.indexOf(lineToPaste) + lineToPaste.length,
                codeText,
                "\n$lineToPaste"
            )

            // Updates the content of [editorState.codeText.value] using the value of [newText].
            editorState.codeText.value = TextFieldValue(newText, TextRange(editorState.codeText.value.selection.start + lineToPaste.length + 1))

            editorState.isKeyBeingPressed.value = true
            true
        }
        (keyEvent.type == KeyEventType.KeyUp) -> {
            editorState.isKeyBeingPressed.value = false
            false
        }
        else -> false
    }
}