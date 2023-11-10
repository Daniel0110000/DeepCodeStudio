package data.repository

import data.local.databaseConnection
import data.local.tables.AutocompleteTable
import data.local.tables.HistorySelectedAutocompleteOptionsTable
import data.local.tables.SyntaxHighlightTable
import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel
import domain.model.SyntaxHighlightConfigModel
import domain.repository.SettingRepository
import domain.util.CallHandler
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class SettingRepositoryImpl: SettingRepository {

    init {
        // Execute the database connection
        databaseConnection()
        // Creating the tables
        transaction { SchemaUtils.create(AutocompleteTable, HistorySelectedAutocompleteOptionsTable, SyntaxHighlightTable) }
    }

    /**
     * Inserts a new option for autocomplete into the database
     *
     * @param model The data to be inserted into the database
     */
    override suspend fun addAutocompleteOption(model: AutocompleteOptionModel) {
        CallHandler.callHandler {
            transaction {
                AutocompleteTable.insert {
                    it[uuid] = model.uuid
                    it[optionName] = model.optionName
                    it[jsonPath] = model.jsonPath
                }
            }
        }
    }

    /**
     * Deletes an autocomplete option with the specified [uuid] from the database
     *
     * @param uuid The UUID of the autocomplete option to delete
     */
    override suspend fun deleteAutocompleteOption(uuid: String) {
        CallHandler.callHandler {
            transaction {
                AutocompleteTable.deleteWhere { AutocompleteTable.uuid eq uuid }
            }
        }
    }

    /**
     * Updates the JSOn path of an autocomplete option specified by the [uuid]
     *
     * @param jsonPath The new JSON path to set for the autocomplete option
     * @param uuid The UUID of the autocomplete option to update
     */
    override suspend fun updateAutocompleteOptionJsonPath(jsonPath: String, uuid: String) {
        CallHandler.callHandler {
            transaction {
                AutocompleteTable.update({ AutocompleteTable.uuid eq uuid }){
                    it[AutocompleteTable.jsonPath] = jsonPath
                }
            }
        }
    }

    /**
     * Retrieves all autocomplete options from the database and returns them as a list of [AutocompleteOptionModel]
     *
     * @return A list of AutocompleteOptionModel containing all autocomplete options
     */
    override fun getAllAutocompleteOptions(): List<AutocompleteOptionModel> = transaction {
        AutocompleteTable.selectAll().map {
            AutocompleteOptionModel(
                it[AutocompleteTable.uuid],
                it[AutocompleteTable.optionName],
                it[AutocompleteTable.jsonPath]
            )
        }
    }

    /**
     * Check if a selected autocomplete option already exists in the database
     *
     * @param asmFilePath The parameter to check if the option already exists in the database
     * @return Returns 'true' if the option exists, and 'false' if it does not.
     */
    override fun existsSelectedAutocompleteOption(asmFilePath: String): Boolean = transaction {
         val result = HistorySelectedAutocompleteOptionsTable
            .select { HistorySelectedAutocompleteOptionsTable.asmFilePath eq asmFilePath }
            .map {
                SelectedAutocompleteOptionModel(
                    uuid = it[HistorySelectedAutocompleteOptionsTable.uuid],
                    asmFilePath = it[HistorySelectedAutocompleteOptionsTable.asmFilePath],
                    optionName = it[HistorySelectedAutocompleteOptionsTable.optionName],
                    jsonPath = it[HistorySelectedAutocompleteOptionsTable.jsonPath]
                )
            }
        result.isNotEmpty()
    }


    /**
     * Inserts a new selected autocomplete option entry into the history database
     *
     * @param model The data model representing the selected autocomplete option
     */
    override suspend fun addSelectedAutocompleteOption(model: SelectedAutocompleteOptionModel) {
        CallHandler.callHandler {
            transaction {
                HistorySelectedAutocompleteOptionsTable.insert{
                    it[uuid] = model.uuid
                    it[asmFilePath] = model.asmFilePath
                    it[optionName] = model.optionName
                    it[jsonPath] = model.jsonPath
                }
            }
        }
    }

    /**
     * Retrieves the selected autocomplete option for a specific ASM file
     *
     * @param asmFilePath The path of the ASM file for which the option was selected
     * @return The selected autocomplete option for the specified ASM file
     */
    override fun getSelectedAutocompleteOption(asmFilePath: String): SelectedAutocompleteOptionModel = transaction {
        val result = HistorySelectedAutocompleteOptionsTable
            .select { HistorySelectedAutocompleteOptionsTable.asmFilePath eq asmFilePath }
            .map {
                SelectedAutocompleteOptionModel(
                    uuid = it[HistorySelectedAutocompleteOptionsTable.uuid],
                    asmFilePath = it[HistorySelectedAutocompleteOptionsTable.asmFilePath],
                    optionName = it[HistorySelectedAutocompleteOptionsTable.optionName],
                    jsonPath = it[HistorySelectedAutocompleteOptionsTable.jsonPath]
                )
            }
        if (result.isNotEmpty()) result[0]
        else SelectedAutocompleteOptionModel("", "", "", "")
    }

    /**
     * Updates the selected autocomplete option for a specific ASM file
     *
     * @param model The model containing the updated information for the selected autocomplete option
     */
    override suspend fun updateSelectedAutocompleteOption(model: SelectedAutocompleteOptionModel) {
        CallHandler.callHandler {
            transaction {
                HistorySelectedAutocompleteOptionsTable.update({
                    HistorySelectedAutocompleteOptionsTable.asmFilePath eq model.asmFilePath
                }){
                    it[uuid] = model.uuid
                    it[optionName] = model.optionName
                    it[jsonPath] = model.jsonPath
                }
            }
        }
    }

    /**
     * Update the JSON path of a selected autocomplete option specified by the [uuid]
     *
     * @param jsonPath The new JSON path to set for the selected autocomplete option
     * @param uuid The UUID of the selected autocomplete option to update
     */
    override suspend fun updateSelectedAutocompleteOptionJsonPath(jsonPath: String, uuid: String) {
        CallHandler.callHandler {
            transaction {
                HistorySelectedAutocompleteOptionsTable.update(
                    { HistorySelectedAutocompleteOptionsTable.uuid eq uuid }
                ) {
                    it[HistorySelectedAutocompleteOptionsTable.jsonPath] = jsonPath
                }
            }
        }
    }

    /**
     * Deletes a selected autocomplete option specified by the [asmFilePath] and [uuid]
     *
     * @param asmFilePath The ASM file path for which selected option is associated
     * @param uuid The UUID of the selected autocomplete option to delete
     */
    override suspend fun deleteSelectedAutocompleteOption(asmFilePath: String, uuid: String) {
        CallHandler.callHandler {
            transaction {
                HistorySelectedAutocompleteOptionsTable.deleteWhere {
                    (HistorySelectedAutocompleteOptionsTable.asmFilePath eq asmFilePath) or
                    (HistorySelectedAutocompleteOptionsTable.uuid eq uuid)
                }
            }
        }
    }

    /**
     * Creates a new syntax highlight configuration by inserting it into the database
     *
     * @param model The [SyntaxHighlightConfigModel] containing the configurations details to be created
     */
    override suspend fun createSyntaxHighlightConfig(model: SyntaxHighlightConfigModel) {
        CallHandler.callHandler {
            transaction {
                SyntaxHighlightTable.insert{
                    it[uuid] = model.uuid
                    it[optionName] = model.optionName
                    it[jsonPath] = model.jsonPath
                    it[simpleColor] = model.simpleColor
                    it[instructionColor] = model.instructionColor
                    it[variableColor] = model.variableColor
                    it[constantColor] = model.constantColor
                    it[numberColor] = model.numberColor
                    it[segmentColor] = model.segmentColor
                    it[systemCallColor] = model.systemCallColor
                    it[registerColor] = model.registerColor
                    it[arithmeticInstructionColor] = model.arithmeticInstructionColor
                    it[logicalInstructionColor] = model.logicalInstructionColor
                    it[conditionColor] = model.conditionColor
                    it[loopColor] = model.loopColor
                    it[memoryManagementColor] = model.memoryManagementColor
                    it[commentColor] = model.commentColor
                    it[stringColor] = model.stringColor
                    it[labelColor] = model.labelColor
                }
            }
        }
    }

    /**
     * Retrieves all syntax highlight configurations from the database
     *
     * @return A list of [SyntaxHighlightConfigModel] objects representing the configurations
     */
    override fun getAllSyntaxHighlightConfigs(): List<SyntaxHighlightConfigModel> = transaction {
        SyntaxHighlightTable.selectAll().map {
            SyntaxHighlightConfigModel(
                it[SyntaxHighlightTable.uuid],
                it[SyntaxHighlightTable.optionName],
                it[SyntaxHighlightTable.jsonPath],
                it[SyntaxHighlightTable.simpleColor],
                it[SyntaxHighlightTable.instructionColor],
                it[SyntaxHighlightTable.variableColor],
                it[SyntaxHighlightTable.constantColor],
                it[SyntaxHighlightTable.numberColor],
                it[SyntaxHighlightTable.segmentColor],
                it[SyntaxHighlightTable.systemCallColor],
                it[SyntaxHighlightTable.registerColor],
                it[SyntaxHighlightTable.arithmeticInstructionColor],
                it[SyntaxHighlightTable.logicalInstructionColor],
                it[SyntaxHighlightTable.conditionColor],
                it[SyntaxHighlightTable.loopColor],
                it[SyntaxHighlightTable.memoryManagementColor],
                it[SyntaxHighlightTable.commentColor],
                it[SyntaxHighlightTable.stringColor],
                it[SyntaxHighlightTable.labelColor]
            )
        }
    }

    /**
     * Retrieves a syntax highlight configuration from the database based on the specified UUID
     *
     * @param uuid The UUID of the syntax highlight configuration to retrieve
     * @return The retrieved [SyntaxHighlightConfigModel] or an empty model if not found
     */
    override fun getSyntaxHighlightConfig(uuid: String): SyntaxHighlightConfigModel = transaction {
        val result = SyntaxHighlightTable
            .select { SyntaxHighlightTable.uuid eq uuid }
            .map {
                SyntaxHighlightConfigModel(
                    it[SyntaxHighlightTable.uuid],
                    it[SyntaxHighlightTable.optionName],
                    it[SyntaxHighlightTable.jsonPath],
                    it[SyntaxHighlightTable.simpleColor],
                    it[SyntaxHighlightTable.instructionColor],
                    it[SyntaxHighlightTable.variableColor],
                    it[SyntaxHighlightTable.constantColor],
                    it[SyntaxHighlightTable.numberColor],
                    it[SyntaxHighlightTable.segmentColor],
                    it[SyntaxHighlightTable.systemCallColor],
                    it[SyntaxHighlightTable.registerColor],
                    it[SyntaxHighlightTable.arithmeticInstructionColor],
                    it[SyntaxHighlightTable.logicalInstructionColor],
                    it[SyntaxHighlightTable.conditionColor],
                    it[SyntaxHighlightTable.loopColor],
                    it[SyntaxHighlightTable.memoryManagementColor],
                    it[SyntaxHighlightTable.commentColor],
                    it[SyntaxHighlightTable.stringColor],
                    it[SyntaxHighlightTable.labelColor]
                )
            }
        if(result.isNotEmpty()) result[0] else SyntaxHighlightConfigModel()
    }

    /**
     * Updates a syntax highlight configuration with the provided [model]
     *
     * @param model The [SyntaxHighlightConfigModel] containing the upload configuration
     */
    override suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightConfigModel) {
        CallHandler.callHandler {
            transaction {
                SyntaxHighlightTable.update({ SyntaxHighlightTable.uuid eq model.uuid }){
                    it[simpleColor] = model.simpleColor
                    it[instructionColor] = model.instructionColor
                    it[variableColor] = model.variableColor
                    it[constantColor] = model.constantColor
                    it[numberColor] = model.numberColor
                    it[segmentColor] = model.segmentColor
                    it[systemCallColor] = model.systemCallColor
                    it[registerColor] = model.registerColor
                    it[arithmeticInstructionColor] = model.arithmeticInstructionColor
                    it[logicalInstructionColor] = model.logicalInstructionColor
                    it[conditionColor] = model.conditionColor
                    it[loopColor] = model.loopColor
                    it[memoryManagementColor] = model.memoryManagementColor
                    it[commentColor] = model.commentColor
                    it[stringColor] = model.stringColor
                    it[labelColor] = model.labelColor
                }
            }
        }
    }

    /**
     * Deletes a syntax highlight configuration specified by the [uuid]
     *
     * @param uuid The UUID of the syntax highlight configuration to delete
     */
    override suspend fun deleteSyntaxHighlightConfig(uuid: String) {
        CallHandler.callHandler {
            transaction {
                SyntaxHighlightTable.deleteWhere { SyntaxHighlightTable.uuid eq uuid }
            }
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
            transaction {
                SyntaxHighlightTable.update({ SyntaxHighlightTable.uuid eq uuid }){
                    it[SyntaxHighlightTable.jsonPath] = jsonPath
                }
            }
        }
    }


}