package com.dr10.settings.ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.settings.domain.model.Option
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

/**
 * [DefaultListCellRenderer] for a [JList] that displays setting option
 */
class SettingOptionListCellRender: DefaultListCellRenderer() {

    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val optionPanel = SettingOption((value as Option).title)
        optionPanel.background = if (isSelected) ThemeApp.awtColors.complementaryColor
        else ThemeApp.awtColors.secondaryColor
        return optionPanel
    }

}