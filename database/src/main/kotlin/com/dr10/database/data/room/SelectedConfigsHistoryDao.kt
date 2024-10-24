package com.dr10.database.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SelectedConfigsHistoryDao {

    @Upsert
    suspend fun insert(selectedConfigsHistory: SelectedConfigsHistoryEntity)

    @Query("SELECT * FROM selected_configs_history WHERE asmFilePath = :asmFilePath LIMIT 1")
    suspend fun getSelectedConfig(asmFilePath: String): SelectedConfigsHistoryEntity?

    @Query("UPDATE selected_configs_history SET uniqueId = :uniqueId, optionName = :optionName, className = :className WHERE asmFilePath = :asmFilePath")
    suspend fun update(
        uniqueId: String,
        optionName: String,
        className: String,
        asmFilePath: String
    )

    @Query("DELETE FROM selected_configs_history WHERE uniqueId = :uniqueId OR asmFilePath = :asmFilePath")
    suspend fun delete(uniqueId: String = "", asmFilePath: String = "")
}