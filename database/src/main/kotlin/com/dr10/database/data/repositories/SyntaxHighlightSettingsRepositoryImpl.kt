package com.dr10.database.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.deepCodeStudio.database.AppDatabase
import app.deepCodeStudio.database.SyntaxHighlightOptionsQueries
import com.dr10.common.models.SyntaxHighlightOptionModel
import com.dr10.common.utilities.CallHandler
import com.dr10.database.data.mappers.toModel
import com.dr10.database.domain.repositories.SyntaxHighlightSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SyntaxHighlightSettingsRepositoryImpl(
    appDatabase: AppDatabase
): SyntaxHighlightSettingsRepository {

    private val syntaxHighlightOptionsQueries: SyntaxHighlightOptionsQueries = appDatabase.syntaxHighlightOptionsQueries

    /**
     * Creates a new syntax highlight configuration by inserting it into the database
     *
     * @param model The [SyntaxHighlightOptionModel] containing the configurations details to be created
     */
    override suspend fun createSyntaxHighlightConfig(model: SyntaxHighlightOptionModel) {
        CallHandler.callHandler {
            syntaxHighlightOptionsQueries.insert(
                uuid = model.uuid,
                optionName = model.optionName,
                jsonPath = model.jsonPath,
                simpleColor = model.simpleColor,
                instructionColor = model.instructionColor,
                variableColor = model.variableColor,
                constantColor = model.constantColor,
                numberColor = model.numberColor,
                segmentColor = model.segmentColor,
                systemCallColor = model.systemCallColor,
                registerColor = model.registerColor,
                arithmeticInstructionColor = model.arithmeticInstructionColor,
                logicalInstructionColor = model.logicalInstructionColor,
                conditionColor = model.conditionColor,
                loopColor = model.loopColor,
                memoryManagementColor = model.memoryManagementColor,
                commentColor = model.commentColor,
                stringColor = model.stringColor,
                labelColor = model.labelColor,
            )
        }
    }

    /**
     * Retrieves all syntax highlight configurations from the database
     *
     * @return A [Flow] emitting a [List] of [SyntaxHighlightOptionModel] objects representing the configurations
     */
    override fun getAllSyntaxHighlightConfigs(): Flow<List<SyntaxHighlightOptionModel>> =
        syntaxHighlightOptionsQueries.selectAll().asFlow().map { it.executeAsList().map { entity -> entity.toModel() } }

    /**
     * Retrieves a syntax highlight configuration from the database based on the specified UUID
     *
     * @param uuid The UUID of the syntax highlight configuration to retrieve
     * @return The retrieved [SyntaxHighlightOptionModel] or an empty model if not found
     */
    override fun getSyntaxHighlightConfig(uuid: String): SyntaxHighlightOptionModel {
        val result = syntaxHighlightOptionsQueries.select(uuid).executeAsList().map { it.toModel() }
        return if (result.isNotEmpty()) result[0] else SyntaxHighlightOptionModel()
    }

    /**
     * Updates a syntax highlight configuration with the provided [model]
     *
     * @param model The [SyntaxHighlightOptionModel] containing the upload configuration
     */
    override suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightOptionModel) {
        CallHandler.callHandler {
            syntaxHighlightOptionsQueries.update(
                simpleColor = model.simpleColor,
                instructionColor = model.instructionColor,
                variableColor = model.variableColor,
                constantColor = model.constantColor,
                numberColor = model.numberColor,
                segmentColor = model.segmentColor,
                systemCallColor = model.systemCallColor,
                registerColor = model.registerColor,
                arithmeticInstructionColor = model.arithmeticInstructionColor,
                logicalInstructionColor = model.logicalInstructionColor,
                conditionColor = model.conditionColor,
                loopColor = model.loopColor,
                memoryManagementColor = model.memoryManagementColor,
                commentColor = model.commentColor,
                stringColor = model.stringColor,
                labelColor = model.labelColor,
                uuid = model.uuid
            )
        }
    }

    /**
     * Deletes a syntax highlight configuration specified by the [uuid]
     *
     * @param uuid The UUID of the syntax highlight configuration to delete
     */
    override suspend fun deleteSyntaxHighlightConfig(uuid: String) {
        CallHandler.callHandler { syntaxHighlightOptionsQueries.delete(uuid) }
    }

    /**
     * Updates the JSON path of a syntax highlight configuration specified by the [uuid]
     *
     * @param jsonPath The new JSON path to set for the syntax highlight configuration
     * @param uuid The UUID of the syntax highlight configuration to update
     */
    override suspend fun updateSyntaxHighlightConfigJsonPath(jsonPath: String, uuid: String) {
        CallHandler.callHandler { syntaxHighlightOptionsQueries.updateJsonPath(jsonPath, uuid) }
    }
}