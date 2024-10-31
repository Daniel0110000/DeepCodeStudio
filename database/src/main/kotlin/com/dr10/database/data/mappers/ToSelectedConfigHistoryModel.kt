package com.dr10.database.data.mappers

import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.database.data.room.SelectedConfigsHistoryEntity

/**
 * Converts [SelectedConfigsHistoryEntity] to [SelectedConfigHistoryModel]
 *
 * @return The converted [SelectedConfigHistoryModel]
 */
fun SelectedConfigsHistoryEntity.toModel(): SelectedConfigHistoryModel = SelectedConfigHistoryModel(
    uniqueId = uniqueId,
    optionName = optionName,
    className = className,
    asmFilePath = asmFilePath,
    jsonPath = jsonPath
)