package com.dr10.editor.ui.tabs

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Custom view for each tab in the editor
 *
 * @property tab The model containing information about the tab
 * @property onCloseTab Callback function that is invoked when the tab's close button is clicked
 */
class TabView(
    private val tab: TabModel,
    private val onCloseTab: (tab: TabModel) -> Unit
): JPanel() {
    init { onCreate() }

    private fun onCreate() {
        val tabLayout = GroupLayout(this)
        layout = tabLayout
        isOpaque = false
        background = ThemeApp.colors.secondColor.toAWTColor()

        val tabIcon = JLabel(AppIcons.asmIcon)
        val tabLabel = JLabel(tab.fileName).apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.colors.textColor.toAWTColor()
        }

        val tabClose = TabCloseButton{ onCloseTab(tab) }

        tabLayout.setHorizontalGroup(
            tabLayout.createSequentialGroup()
                .addComponent(tabIcon)
                .addGap(5)
                .addComponent(tabLabel)
                .addGap(5)
                .addComponent(tabClose, 18, 18, 18)
        )

        tabLayout.setVerticalGroup(
            tabLayout.createSequentialGroup()
                .addGap(8)
                .addGroup(
                    tabLayout.createParallelGroup()
                        .addComponent(tabIcon)
                        .addComponent(tabLabel)
                        .addComponent(tabClose, 18, 18, 18)
                )
                .addGap(8)
        )

    }

}