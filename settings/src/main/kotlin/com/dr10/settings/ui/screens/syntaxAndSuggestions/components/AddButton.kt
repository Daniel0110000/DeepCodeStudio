package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * [JPanel] that represents a button for adding a new configuration
 *
 * @param onClickListener Callback function invoked when the button is clicked
 */
class AddButton(
    private val onClickListener: () -> Unit
): JPanel() {

    init {
        layout = GridBagLayout()
        background = ThemeApp.awtColors.complementaryColor

        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) { onClickListener() }
        })

        add(
            JLabel(AppIcons.addIcon),
            GridBagConstraints().apply {
                anchor = GridBagConstraints.CENTER
            }
        )

    }

    /**
     * Paints the component with the current background color and rounded corners
     *
     * @param graphics The Graphics context to be used for painting
     */
    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10)
    }
}