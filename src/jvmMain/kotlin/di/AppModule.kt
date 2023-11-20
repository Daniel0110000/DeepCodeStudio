package di

import data.repository.SettingRepositoryImpl
import domain.repository.SettingRepository
import org.koin.dsl.module
import ui.viewModels.editor.EditorViewModel
import ui.viewModels.editor.TabsViewModel
import ui.viewModels.settings.AutocompleteSettingsViewModel
import ui.viewModels.settings.SettingsViewModel
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel
import ui.viewModels.splitPane.SplitPaneViewModel
import ui.viewModels.terminal.TerminalViewModel

/**
 * Define the app module for dependency injection
 */
val appModule = module {
    single<SettingRepository> { SettingRepositoryImpl() }

    single { SyntaxHighlightSettingsViewModel(get()) }
    single { AutocompleteSettingsViewModel(get()) }
    single { TabsViewModel() }
    single { EditorViewModel(get()) }
    single { SettingsViewModel() }
    single { TerminalViewModel() }
    single { SplitPaneViewModel() }
}