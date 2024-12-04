package com.dr10.settings.ui.screens.colorScheme.components

import com.dr10.common.models.ColorSchemeModel
import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.settings.ui.viewModels.ColorSchemesViewModel
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

class ColorSchemeHeader(
    private val colorSchemesViewModel: ColorSchemesViewModel,
    private val model: ColorSchemeModel
): JPanel() {

    val indicationIcon = JLabel(AppIcons.arrowRightIcon)

    init { onCreate() }

    private fun onCreate() {
        val colorSchemeHeaderLayout = GroupLayout(this)
        layout = colorSchemeHeaderLayout
        isOpaque = false
        background = ThemeApp.awtColors.primaryColor

        val optionName = JLabel(model.optionName).apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
        }

        addMouseListener(object: MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                val index = colorSchemesViewModel.getIndex(model)
                if (e.clickCount == 1) colorSchemesViewModel.setSelectedOptionIndex(index)
                if (e.clickCount == 2) colorSchemesViewModel.setIsExpandColorsList(index)
            }
        })

        colorSchemeHeaderLayout.setHorizontalGroup(
            colorSchemeHeaderLayout.createSequentialGroup()
                .addGap(5)
                .addComponent(indicationIcon)
                .addComponent(optionName)
                .addGap(0, 0, Short.MAX_VALUE.toInt())
        )

        colorSchemeHeaderLayout.setVerticalGroup(
            colorSchemeHeaderLayout.createSequentialGroup()
                .addGap(3)
                .addGroup(
                    colorSchemeHeaderLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(indicationIcon)
                        .addComponent(optionName)
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