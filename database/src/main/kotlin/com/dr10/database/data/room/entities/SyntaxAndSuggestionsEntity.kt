package com.dr10.database.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "syntax_and_suggestions")
data class SyntaxAndSuggestionsEntity(
    @PrimaryKey @ColumnInfo(name = "unique_id") val uniqueId: String,
    @ColumnInfo(name = "option_name") val optionName: String,
    @ColumnInfo(name = "class_name") val className: String,
    @ColumnInfo(name = "json_path") val jsonPath: String
)
