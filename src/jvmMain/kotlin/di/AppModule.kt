package di

import data.repository.SettingRepositoryImpl
import domain.repository.SettingRepository
import org.koin.dsl.module
import ui.viewModels.settings.AutocompleteSettingsViewModel
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel

/**
 * Define the app module for dependency injection
 */
val appModule = module {
    single<SettingRepository> { SettingRepositoryImpl() }

    single { SyntaxHighlightSettingsViewModel(get()) }
    single { AutocompleteSettingsViewModel(get()) }
}