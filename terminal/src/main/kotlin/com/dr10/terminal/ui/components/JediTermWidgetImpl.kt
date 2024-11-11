package com.dr10.terminal.ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.settings.SettingsProvider
import javax.swing.JScrollBar

class JediTermWidgetImpl(settingsProvider: SettingsProvider): JediTermWidget(settingsProvider) {

    override fun createScrollBar(): JScrollBar {
        return JScrollBar().apply {
            setUI(CustomScrollBar(gTrackColor = ThemeApp.awtColors.secondaryColor))
        }
    }

}