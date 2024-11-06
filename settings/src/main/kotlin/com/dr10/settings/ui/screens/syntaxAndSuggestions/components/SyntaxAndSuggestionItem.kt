package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * [JPanel] that displays the syntax and suggestion data
 *
 * @param model The model containing the syntax and suggestion data
 */
class SyntaxAndSuggestionItem(
    private val model: SyntaxAndSuggestionModel
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val syntaxHighlightConfigItemLayout = GroupLayout(this)
        layout = syntaxHighlightConfigItemLayout
        background = ThemeApp.awtColors.complementaryColor

        val jsonIcon = JLabel(AppIcons.jsonIcon)
        val optionNameLabel = JLabel(model.optionName).apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
        }
        val jsonPathLabel = JLabel(model.jsonPath).apply {
            font = ThemeApp.text.fontInterRegular(10f)
            foreground = ThemeApp.awtColors.textColor
        }

        syntaxHighlightConfigItemLayout.setHorizontalGroup(
            syntaxHighlightConfigItemLayout.createSequentialGroup()
                .addGap(3)
                .addComponent(jsonIcon)
                .addGap(5)
                .addGroup(
                    syntaxHighlightConfigItemLayout.createParallelGroup()
                        .addComponent(optionNameLabel)
                        .addComponent(jsonPathLabel)
                )

        )

        syntaxHighlightConfigItemLayout.setVerticalGroup(
            syntaxHighlightConfigItemLayout.createSequentialGroup()
                .addGap(3)
                .addGroup(
                    syntaxHighlightConfigItemLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(jsonIcon)
                        .addGroup(
                            syntaxHighlightConfigItemLayout.createSequentialGroup()
                                .addComponent(optionNameLabel)
                                .addComponent(jsonPathLabel)
                        )
                )
                .addGap(3)
        )

    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10)
    }

}