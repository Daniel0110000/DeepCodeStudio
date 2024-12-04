package com.dr10.settings.ui.screens.colorScheme.components

import com.dr10.common.ui.ThemeApp
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JSlider
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener
import kotlin.properties.Delegates

class ColorPicker(
    private var initialValue: Color,
    private val onColorSelected: (Color) -> Unit
): JPanel() {

    private val redLabel = createSliderLabel("Red: ${initialValue.red}")
    private val greenLabel = createSliderLabel("Green: ${initialValue.green}")
    private val blueLabel = createSliderLabel("Blue: ${initialValue.blue}")

    private val redSlider = JSlider(0, 255, initialValue.red)
    private val greenSlider = JSlider(0, 255, initialValue.green)
    private val blueSlider = JSlider(0, 255, initialValue.blue)

    var colorValue: Color by Delegates.observable(initialValue){ _, _, newValue ->
        redSlider.value = newValue.red
        greenSlider.value = newValue.green
        blueSlider.value = newValue.blue

        redLabel.text = "Red: ${newValue.red}"
        greenLabel.text = "Green: ${newValue.green}"
        blueLabel.text = "Blue: ${newValue.blue}"
    }

    private val popup = JPopupMenu().apply {
        background = ThemeApp.awtColors.secondaryColor
    }

    private var currentColor = initialValue

    private val previewColor = ColorPickerColorPreview().apply {
        background = initialValue
        isOpaque = false
    }

    init { onCreate() }

    private fun onCreate() {
        val colorPickerLayout = GroupLayout(this)
        layout = colorPickerLayout
        background = ThemeApp.awtColors.secondaryColor
        preferredSize = Dimension(200, 205)

        popup.add(this)
        popup.addPopupMenuListener(object: PopupMenuListener {
            override fun popupMenuWillBecomeVisible(e: PopupMenuEvent?) {}

            override fun popupMenuWillBecomeInvisible(e: PopupMenuEvent?) {}

            override fun popupMenuCanceled(e: PopupMenuEvent) { hidePopup() }

        })

        configureSlider()

        colorPickerLayout.setHorizontalGroup(
            colorPickerLayout.createSequentialGroup()
                .addGap(5)
                .addGroup(
                    colorPickerLayout.createParallelGroup()
                        .addComponent(previewColor, 0, 0, Short.MAX_VALUE.toInt())
                        .addComponent(redLabel)
                        .addComponent(redSlider, 0, 0, Short.MAX_VALUE.toInt())
                        .addComponent(greenLabel)
                        .addComponent(greenSlider, 0, 0, Short.MAX_VALUE.toInt())
                        .addComponent(blueLabel)
                        .addComponent(blueSlider, 0, 0, Short.MAX_VALUE.toInt())
                )
                .addGap(5)
        )

        colorPickerLayout.setVerticalGroup(
            colorPickerLayout.createSequentialGroup()
                .addGap(5)
                .addComponent(previewColor, 0, 0, 80)
                .addGap(5)
                .addComponent(redLabel)
                .addComponent(redSlider)
                .addComponent(greenLabel)
                .addComponent(greenSlider)
                .addComponent(blueLabel)
                .addComponent(blueSlider)
        )

    }

    private fun configureSlider() {
        listOf(redSlider, greenSlider, blueSlider).forEach { slider ->
            slider.apply {
                paintTicks = false
                paintLabels = false
                addChangeListener { updateColorPreview() }
            }
        }

    }

    private fun createSliderLabel(label: String): JLabel = JLabel(label).apply {
        font = ThemeApp.text.fontInterRegular(13f)
        foreground = ThemeApp.awtColors.textColor
    }

    private fun updateColorPreview() {
        val redValue = redSlider.value
        val greenValue = greenSlider.value
        val blueValue = blueSlider.value

        currentColor = Color(redValue, greenValue, blueValue)
        previewColor.background = currentColor
        redLabel.text = "Red: $redValue"
        greenLabel.text = "Green: $greenValue"
        blueLabel.text = "Blue: $blueValue"
    }

    fun showPopup(invoker: Component, x: Int, y: Int) {
        val screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration.bounds

        var popupX = x
        var popupY = y

        if (popupX + popup.preferredSize.width > screenBounds.width) popupX = screenBounds.width - popup.preferredSize.width
        if (popupY + popup.preferredSize.height > screenBounds.height) popupY = y - popup.preferredSize.height

        popup.show(invoker, popupX, popupY)
    }

    fun hidePopup() {
        onColorSelected(currentColor)
        popup.isVisible = false
    }
}