package com.dr10.database.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("regex_rules")
data class RegexRulesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "unique_id") val uniqueId: String,
    @ColumnInfo(name = "regex_name") val regexName: String,
    @ColumnInfo(name = "regex_pattern") val regexPattern: String,
)
