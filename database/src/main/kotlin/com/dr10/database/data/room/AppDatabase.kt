package com.dr10.database.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dr10.database.data.room.entities.ColorSchemesEntity
import com.dr10.database.data.room.entities.SelectedConfigHistoryEntity
import com.dr10.database.data.room.entities.SyntaxAndSuggestionsEntity

@Database(
    entities = [
        SyntaxAndSuggestionsEntity::class,
        ColorSchemesEntity::class,
        SelectedConfigHistoryEntity::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getQueries(): Queries
}