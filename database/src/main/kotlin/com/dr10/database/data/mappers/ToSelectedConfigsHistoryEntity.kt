package com.dr10.database.data.mappers

import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.database.data.room.SelectedConfigsHistoryEntity

/**
 * Converts [SelectedConfigHistoryModel] to [SelectedConfigsHistoryEntity]
 *
 * @return The converted [SelectedConfigsHistoryEntity]
 */
fun SelectedConfigHistoryModel.toEntity(): SelectedConfigsHistoryEntity = SelectedConfigsHistoryEntity(
    uniqueId = uniqueId,
    optionName = optionName,
    className = className,
    asmFilePath = asmFilePath
)