package com.dr10.editor.ui

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

class EmptyEditorPanel: JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val emptyEditorViewLayout = GroupLayout(this)
        layout = emptyEditorViewLayout
        background = ThemeApp.colors.background.toAWTColor()

        val appIcon = JLabel(AppIcons.appIcon)
        val appName = JLabel("DeepCode Studio").apply {
            font = ThemeApp.text.fontInterBold(25f)
            foreground = ThemeApp.colors.secondColor.toAWTColor()
        }

        emptyEditorViewLayout.setHorizontalGroup(
            emptyEditorViewLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addGroup(
                    emptyEditorViewLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(appIcon)
                        .addComponent(appName)
                )
                .addGap(0, 0, Short.MAX_VALUE.toInt())
        )

        emptyEditorViewLayout.setVerticalGroup(
            emptyEditorViewLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(appIcon)
                .addGap(5)
                .addComponent(appName)
                .addGap(0, 0, Short.MAX_VALUE.toInt())
        )

    }
}