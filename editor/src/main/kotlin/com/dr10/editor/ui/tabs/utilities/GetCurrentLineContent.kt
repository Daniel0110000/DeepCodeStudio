package com.dr10.editor.ui.tabs.utilities

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

/**
 * Gets the current line content of the editor
 *
 * @param editor The editor to get the current line content from
 * @param offset The offset of the current cursor position
 *
 * @return [Pair] The current line content and the line number
 */
fun getCurrentLineContent(editor: RSyntaxTextArea, offset: Int): Pair<String, Int> =
    editor.takeIf { it.document.length > 0 }?.let { textArea ->
        runCatching {
            val lineNumber = textArea.getLineOfOffset(offset.coerceIn(0, textArea.document.length))
            textArea.getLineText(lineNumber)?.let { text ->
                text.trimEnd() to lineNumber
            }
        }.getOrDefault("" to -1)
    } ?: ("" to -1)
