package com.dr10.database.domain.repositories

import com.dr10.common.models.SelectedConfigHistoryModel

interface EditorRepository {
    suspend fun getAllConfigs(): List<SelectedConfigHistoryModel>

    suspend fun getSelectedConfig(asmFilePath: String): SelectedConfigHistoryModel?

    suspend fun insertSelectedConfig(uniqueId: String, asmFilePath: String)

    suspend fun updateSelectedConfig(uniqueId: String, asmFilePath: String)

    suspend fun deleteSelectedConfig(asmFilePath: String = "")

}