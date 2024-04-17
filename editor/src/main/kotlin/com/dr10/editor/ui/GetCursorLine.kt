package com.dr10.editor.ui

import androidx.compose.ui.text.input.TextFieldValue

/**
 * Determines the line number where the cursor is located within text field
 *
 * @param textFieldValue The TextFieldValue object representing the text field content and cursor position
 * @return The line number where the cursor is located. Returns 1 if the text is empty or the cursor is at the beginning
 */
fun getCursorLine(textFieldValue: TextFieldValue): Int {
    val text = textFieldValue.text
    val selectionStart = textFieldValue.selection.start

    if (text.isEmpty() || selectionStart <= 0) return 1

    var lineCount = 1
    var index = 0

    while (index < text.length && index < selectionStart) {
        if (text[index] == '\n') {
            lineCount++
        }
        index++
    }

    return lineCount
}