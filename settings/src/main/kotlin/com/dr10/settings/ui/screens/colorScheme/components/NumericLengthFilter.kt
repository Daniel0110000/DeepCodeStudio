package com.dr10.settings.ui.screens.colorScheme.components

import javax.swing.text.AttributeSet
import javax.swing.text.DocumentFilter

class NumericLengthFilter: DocumentFilter() {

    override fun insertString(fb: FilterBypass?, offset: Int, string: String?, attr: AttributeSet?) {
        applyChange(fb!!, string, offset, 0) { text ->  fb.insertString(offset, text, attr) }
    }

    override fun replace(fb: FilterBypass?, offset: Int, length: Int, text: String?, attrs: AttributeSet?) {
        applyChange(fb!!, text, offset, length) { validText -> fb.replace(offset, length, validText, attrs) }
    }

    private fun applyChange(fb: FilterBypass, text: String?, offset: Int, length: Int, action: (String) -> Unit) {
        if (text.isNullOrEmpty() || !text.all { it.isDigit() }) return

        val currentText = fb.document.getText(0, fb.document.length) ?: ""
        val proposedText = currentText.replaceRange(offset, offset + length, text)

        if (isValidRGBValue(proposedText)) {
            action(text)
        }

    }

    private fun isValidRGBValue(value: String): Boolean {
        return try {
            val intValue = value.toInt()
            intValue in 0..255
        } catch (e: NumberFormatException) {
            false
        }
    }
}