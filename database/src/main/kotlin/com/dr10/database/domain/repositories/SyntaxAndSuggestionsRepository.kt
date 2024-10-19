package com.dr10.database.domain.repositories

import com.dr10.common.models.SyntaxAndSuggestionModel
import kotlinx.coroutines.flow.Flow

interface SyntaxAndSuggestionsRepository {
    suspend fun addConfig(model: SyntaxAndSuggestionModel, loading: (Boolean) -> Unit)

    suspend fun deleteConfig(model: SyntaxAndSuggestionModel)

    suspend fun getConfigs(): Flow<List<SyntaxAndSuggestionModel>>

}