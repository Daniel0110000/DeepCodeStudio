package com.dr10.database.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "selected_configs_history")
data class SelectedConfigHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "unique_id") val uniqueId: String,
    @ColumnInfo(name = "asm_file_path") val asmFilePath: String
)
