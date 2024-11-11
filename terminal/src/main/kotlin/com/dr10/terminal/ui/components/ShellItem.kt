package com.dr10.terminal.ui.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.terminal.model.ShellInfo
import com.dr10.terminal.utils.ShellType
import com.jediterm.terminal.ui.UIUtil
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

class ShellItem(
    private val shellInfo: ShellInfo
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val shellItemLayout = GroupLayout(this)
        layout = shellItemLayout

        val terminalIcon = JLabel(
            when (shellInfo.type) {
                ShellType.DEFAULT -> if(UIUtil.isWindows) AppIcons.defaultShellWindows else AppIcons.defaultShellLinux
                ShellType.BASH -> AppIcons.bashIcon
                ShellType.ZSH -> AppIcons.zshIcon
                ShellType.FISH -> AppIcons.fishIcon
                ShellType.POWERSHELL -> AppIcons.powershellIcon
                ShellType.CMD -> AppIcons.cmdIcon
            }
        )

        val terminalName = JLabel(shellInfo.name).apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
        }

        shellItemLayout.setHorizontalGroup(
            shellItemLayout.createSequentialGroup()
                .addComponent(terminalIcon)
                .addGap(5)
                .addComponent(terminalName)
                .addGap(5)
        )

        shellItemLayout.setVerticalGroup(
            shellItemLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(terminalIcon)
                .addComponent(terminalName)
        )

    }

}