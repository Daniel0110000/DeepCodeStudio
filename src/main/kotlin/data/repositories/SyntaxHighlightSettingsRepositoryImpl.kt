package data.repositories

import app.cash.sqldelight.db.SqlDriver
import data.mapper.toSyntaxHighlightConfigModel
import dev.daniel.database.AppDatabase
import dev.daniel.database.SyntaxHighlightTableQueries
import domain.model.SyntaxHighlightConfigModel
import domain.repositories.SyntaxHighlightSettingsRepository
import domain.utilies.CallHandler

class SyntaxHighlightSettingsRepositoryImpl(driver: SqlDriver) : SyntaxHighlightSettingsRepository {

    private val appDatabase: AppDatabase = AppDatabase(driver)
    private val syntaxHighlightTableQueries: SyntaxHighlightTableQueries = appDatabase.syntaxHighlightTableQueries

    init { AppDatabase.Schema.create(driver) }

    /**
     * Creates a new syntax highlight configuration by inserting it into the database
     *
     * @param model The [SyntaxHighlightConfigModel] containing the configurations details to be created
     */
    override suspend fun createSyntaxHighlightConfig(model: SyntaxHighlightConfigModel) {
        CallHandler.callHandler {
            syntaxHighlightTableQueries.insert(
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
     * @return A list of [SyntaxHighlightConfigModel] objects representing the configurations
     */
    override fun getAllSyntaxHighlightConfigs(): List<SyntaxHighlightConfigModel> =
        syntaxHighlightTableQueries.selectAll().executeAsList().map { it.toSyntaxHighlightConfigModel() }

    /**
     * Retrieves a syntax highlight configuration from the database based on the specified UUID
     *
     * @param uuid The UUID of the syntax highlight configuration to retrieve
     * @return The retrieved [SyntaxHighlightConfigModel] or an empty model if not found
     */
    override fun getSyntaxHighlightConfig(uuid: String): SyntaxHighlightConfigModel{
        val result = syntaxHighlightTableQueries.select(uuid).executeAsList().map { it.toSyntaxHighlightConfigModel() }
        return if (result.isNotEmpty()) result[0] else SyntaxHighlightConfigModel()
    }

    /**
     * Updates a syntax highlight configuration with the provided [model]
     *
     * @param model The [SyntaxHighlightConfigModel] containing the upload configuration
     */
    override suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightConfigModel) {
        CallHandler.callHandler {
            syntaxHighlightTableQueries.update(
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
        CallHandler.callHandler {
            syntaxHighlightTableQueries.delete(uuid)
        }
    }


    /**
     * Updates the JSON path of a syntax highlight configuration specified by the [uuid]
     *
     * @param jsonPath The new JSON path to set for the syntax highlight configuration
     * @param uuid The UUID of the syntax highlight configuration to update
     */
    override suspend fun updateSyntaxHighlightConfigJsonPath(
        jsonPath: String,
        uuid: String,
    ) {
        CallHandler.callHandler {
            syntaxHighlightTableQueries.updateJsonPath(jsonPath, uuid)
        }
    }
}