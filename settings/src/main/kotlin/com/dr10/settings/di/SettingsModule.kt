package com.dr10.settings.di

import com.dr10.settings.ui.viewModels.*
import org.koin.dsl.module

/**
 * Define the [settingsModule] for dependency injection
 */
val settingsModule = module {
    single { SettingsViewModel() }
    single { SyntaxAndSuggestionsViewModel(get(), get()) }
    single { SettingsViewModel() }
    single { ColorSchemesViewModel(get()) }
    single { RegexRulesViewModel(get(), get()) }
    single { SettingsNotificationsViewModel() }
}