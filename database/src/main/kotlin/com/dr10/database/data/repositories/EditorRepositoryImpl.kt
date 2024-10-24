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
            asmFilePath = model.asmFilePath
        )
    }

}