package com.dr10.settings.ui.screens.colorScheme.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.extensions.mouseEventListener
import com.dr10.common.utilities.ColorUtils
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

class ColorScheme(
    private val model: ColorSchemeOption,
    private val onColorChanged: (Color) -> Unit
): JPanel() {

    private val redTextField = ColorTextField()
    private val greenTextField = ColorTextField()
    private val blueTextField = ColorTextField()
    private lateinit var colorPreview: ColorPreview

    private val initialColor: Color
        get() = ColorUtils.stringToColor(model.color)

    private var isUpdatingFromPreview: Boolean = false

    init { onCreate() }

    private fun onCreate() {
        val colorSchemeLayout = GroupLayout(this)
        layout = colorSchemeLayout
        background = ThemeApp.awtColors.primaryColor

        mouseEventListener(
            onEnter = { background = ThemeApp.awtColors.hoverColor },
            onExit = { background = ThemeApp.awtColors.primaryColor }
        )

        val colorSchemeTitle = titleLabel(model.title)

        val redLabel = titleLabel("R:")
        redTextField.textField.text = initialColor.red.toString()
        redTextField.onValueChange = { value ->
            if(value.isNotBlank() && !isUpdatingFromPreview) {
                colorPreview.newColor = Color(value.toInt(), colorPreview.newColor.green, colorPreview.newColor.blue)
            }
        }

        val greenLabel = titleLabel("G:")
        greenTextField.textField.text = initialColor.green.toString()
        greenTextField.onValueChange = { value ->
            if(value.isNotBlank() && !isUpdatingFromPreview) {
                colorPreview.newColor = Color(colorPreview.newColor.red, value.toInt(), colorPreview.newColor.blue)
            }
        }

        val blueLabel = titleLabel("B:")
        blueTextField.textField.text = initialColor.blue.toString()
        blueTextField.onValueChange = { value ->
            if(value.isNotBlank() && !isUpdatingFromPreview) {
                colorPreview.newColor = Color(colorPreview.newColor.red, colorPreview.newColor.green, value.toInt())
            }
        }

        colorPreview = ColorPreview(
            ColorUtils.stringToColor(model.color),
            onColorPicker = { color ->
                isUpdatingFromPreview = true
                redTextField.textField.text = color.red.toString()
                greenTextField.textField.text = color.green.toString()
                blueTextField.textField.text = color.blue.toString()
                isUpdatingFromPreview = false
            },
            onColorChanged = { color -> onColorChanged(color) }
        )


        colorSchemeLayout.setHorizontalGroup(
            colorSchemeLayout.createSequentialGroup()
                .addGap(30)
                .addComponent(colorSchemeTitle)
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(colorPreview, 0, 0, 120)
                .addGap(7)
                .addComponent(redLabel)
                .addGap(3)
                .addComponent(redTextField, 0, 0, 40)
                .addGap(5)
                .addComponent(greenLabel)
                .addGap(3)
                .addComponent(greenTextField, 0, 0, 40)
                .addGap(5)
                .addComponent(blueLabel)
                .addGap(3)
                .addComponent(blueTextField, 0, 0, 40)
                .addGap(5)
        )

        colorSchemeLayout.setVerticalGroup(
            colorSchemeLayout.createSequentialGroup()
                .addGap(3)
                .addGroup(
                    colorSchemeLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(colorSchemeTitle)
                        .addComponent(colorPreview, 22, 22, 22)
                        .addComponent(redLabel)
                        .addComponent(redTextField, 25, 25, 25)
                        .addComponent(greenLabel)
                        .addComponent(greenTextField, 25, 25, 25)
                        .addComponent(blueLabel)
                        .addComponent(blueTextField, 25, 25, 25)
                )
                .addGap(3)
        )

    }

    private fun titleLabel(text: String): JLabel = JLabel(text).apply {
        font = ThemeApp.text.fontInterRegular(13f)
        foreground = ThemeApp.awtColors.textColor
    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10)
    }


}