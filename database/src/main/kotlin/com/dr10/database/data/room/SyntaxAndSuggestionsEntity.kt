package com.dr10.database.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "syntax_and_suggestions")
data class SyntaxAndSuggestionsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uniqueId: String,
    val optionName: String,
    val className: String,
    val jsonPath: String
)
