package com.dr10.settings.ui.screens.colorScheme.components

import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel

class ColorPickerColorPreview: JPanel() {

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 7, 7)
    }

}