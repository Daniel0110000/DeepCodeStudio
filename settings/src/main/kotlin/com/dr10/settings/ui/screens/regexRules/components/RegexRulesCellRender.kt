package com.dr10.settings.ui.screens.regexRules.components

import com.dr10.common.ui.ThemeApp
import com.dr10.settings.ui.screens.regexRules.RegexRuleModel
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

class RegexRulesCellRender: DefaultListCellRenderer() {

    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val item = RegexRuleItem(value as RegexRuleModel)
        item.background = if(isSelected) ThemeApp.awtColors.complementaryColor
        else ThemeApp.awtColors.primaryColor
        return item
    }

}