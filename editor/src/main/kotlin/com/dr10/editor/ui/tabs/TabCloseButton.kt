package com.dr10.editor.ui.tabs

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Custom button used for closing tabs in the editor
 *
 * @property onCloseTab Callback function to be invoked when the button is clicked
 */
class TabCloseButton(
    private val onCloseTab: () -> Unit
): JPanel() {

    var backgroundColor = Color(0,0,0,1)

    init { onCreate() }

    private fun onCreate() {
        layout = GridBagLayout()
        background = Color.CYAN
        isOpaque = false

        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent) {
                backgroundColor = ThemeApp.colors.hoverTab.toAWTColor()
                repaint()
            }

            override fun mouseExited(e: MouseEvent) {
                backgroundColor = Color(0,0,0,1)
                repaint()
            }

            override fun mouseClicked(e: MouseEvent) { onCloseTab() }
        })

        add(
            JLabel(AppIcons.closeIcon),
            GridBagConstraints().apply {
                anchor = GridBagConstraints.CENTER

            }
        )
    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = backgroundColor
        graphics.fillRoundRect(0, 0, 18 - 1, 18 - 1, 50, 50)

    }

}