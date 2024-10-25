package com.dr10.common.ui

import com.dr10.common.ui.components.CustomSplitPaneDivider
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme
import java.awt.Insets
import javax.swing.UIManager

object UIManagerConfig {
    fun config() {
        try {
            UIManager.setLookAndFeel(FlatOneDarkIJTheme())

            UIManager.put("Panel.background", ThemeApp.colors.secondColor.toAWTColor())

            UIManager.put("Tree.selectionBackground", ThemeApp.colors.buttonColor.toAWTColor())
            UIManager.put("Tree.selectionArc", 15)
            UIManager.put("Tree.selectionInsets", Insets(0, 10, 0, 10))
            UIManager.put("Tree.expandedIcon", AppIcons.expandedIcon)
            UIManager.put("Tree.collapsedIcon", AppIcons.collapseIcon)
            UIManager.put("Tree.paintLines", false)

            UIManager.put("TabbedPane.hoverColor", ThemeApp.colors.hoverTab.toAWTColor())
            UIManager.put("TabbedPane.selectedBackground", ThemeApp.colors.secondColor)
            UIManager.put("TabbedPane.underlineColor", ThemeApp.colors.buttonColor.toAWTColor())
            UIManager.put("TabbedPane.inactiveUnderlineColor", ThemeApp.colors.buttonColor.toAWTColor())

            UIManager.put("SplitPaneUI", CustomSplitPaneDivider::class.java.name)

        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}