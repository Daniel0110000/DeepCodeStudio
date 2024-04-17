package com.dr10.database.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.deepCodeStudio.database.AppDatabase
import com.dr10.database.data.repositories.AutocompleteSettingsRepositoryImpl
import com.dr10.database.data.repositories.SyntaxHighlightSettingsRepositoryImpl
import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import com.dr10.database.domain.repositories.SyntaxHighlightSettingsRepository
import org.koin.dsl.module

/**
 * Define the [databaseModule] for dependency injection
 */
val databaseModule = module {
    single<AppDatabase> {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + "Settings.db")
        AppDatabase.Schema.create(driver)
        AppDatabase(driver)
    }

    single<AutocompleteSettingsRepository> { AutocompleteSettingsRepositoryImpl(get()) }
    single<SyntaxHighlightSettingsRepository> { SyntaxHighlightSettingsRepositoryImpl(get()) }

}