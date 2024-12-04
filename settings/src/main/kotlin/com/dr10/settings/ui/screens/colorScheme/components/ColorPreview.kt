package com.dr10.settings.ui.screens.colorScheme.components

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.MouseInfo
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import kotlin.properties.Delegates

class ColorPreview(
    initialColor: Color,
    private val onColorPicker: (Color) -> Unit,
    private val onColorChanged: (Color) -> Unit
): JPanel() {

    private lateinit var colorPicker: ColorPicker

    var newColor: Color by Delegates.observable(initialColor){ _, _, newValue ->
        background = newValue
        colorPicker.colorValue = newValue
        onColorChanged(newValue)
    }

    init {
        background = initialColor
        isOpaque = false

        colorPicker = ColorPicker(initialColor) {
            newColor = it
            onColorPicker(it)
        }

        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val mousePosition = MouseInfo.getPointerInfo().location
                val buttonLocation = this@ColorPreview.locationOnScreen

                colorPicker.showPopup(
                    this@ColorPreview,
                    mousePosition.x - buttonLocation.x,
                    mousePosition.y - buttonLocation.y
                )
            }
        })
    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 7, 7)
    }

}