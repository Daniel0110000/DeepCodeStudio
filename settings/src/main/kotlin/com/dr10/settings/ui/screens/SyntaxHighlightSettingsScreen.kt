package com.dr10.settings.ui.screens

import com.dr10.common.ui.ThemeApp
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel

class SyntaxHighlightSettingsScreen: JPanel() {

    init { onCreate() }

    private fun onCreate() {
        background = ThemeApp.awtColors.primaryColor
        add(JLabel("Color Scheme Settings"), BorderLayout.CENTER)
    }

}