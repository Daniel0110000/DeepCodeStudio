package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import javax.swing.text.AttributeSet
import javax.swing.text.DocumentFilter

class FirstCharAlphaFilter: DocumentFilter() {
    override fun insertString(fb: FilterBypass?, offset: Int, string: String?, attr: AttributeSet?) {
        applyChange(fb!!, string, offset, 0) { text -> fb.insertString(offset, text, attr) }
    }

    override fun replace(fb: FilterBypass?, offset: Int, length: Int, text: String?, attrs: AttributeSet?) {
        applyChange(fb!!, text, offset, length) { validText -> fb.replace(offset, length, validText, attrs) }
    }

    private fun applyChange(fb: FilterBypass, text: String?, offset: Int, length: Int, action: (String) -> Unit) {
        if (text.isNullOrEmpty()) {
            action("")
            return
        }

        val currentText = fb.document.getText(0, fb.document.length) ?: ""
        val proposedText = currentText.replaceRange(offset, offset + length, text)

        if (isValidFirstChar(proposedText)) action(text)
    }

    private fun isValidFirstChar(text: String): Boolean = text.firstOrNull()?.isLetter() ?: true
}