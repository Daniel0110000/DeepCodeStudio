package com.dr10.editor.ui.tabs.components

import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

/**
 * Custom [DefaultListCellRenderer] for rendering autocomplete options in a [JList]
 */
class OptionsListCellRender: DefaultListCellRenderer() {

    override fun getListCellRendererComponent(
        list: JList<*>,
        value: Any,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val optionPanel = AutoCompleteOption((value as SelectedConfigHistoryModel).optionName)

        if (isSelected) optionPanel.background = ThemeApp.colors.buttonColor.toAWTColor()
        else optionPanel.background = ThemeApp.colors.secondColor.toAWTColor()

        return optionPanel
    }

}