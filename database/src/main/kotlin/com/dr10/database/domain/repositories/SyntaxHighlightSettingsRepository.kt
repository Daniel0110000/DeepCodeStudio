package com.dr10.database.domain.repositories

import com.dr10.common.models.SyntaxHighlightOptionModel
import kotlinx.coroutines.flow.Flow

interface SyntaxHighlightSettingsRepository {
    suspend fun createSyntaxHighlightConfig(model: SyntaxHighlightOptionModel)

    fun getAllSyntaxHighlightConfigs(): Flow<List<SyntaxHighlightOptionModel>>

    fun getSyntaxHighlightConfig(uuid: String): SyntaxHighlightOptionModel

    suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightOptionModel)

    suspend fun deleteSyntaxHighlightConfig(uuid: String)

    // Change the name
    suspend fun updateSyntaxHighlightConfigJsonPath(jsonPath: String, uuid: String)
}