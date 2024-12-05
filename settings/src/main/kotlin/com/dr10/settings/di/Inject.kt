package com.dr10.settings.di

import com.dr10.settings.ui.viewModels.ColorSchemesViewModel
import com.dr10.settings.ui.viewModels.SettingsViewModel
import com.dr10.settings.ui.viewModels.SyntaxAndSuggestionsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * A [KoinComponent] class for provided the dependency inject to Settings module
 */
class Inject: KoinComponent {
    // Injecting the [SyntaxAndSuggestionsViewModel]
    val syntaxAndSuggestionsViewModel: SyntaxAndSuggestionsViewModel by inject()
    // Injecting the [SettingsViewModel]
    val settingsViewModel: SettingsViewModel by inject()

    val colorSchemesViewModel: ColorSchemesViewModel by inject()

}