package com.dr10.editor.ui.tabs

import com.dr10.common.ui.ThemeApp
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.plaf.basic.BasicScrollBarUI

class TabsScrollbar: BasicScrollBarUI() {

    override fun paintTrack(g: Graphics, c: JComponent?, trackBounds: Rectangle) {
        g.color = ThemeApp.awtColors.secondaryColor
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, 3)
    }

    override fun paintThumb(g: Graphics, c: JComponent?, thumbBounds: Rectangle) {
        g.color = ThemeApp.awtColors.complementaryColor
        g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, 3, 2, 2)
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