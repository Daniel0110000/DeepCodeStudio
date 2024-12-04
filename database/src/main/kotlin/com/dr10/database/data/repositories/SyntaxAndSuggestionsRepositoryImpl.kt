package com.dr10.database.data.repositories

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.utilities.CallHandler
import com.dr10.common.utilities.DocumentsManager
import com.dr10.common.utilities.JFlexProcessor
import com.dr10.common.utilities.JsonUtils
import com.dr10.database.data.mappers.toEntity
import com.dr10.database.data.mappers.toModel
import com.dr10.database.data.room.Queries
import com.dr10.database.data.room.entities.SyntaxAndSuggestionsEntity
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SyntaxAndSuggestionsRepositoryImpl(
    private val queries: Queries
): SyntaxAndSuggestionsRepository {

    /**
     * Adds a new configuration for syntax highlighting and suggestions
     * Process the provider model, generates and compiles syntax files, and inserts the configuration into the database
     *
     * @param model The model containing syntax and suggestion data to be added
     * @param loading A callback function to indicate loading status, true when loading starts and false when it ends
     */
    override suspend fun addConfig(
        model: SyntaxAndSuggestionModel,
        loading: suspend (Boolean) -> Unit
    ) {
        CallHandler.callHandler {
            loading(true)
            val syntaxModel = JsonUtils.jsonToSyntaxHighlightModel(jsonPath = model.jsonPath)
            JFlexProcessor.generateAndCompileSyntaxFiles(
                optionNameBase = model.optionName,
                syntaxHighlightModel = syntaxModel
            ) { className ->
                CoroutineScope(Dispatchers.IO).launch {
                    queries.insertSyntaxAndSuggestion(
                        SyntaxAndSuggestionsEntity(
                            uniqueId = model.uniqueId,
                            optionName = model.optionName,
                            className = className,
                            jsonPath = model.jsonPath
                        )
                    )
                    loading(false)
                }
            }

        }
    }

    /**
     * Deletes configuration for syntax highlighting and suggestions and deletes generated files
     *
     * @param model The model containing the configuration to be deleted
     */
    override suspend fun deleteConfig(model: SyntaxAndSuggestionModel) {
        CallHandler.callHandler {
            DocumentsManager.deleteGeneratedFiles(model.className)
            queries.deleteSyntaxAndSuggestion(model.toEntity())
        }
    }

    /**
     * Retrieves all configurations as a flow of SyntaxAndSuggestionModel list
     *
     * @return A flow emitting a list of all syntax and suggestion models
     */
    override suspend fun getConfigs(): Flow<List<SyntaxAndSuggestionModel>> =
        queries.getAllSyntaxAndSuggestions().map { entities -> entities.map(SyntaxAndSuggestionsEntity::toModel) }

    /**
     * Retrieves all configurations as [List<SyntaxAndSuggestionModel>]
     *
     * @return A [List<SyntaxAndSuggestionModel>] with all syntax and suggestion models
     */
    override suspend fun getConfigsAsList(): List<SyntaxAndSuggestionModel> =
        queries.getAllSyntaxAndSuggestionsAsList().map(SyntaxAndSuggestionsEntity::toModel)

}