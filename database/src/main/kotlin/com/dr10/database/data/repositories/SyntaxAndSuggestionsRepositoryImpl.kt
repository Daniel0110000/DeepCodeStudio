package com.dr10.database.data.repositories

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.utilities.CallHandler
import com.dr10.common.utilities.DocumentsManager
import com.dr10.common.utilities.JFlexProcessor
import com.dr10.common.utilities.JsonUtils
import com.dr10.database.data.mappers.toEntity
import com.dr10.database.data.mappers.toModel
import com.dr10.database.data.room.SyntaxAndSuggestionsDao
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SyntaxAndSuggestionsRepositoryImpl(
    private val syntaxAndSuggestionsDao: SyntaxAndSuggestionsDao
): SyntaxAndSuggestionsRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Adds a new configuration for syntax highlighting and suggestions
     * Process the provider model, generates and compiles syntax files, and inserts the configuration into the database
     *
     * @param model The model containing syntax and suggestion data to be added
     * @param isLoading A lambda function to indicate loading status, true when loading starts and false when it ends
     */
    override suspend fun addConfig(
        model: SyntaxAndSuggestionModel,
        isLoading: (Boolean) -> Unit
    ) {
        CallHandler.callHandler {
            isLoading(true)
            val syntaxModel = JsonUtils.jsonToSyntaxHighlightModel(jsonPath = model.jsonPath)
            JFlexProcessor.generateAndCompileSyntaxFiles(
                optionName = model.optionName,
                syntaxHighlightModel = syntaxModel
            ) { className ->
                scope.launch {
                    syntaxAndSuggestionsDao.insert(model.copy(className = className).toEntity())
                    isLoading(false)
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
            syntaxAndSuggestionsDao.delete(model.uniqueId)
        }
    }

    /**
     * Retrieves all configurations as a flow of SyntaxAndSuggestionModel list
     *
     * @return A flow emitting a list of all syntax and suggestion models
     */
    override suspend fun getConfigs(): Flow<List<SyntaxAndSuggestionModel>>  =
        syntaxAndSuggestionsDao.getAll().map { it.map { it.toModel() } }
}