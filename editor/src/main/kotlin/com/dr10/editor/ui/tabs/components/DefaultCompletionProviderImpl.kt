package com.dr10.editor.ui.tabs.components

import com.dr10.autocomplete.DefaultCompletionProvider
import javax.swing.text.JTextComponent

class DefaultCompletionProviderImpl: DefaultCompletionProvider() {
    override fun getAlreadyEnteredText(comp: JTextComponent?): String {
        return if (comp != null) {
            getCurrentPrefix(comp.text, comp.caretPosition)
        } else ""
    }

    private fun getCurrentPrefix(text: String, caretPos: Int): String {
        if (text.isEmpty() || caretPos <= 0) return ""

        var start = caretPos - 1

        val specialChars = setOf('.', '%', '@', '#', '$', '_', '-', ':', '/', '\\', '&', '*', '+', '=', '!', '?', '|', '^', '~', '`', '[', ']', '{', '}', '(', ')', '<', '>')

        while (start >= 0 && (text[start].isLetterOrDigit() || text[start] in specialChars)) {
            start--
        }
        start++

        return if (start < caretPos) text.substring(start, caretPos) else ""
    }
}