package com.dr10.database.domain.repositories

import com.dr10.common.models.ColorSchemeModel
import kotlinx.coroutines.flow.Flow

interface ColorSchemeRepository {

    suspend fun insertColorScheme(model: ColorSchemeModel)

    suspend fun getAllColorSchemes(): Flow<List<ColorSchemeModel>>

    suspend fun updateColor(
        uniqueId: String,
        simpleColor: String? = null,
        commentColor: String? = null,
        reservedWordColor: String? = null,
        reservedWord2Color: String? = null,
        hexadecimalColor: String? = null,
        numberColor: String? = null,
        functionColor: String? = null,
        stringColor: String? = null,
        dataTypeColor: String? = null,
        variableColor: String? = null,
        operatorColor: String? = null,
        processorColor: String? = null
    )
}