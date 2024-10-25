package com.dr10.common.ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.Graphics
import javax.swing.plaf.basic.BasicSplitPaneDivider
import javax.swing.plaf.basic.BasicSplitPaneUI

/**
 * Creates a custom divider for the split pane
 */
class CustomSplitPaneDivider: BasicSplitPaneUI() {

    override fun createDefaultDivider(): BasicSplitPaneDivider {
        return object : BasicSplitPaneDivider(this) {
            override fun paint(g: Graphics) {
                g.color = ThemeApp.colors.secondColor.toAWTColor()
                g.fillRect(0, 0, width, height)
            }
        }
    }

}