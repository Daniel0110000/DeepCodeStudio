package com.dr10.settings.di

import com.dr10.settings.ui.viewModels.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * A [KoinComponent] class for provided the dependency inject to Settings module
 */
class Inject: KoinComponent {
    val syntaxAndSuggestionsViewModel: SyntaxAndSuggestionsViewModel by inject()
    val settingsViewModel: SettingsViewModel by inject()
    val colorSchemesViewModel: ColorSchemesViewModel by inject()
    val regexRulesViewModel: RegexRulesViewModel by inject()
    val settingsNotificationsViewModel: SettingsNotificationsViewModel by inject()
}