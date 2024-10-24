package com.dr10.editor.ui.tabs

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel

/**
 * Extension function for [JPanel] that simplifies mouse event handling
 *
 * @param onEnter Callback function is called when mouse enters the panel
 * @param onExit Callback function is called when mouse exits the panel
 * @param onClick Callback function is called when panel is clicked
 */
fun JPanel.mouseEventListener(
    onEnter: () -> Unit,
    onExit: () -> Unit,
    onClick: () -> Unit
) {
    isOpaque = false
    addMouseListener(object : MouseAdapter() {
        override fun mouseEntered(e: MouseEvent) {
            onEnter()
            repaint()
        }

        override fun mouseExited(e: MouseEvent) {
            onExit()
            repaint()
        }

        override fun mouseClicked(e: MouseEvent) { onClick() }

    })
}