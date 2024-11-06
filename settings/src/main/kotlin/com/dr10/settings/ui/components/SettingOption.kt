package com.dr10.settings.ui.components

import com.dr10.common.ui.ThemeApp
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * [JPanel] that represents a single setting option that will be using in the [SettingOptionListCellRender]
 *
 * @param optionName The name of the setting option
 */
class SettingOption(optionName: String): JPanel() {

    init {
        val optionNameLabel = JLabel(optionName).apply {
            font = ThemeApp.text.fontInterRegular(12f)
            foreground = ThemeApp.awtColors.textColor
        }

        add(optionNameLabel)
    }

}