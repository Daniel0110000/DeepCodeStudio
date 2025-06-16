package com.dr10.settings.ui.components

import com.dr10.common.ui.ThemeApp
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingUtilities
import javax.swing.border.EmptyBorder

class TextField: JPanel() {

    private val textField = JTextField()

    init { onCreate() }

    private fun onCreate() {
        layout = BorderLayout()
        background = ThemeApp.awtColors.secondaryColor
        border = EmptyBorder(2, 5, 2, 5)

        textField.apply {
            border = EmptyBorder(0, 0, 0, 0)
            background = ThemeApp.awtColors.secondaryColor
            caretColor = ThemeApp.awtColors.complementaryColor
            selectionColor = ThemeApp.awtColors.complementaryColor
            font = ThemeApp.text.fontInterRegular(13f)
        }

        add(textField, BorderLayout.CENTER)

        SwingUtilities.invokeLater { textField.requestFocusInWindow() }

    }

    fun getText(): String = textField.text

    fun setText(text: String) { textField.text = text }

    fun getJTextField(): JTextField = textField

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10)
    }


}