package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import data.repositories.AutocompleteSettingsRepositoryImpl
import data.repositories.SyntaxHighlightSettingsRepositoryImpl
import domain.repositories.AutocompleteSettingsRepository
import domain.repositories.SyntaxHighlightSettingsRepository
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

    single<SqlDriver> { JdbcSqliteDriver("jdbc:sqlite:Settings.db") }

    single<AutocompleteSettingsRepository> { AutocompleteSettingsRepositoryImpl(get()) }
    single<SyntaxHighlightSettingsRepository> { SyntaxHighlightSettingsRepositoryImpl(get()) }

    single { SyntaxHighlightSettingsViewModel(get()) }
    single { AutocompleteSettingsViewModel(get(), get()) }
    single { CodeEditorViewModel() }
    single { TabsViewModel() }
    single { EditorViewModel(get(), get()) }
    single { SettingsViewModel(get(), get()) }
    single { SplitPaneViewModel() }
}