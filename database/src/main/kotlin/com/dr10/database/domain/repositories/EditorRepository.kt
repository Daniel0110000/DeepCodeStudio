package com.dr10.database.domain.repositories

import com.dr10.common.models.SelectedConfigHistoryModel

interface EditorRepository {
    suspend fun getSelectedConfig(asmFile: String): SelectedConfigHistoryModel?

    suspend fun insertSelectedConfig(model: SelectedConfigHistoryModel)

    suspend fun updateSelectedConfig(model: SelectedConfigHistoryModel)

    suspend fun deleteSelectedConfig(uniqueId: String = "", asmFilePath: String = "")

}