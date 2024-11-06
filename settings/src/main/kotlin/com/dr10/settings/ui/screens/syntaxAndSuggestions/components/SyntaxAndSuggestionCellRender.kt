package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.ui.ThemeApp
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

/**
 * [DefaultListCellRenderer] for a [JList] that displays syntax and suggestion configs
 */
class SyntaxAndSuggestionCellRender: DefaultListCellRenderer() {

    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val item = SyntaxAndSuggestionItem(value as SyntaxAndSuggestionModel)
        item.background = if(isSelected) ThemeApp.awtColors.complementaryColor
        else ThemeApp.awtColors.primaryColor
        return item
    }

}