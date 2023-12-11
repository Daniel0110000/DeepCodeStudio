package di

import data.repository.SettingRepositoryImpl
import domain.repository.SettingRepository
import org.koin.dsl.module
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.editor.EditorViewModel
import ui.viewModels.editor.TabsViewModel
import ui.viewModels.settings.AutocompleteSettingsViewModel
import ui.viewModels.settings.SettingsViewModel
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel
import ui.viewModels.splitPane.SplitPaneViewModel

/**
 * Define the app module for dependency injection
 */
val appModule = module {
    single<SettingRepository> { SettingRepositoryImpl() }

    single { SyntaxHighlightSettingsViewModel(get()) }
    single { AutocompleteSettingsViewModel(get()) }
    single { CodeEditorViewModel() }
    single { TabsViewModel() }
    single { EditorViewModel(get()) }
    single { SettingsViewModel() }
    single { SplitPaneViewModel() }
}