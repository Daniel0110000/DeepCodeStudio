package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.extensions.mouseEventListener
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * [JPanel] that represents a button to select a JSON file
 *
 * @param onClickListener Callback function invoked when the button is clicked
 */
class SelectJsonButton(
    private val onClickListener: () -> Unit
): JPanel() {

    init {
        layout = BorderLayout()
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

        mouseEventListener(
            onEnter = { background = ThemeApp.colors.hoverTab.toAWTColor() },
            onExit = { background = ThemeApp.awtColors.secondaryColor },
            onClick = { onClickListener() }
        )

        add(JLabel(AppIcons.simpleFolderIcon), BorderLayout.CENTER)
    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 6, 6)
    }

}