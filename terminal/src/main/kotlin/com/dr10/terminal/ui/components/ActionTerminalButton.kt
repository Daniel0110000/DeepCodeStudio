package com.dr10.terminal.ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.extensions.mouseEventListener
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.JPanel

class ActionTerminalButton(
    private val icon: Icon,
    private val onClickListener: () -> Unit
): JPanel() {

    init {
        layout = BorderLayout()

        mouseEventListener(
            onEnter = { background = ThemeApp.colors.hoverTab.toAWTColor() },
            onExit = { background = ThemeApp.awtColors.secondaryColor },
            onClick = { onClickListener() }
        )

        add(JLabel(icon), BorderLayout.CENTER)

    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 8, 8)
    }

}