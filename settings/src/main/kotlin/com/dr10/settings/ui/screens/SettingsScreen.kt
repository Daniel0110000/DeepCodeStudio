package com.dr10.settings.ui.screens

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomSplitPaneDivider
import com.dr10.settings.di.Inject
import com.dr10.settings.ui.components.VerticalSettingOptions
import com.dr10.settings.ui.screens.colorScheme.ColorSchemeSettingsScreen
import com.dr10.settings.ui.screens.syntaxAndSuggestions.SyntaxAndSuggestionsScreen
import com.dr10.settings.ui.viewModels.SettingsViewModel
import java.awt.CardLayout
import java.awt.Dimension
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.SwingConstants

/**
 * [JPanel] that contains all the settings screens
 */
class SettingsScreen: JPanel() {

    private val viewModel: SettingsViewModel = Inject().settingsViewModel

    init { onCreate() }

    private fun onCreate() {
        val settingsScreenLayout = GroupLayout(this)
        layout = settingsScreenLayout
        background = ThemeApp.awtColors.primaryColor

        val screensContainer = CardLayout()
        val screensPanel = JPanel(screensContainer).apply {
            add(SyntaxAndSuggestionsScreen(), Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS.name)
            add(ColorSchemeSettingsScreen(), Screens.COLOR_SCHEME_SETTINGS.name)
        }

        val verticalSettingOptions = VerticalSettingOptions(viewModel) { option ->
            screensContainer.show(screensPanel, option.screen.name)
        }

        val splitPane = JSplitPane(
            SwingConstants.VERTICAL,
            verticalSettingOptions,
            screensPanel
        ).apply {
            setUI(CustomSplitPaneDivider())
            isContinuousLayout = true
            leftComponent.minimumSize = Dimension(220, 0)
        }

        settingsScreenLayout.setHorizontalGroup(
            settingsScreenLayout.createParallelGroup()
                .addComponent(splitPane)
        )

        settingsScreenLayout.setVerticalGroup(
            settingsScreenLayout.createSequentialGroup()
                .addComponent(splitPane)
        )

    }
}