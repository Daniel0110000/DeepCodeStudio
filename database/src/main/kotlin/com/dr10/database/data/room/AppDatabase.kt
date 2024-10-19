package com.dr10.database.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SyntaxAndSuggestionsEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun syntaxAndSuggestionsDao(): SyntaxAndSuggestionsDao
}