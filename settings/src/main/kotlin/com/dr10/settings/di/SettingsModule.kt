package com.dr10.settings.di

import com.dr10.settings.ui.viewModels.AutocompleteSettingsViewModel
import com.dr10.settings.ui.viewModels.SettingsViewModel
import com.dr10.settings.ui.viewModels.SyntaxHighlightSettingsViewModel
import org.koin.dsl.module

/**
 * Define the [settingsModule] for dependency injection
 */
val settingsModule = module {
    single { SettingsViewModel(get(), get()) }
    single { SyntaxHighlightSettingsViewModel(get()) }
    single { AutocompleteSettingsViewModel(get(), get()) }
}