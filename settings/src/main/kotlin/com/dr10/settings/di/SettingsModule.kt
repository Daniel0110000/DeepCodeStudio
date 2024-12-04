package com.dr10.settings.di

import com.dr10.settings.ui.viewModels.ColorSchemesViewModel
import com.dr10.settings.ui.viewModels.SettingsViewModel
import com.dr10.settings.ui.viewModels.SyntaxAndSuggestionsViewModel
import org.koin.dsl.module

/**
 * Define the [settingsModule] for dependency injection
 */
val settingsModule = module {
    single { SettingsViewModel() }
    single { SyntaxAndSuggestionsViewModel(get(), get(), get()) }
    single { SettingsViewModel() }
    single { ColorSchemesViewModel(get()) }
}