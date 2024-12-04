package com.dr10.editor.ui.tabs.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Custom [JPanel] that represents a single autocomplete option
 *
 * @property optionName The name of the option to display
 */
class AutoCompleteOption(
    private val optionName: String
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val autocompleteOptionLayout = GroupLayout(this)
        layout = autocompleteOptionLayout
        isOpaque = false

        val optionIcons = JLabel(AppIcons.asmIcon)
        val optionLabel = JLabel(optionName).apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.colors.textColor.toAWTColor()
        }

        autocompleteOptionLayout.setHorizontalGroup(
            autocompleteOptionLayout.createSequentialGroup()
                .addGap(10)
                .addComponent(optionIcons)
                .addGap(5)
                .addComponent(optionLabel)
        )

        autocompleteOptionLayout.setVerticalGroup(
            autocompleteOptionLayout.createSequentialGroup()
                .addGap(7)
                .addGroup(
                    autocompleteOptionLayout.createParallelGroup()
                        .addComponent(optionIcons)
                        .addComponent(optionLabel)
                )
                .addGap(7)
        )

    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width, height, 10, 10)
    }

}