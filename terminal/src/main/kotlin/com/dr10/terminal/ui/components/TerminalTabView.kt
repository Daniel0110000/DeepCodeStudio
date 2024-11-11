package com.dr10.terminal.ui.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.TabCloseButton
import com.dr10.terminal.model.ShellInfo
import com.dr10.terminal.utils.ShellType
import com.jediterm.terminal.ui.UIUtil
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

class TerminalTabView(
    private val shellInfo: ShellInfo,
    private val onCloseTerminal: () -> Unit
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val terminalTabViewLayout = GroupLayout(this)
        layout = terminalTabViewLayout
        isOpaque = false
        background = ThemeApp.awtColors.secondaryColor

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

        val tabCloseButton = TabCloseButton { onCloseTerminal() }

        terminalTabViewLayout.setHorizontalGroup(
            terminalTabViewLayout.createSequentialGroup()
                .addComponent(terminalIcon)
                .addGap(5)
                .addComponent(terminalName)
                .addGap(5)
                .addComponent(tabCloseButton)
        )

        terminalTabViewLayout.setVerticalGroup(
            terminalTabViewLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(terminalIcon)
                .addComponent(terminalName)
                .addComponent(tabCloseButton)
        )

    }

}