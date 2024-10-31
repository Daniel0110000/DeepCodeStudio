package com.dr10.common.ui.components

import com.dr10.common.ui.ThemeApp
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JScrollBar
import javax.swing.plaf.basic.BasicScrollBarUI


/**
 * Creates a custom scrollbar for vertical and horizontal [JScrollBar]
 *
 * @property gTrackColor The color for the track of the scrollbar
 * @property gThumbColor The color for the thumb of the scrollbar
 */
class CustomScrollBar(
    private val gTrackColor: Color = ThemeApp.AwtColors().primaryColor,
    private val gThumbColor: Color = ThemeApp.AwtColors().thumbColor
): BasicScrollBarUI() {

    override fun paintTrack(g: Graphics, c: JComponent?, trackBounds: Rectangle) {
        g.color = gTrackColor
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height)
    }

    override fun paintThumb(g: Graphics, c: JComponent?, thumbBounds: Rectangle) {
        g.color = gThumbColor
        if (scrollbar.orientation == JScrollBar.VERTICAL) {
            g.fillRoundRect(thumbBounds.x, thumbBounds.y, 8, thumbBounds.height, 2, 2)
        } else {
            g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, 8, 2, 2)
        }
    }


    override fun createIncreaseButton(orientation: Int): JButton {
        return createInvisibleButton()
    }

    override fun createDecreaseButton(orientation: Int): JButton {
        return createInvisibleButton()
    }

    private fun createInvisibleButton(): JButton {
        return JButton().apply {
            preferredSize = Dimension(0, 0)
            minimumSize = Dimension(0, 0)
            maximumSize = Dimension(0, 0)
        }
    }
}