package com.dr10.editor.ui.tabs

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.TabCloseButton
import com.dr10.common.ui.extensions.mouseEventListener
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

class TabView(
    private val tabData: TabData,
    private val onSelectTab: (Int) -> Unit,
    private val onCloseTab: (TabData) -> Unit
): JPanel() {

    private var backgroundColor = ThemeApp.awtColors.secondaryColor

    private val tabLayout = GroupLayout(this)

    private lateinit var tabIcon: JLabel
    private lateinit var titleLabel: JLabel
    private lateinit var closeButton: TabCloseButton

    init {
        layout = tabLayout
        background = ThemeApp.awtColors.secondaryColor

        mouseEventListener(
            onClick = { onSelectTab(tabData.index) },
            onEnter = {
                backgroundColor = ThemeApp.awtColors.hoverColor
            },
            onExit = {
                backgroundColor = ThemeApp.awtColors.secondaryColor
            }
        )

        initializeComponents()
        setComponentsStructure()
    }

    private fun initializeComponents() {
        tabIcon = JLabel(
            if (tabData.tabModel.fileName.endsWith(".asm") || tabData.tabModel.fileName.endsWith(".s")) AppIcons.asmIcon
            else AppIcons.makefileIcon
        )

        titleLabel = JLabel(tabData.tabModel.fileName).apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.colors.textColor.toAWTColor()
        }

        closeButton = TabCloseButton { onCloseTab(tabData) }
    }

    private fun setComponentsStructure() {
        tabLayout.setHorizontalGroup(
            tabLayout.createSequentialGroup()
                .addGap(7)
                .addComponent(tabIcon)
                .addGap(5)
                .addComponent(titleLabel)
                .addGap(5)
                .addComponent(closeButton, 18, 18, 18)
                .addGap(7)
        )

        tabLayout.setVerticalGroup(
            tabLayout.createSequentialGroup()
                .addGap(9)
                .addGroup(
                    tabLayout.createParallelGroup()
                        .addComponent(tabIcon)
                        .addComponent(titleLabel)
                        .addComponent(closeButton, 18, 18, 18)
                )
                .addGap(9)
        )
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val graphics2D = g as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics2D.color = backgroundColor
        graphics2D.fillRect(0, 0, width, height)
    }

}