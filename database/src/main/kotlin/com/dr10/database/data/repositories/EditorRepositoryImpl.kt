package com.dr10.database.data.repositories

import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.database.data.mappers.toHistoryModel
import com.dr10.database.data.room.Queries
import com.dr10.database.data.room.entities.SelectedConfigHistoryEntity
import com.dr10.database.data.room.relations.ColorSchemeRelation
import com.dr10.database.domain.repositories.EditorRepository

class EditorRepositoryImpl(
    private val queries: Queries
): EditorRepository {

    override suspend fun getAllConfigs(): List<SelectedConfigHistoryModel> =
        queries.getAllColorSchemesAsList().map(ColorSchemeRelation::toHistoryModel)

    override suspend fun getSelectedConfig(asmFilePath: String): SelectedConfigHistoryModel? =
        queries.getSelectedConfig(asmFilePath)?.let {
            val syntaxEntity = it.syntaxAndSuggestionsEntity
            val colorEntity = it.colorSchemesEntity
            SelectedConfigHistoryModel(
                uniqueId = syntaxEntity.uniqueId,
                optionName = syntaxEntity.optionName,
                className = syntaxEntity.className,
                jsonPath = syntaxEntity.jsonPath,
                simpleColor = colorEntity.simpleColor,
                commentColor = colorEntity.commentColor,
                reservedWordColor = colorEntity.reservedWordColor,
                reservedWord2Color = colorEntity.reservedWord2Color,
                hexadecimalColor = colorEntity.hexadecimalColor,
                numberColor = colorEntity.numberColor,
                functionColor = colorEntity.functionColor,
                stringColor = colorEntity.stringColor,
                dataTypeColor = colorEntity.dataTypeColor,
                variableColor = colorEntity.variableColor,
                operatorColor = colorEntity.operatorColor,
                processorColor = colorEntity.processorColor
            )
        }

    override suspend fun insertSelectedConfig(uniqueId: String, asmFilePath: String) {
        queries.insertSelectedConfigHistory(
            SelectedConfigHistoryEntity(
                uniqueId = uniqueId,
                asmFilePath = asmFilePath
            )
        )
    }

    override suspend fun updateSelectedConfig(uniqueId: String, asmFilePath: String) {
        queries.updateSelectedConfig(
            uniqueId = uniqueId,
            asmFilePath = asmFilePath
        )
    }

    override suspend fun deleteSelectedConfig(asmFilePath: String) {
        queries.deleteSelectedConfig(asmFilePath)
    }
}