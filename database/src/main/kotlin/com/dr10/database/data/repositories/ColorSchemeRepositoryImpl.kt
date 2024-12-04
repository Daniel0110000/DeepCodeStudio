package com.dr10.database.data.repositories

import com.dr10.common.models.ColorSchemeModel
import com.dr10.database.data.mappers.toEntity
import com.dr10.database.data.mappers.toModel
import com.dr10.database.data.room.Queries
import com.dr10.database.data.room.relations.ColorSchemeRelation
import com.dr10.database.domain.repositories.ColorSchemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ColorSchemeRepositoryImpl(
    private val queries: Queries
): ColorSchemeRepository {

    override suspend fun insertColorScheme(model: ColorSchemeModel) {
        queries.insertColorScheme(model.toEntity())
    }

    override suspend fun getAllColorSchemes(): Flow<List<ColorSchemeModel>> =
        queries.getAllColorSchemes().map { entities -> entities.map(ColorSchemeRelation::toModel) }


    override suspend fun updateColor(
        uniqueId: String,
        simpleColor: String?,
        commentColor: String?,
        reservedWordColor: String?,
        reservedWord2Color: String?,
        hexadecimalColor: String?,
        numberColor: String?,
        functionColor: String?,
        stringColor: String?,
        dataTypeColor: String?,
        variableColor: String?,
        operatorColor: String?,
        processorColor: String?
    ) {
        queries.updateColor(
            uniqueId, simpleColor, commentColor, reservedWordColor, reservedWord2Color,
            hexadecimalColor, numberColor, functionColor, stringColor, dataTypeColor,
            variableColor, operatorColor, processorColor
        )
    }
}