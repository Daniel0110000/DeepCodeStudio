package com.dr10.terminal.ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.terminal.model.ShellInfo
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

class AvailableShellsCellRender: DefaultListCellRenderer() {
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val item = ShellItem(value as ShellInfo)
        item.background = if(isSelected) ThemeApp.awtColors.complementaryColor
        else ThemeApp.awtColors.secondaryColor
        return item
    }

}