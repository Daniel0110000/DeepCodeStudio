package com.dr10.database.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_configs_history")
data class SelectedConfigsHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uniqueId: String,
    val optionName: String,
    val className: String,
    val asmFilePath: String
)
