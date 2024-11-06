package com.dr10.database.data.repositories

import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.database.data.mappers.toEntity
import com.dr10.database.data.mappers.toModel
import com.dr10.database.data.room.SelectedConfigsHistoryDao
import com.dr10.database.domain.repositories.EditorRepository

class EditorRepositoryImpl(
    private val selectedConfigsHistoryDao: SelectedConfigsHistoryDao
): EditorRepository {

    /**
     * Gets the selected config associated with the [asmFile] param
     *
     * @param asmFile The assembly file path to get its configuration.
     * @return The [SelectedConfigHistoryModel] associated with the [asmFile]
     */
    override suspend fun getSelectedConfig(asmFile: String): SelectedConfigHistoryModel? =
        selectedConfigsHistoryDao.getSelectedConfig(asmFile)?.toModel()

    /**
     * Inserts a new assembly file configuration
     *
     * @param model The model containing the necessary data to be added
     */
    override suspend fun insertSelectedConfig(model: SelectedConfigHistoryModel) {
        selectedConfigsHistoryDao.insert(model.toEntity())
    }

    /**
     * Updates the selected config using the asmFilePath as reference
     *
     * @param model The model containing the necessary data to update the configuration
     */
    override suspend fun updateSelectedConfig(model: SelectedConfigHistoryModel) {
        selectedConfigsHistoryDao.update(
            uniqueId = model.uniqueId,
            optionName = model.optionName,
            className = model.className,
            asmFilePath = model.asmFilePath,
            jsonPath = model.jsonPath
        )
    }

    /**
     * Deletes the selected config using the [uniqueId] or [asmFilePath]
     *
     * @param uniqueId The unique id of the selected config
     * @param asmFilePath The assembly file path of the selected config
     */
    override suspend fun deleteSelectedConfig(uniqueId: String, asmFilePath: String) {
        selectedConfigsHistoryDao.delete(uniqueId, asmFilePath)
    }

}