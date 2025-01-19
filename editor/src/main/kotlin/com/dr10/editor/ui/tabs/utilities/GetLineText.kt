package com.dr10.editor.ui.tabs.utilities

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

/**
 * Gets the text of the specified line in the code editor
 *
 * @param line The line number to get the text from
 * @return [String] The text of the specified line
 */
fun RSyntaxTextArea.getLineText(line: Int): String? = runCatching {
    val start = getLineStartOffset(line)
    val end = getLineEndOffset(line)
    getText(start, end - start)
}.getOrNull()