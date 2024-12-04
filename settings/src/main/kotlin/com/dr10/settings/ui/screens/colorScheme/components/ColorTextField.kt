package com.dr10.settings.ui.screens.colorScheme.components

import com.dr10.common.ui.ThemeApp
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.AbstractDocument

class ColorTextField: JPanel() {

    val textField = JTextField()
    var onValueChange: (String) -> Unit = {}

    init { onCreate() }

    private fun onCreate() {
        layout = BorderLayout()
        background = ThemeApp.awtColors.secondaryColor
        border = EmptyBorder(1, 5, 1, 3)

        textField.apply {
            border = EmptyBorder(0, 0, 0, 0)
            background = ThemeApp.awtColors.secondaryColor
            caretColor = ThemeApp.awtColors.complementaryColor
            selectionColor = ThemeApp.awtColors.complementaryColor
            font = ThemeApp.text.fontInterRegular(13f)
            addActionListener { onValueChange(textField.text) }
        }

        val document = textField.document as AbstractDocument
        document.documentFilter = NumericLengthFilter()

        textField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) {
                onValueChange(textField.text)
            }

            override fun removeUpdate(e: DocumentEvent?) {
                onValueChange(textField.text)
            }

            override fun changedUpdate(e: DocumentEvent?) {
                onValueChange(textField.text)
            }

        })

        add(textField, BorderLayout.CENTER)

    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 7, 7)
    }

}