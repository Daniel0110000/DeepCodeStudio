package com.dr10.settings.ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.settings.model.Option
import com.dr10.settings.ui.viewModels.SettingsViewModel
import java.awt.Color
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.GroupLayout
import javax.swing.JList
import javax.swing.JPanel

/**
 * [JPanel] to display the vertical list of settings options
 *
 * @param settingsViewModel The view model of the settings
 * @param onClickListener Callback function invoked when an option is clicked
 */
class VerticalSettingOptions(
    private val settingsViewModel: SettingsViewModel,
    private val onClickListener: (Option) -> Unit
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val verticalSettingOptionsLayout = GroupLayout(this)
        layout = verticalSettingOptionsLayout
        preferredSize = Dimension(220, Short.MAX_VALUE.toInt())
        background = ThemeApp.awtColors.secondaryColor

        val options: JList<Option> = JList<Option>().apply {
            setCellRenderer(SettingOptionListCellRender())
            setListData(settingsViewModel.state.value.options.toTypedArray())
            selectedIndex = 0
            background = Color.CYAN
        }

        options.addMouseListener(object: MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                onClickListener(options.selectedValue)
            }
        })

        verticalSettingOptionsLayout.setHorizontalGroup(
            verticalSettingOptionsLayout.createParallelGroup()
                .addComponent(options, 0, 0, Short.MAX_VALUE.toInt())
        )

        verticalSettingOptionsLayout.setVerticalGroup(
            verticalSettingOptionsLayout.createParallelGroup()
                .addComponent(options)
        )


    }
}