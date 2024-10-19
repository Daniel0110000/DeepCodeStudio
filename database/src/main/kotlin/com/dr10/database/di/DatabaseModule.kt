package com.dr10.database.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.deepCodeStudio.database.AppDatabase
import com.dr10.common.utilities.Constants
import com.dr10.common.utilities.DocumentsManager
import com.dr10.database.data.repositories.AutocompleteSettingsRepositoryImpl
import com.dr10.database.data.repositories.SyntaxAndSuggestionsRepositoryImpl
import com.dr10.database.data.repositories.SyntaxHighlightSettingsRepositoryImpl
import com.dr10.database.data.room.SyntaxAndSuggestionsDao
import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
import com.dr10.database.domain.repositories.SyntaxHighlightSettingsRepository
import org.koin.dsl.module
import java.io.File

/**
 * Define the [databaseModule] for dependency injection
 */
val databaseModule = module {
    single<AppDatabase> {
        val driver = JdbcSqliteDriver("${JdbcSqliteDriver.IN_MEMORY}/${DocumentsManager.databaseDirectory.absolutePath}/${Constants.DATABASE_NAME}")
        AppDatabase.Schema.create(driver)
        AppDatabase(driver)
    }

    single<com.dr10.database.data.room.AppDatabase> {
        val dbFilePath = File("${DocumentsManager.getUserHome()}/${Constants.DEFAULT_LOCAL_DIRECTORY_NAME}", Constants.DATABASE_NAME)
        Room.databaseBuilder<com.dr10.database.data.room.AppDatabase>(name = dbFilePath.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single<SyntaxAndSuggestionsDao> {
        val dbInstance = get<com.dr10.database.data.room.AppDatabase>()
        dbInstance.syntaxAndSuggestionsDao()
    }

    single<AutocompleteSettingsRepository> { AutocompleteSettingsRepositoryImpl(get()) }
    single<SyntaxHighlightSettingsRepository> { SyntaxHighlightSettingsRepositoryImpl(get()) }
    single<SyntaxAndSuggestionsRepository> { SyntaxAndSuggestionsRepositoryImpl(get()) }

}