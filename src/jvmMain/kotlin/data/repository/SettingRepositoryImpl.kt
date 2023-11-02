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
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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
                    it[optionName] = model.optionName
                    it[jsonPath] = model.jsonPath
                }
            }
        }
    }

    /**
     * Deletes an autocomplete option specified by the [model] parameter
     *
     * @param model The autocomplete option to delete
     */
    override suspend fun deleteAutocompleteOption(model: AutocompleteOptionModel) {
        CallHandler.callHandler {
            transaction {
                AutocompleteTable.deleteWhere {
                    (id eq model.id) and (optionName eq model.optionName) and (jsonPath eq model.jsonPath)
                }
            }
        }
    }

    /**
     * Updates the JSOn path pf an autocomplete option specified by the [model] parameter
     *
     * @param jsonPath The new JSON path to set for the autocomplete option
     * @param model The autocomplete option to delete
     */
    override suspend fun updateAutocompleteOptionJsonPath(jsonPath: String, model: AutocompleteOptionModel) {
        CallHandler.callHandler {
            transaction {
                AutocompleteTable.update({ (AutocompleteTable.id eq model.id) and (AutocompleteTable.optionName eq model.optionName) }){
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
            AutocompleteOptionModel(it[AutocompleteTable.id], it[AutocompleteTable.optionName], it[AutocompleteTable.jsonPath])
        }
    }

    /**
     * Check if a selected autocomplete option already exists in the database
     *
     * @param asmFilePath The parameter to check if the option already exists in the database
     * @return Returns 'true' if the option exists, and 'false' if it does not.
     */
    override fun existsAutocompleteOption(asmFilePath: String): Boolean = transaction {
         val result = HistorySelectedAutocompleteOptionsTable
            .select { HistorySelectedAutocompleteOptionsTable.asmFilePath eq asmFilePath }
            .map {
                SelectedAutocompleteOptionModel(
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
                    asmFilePath = it[HistorySelectedAutocompleteOptionsTable.asmFilePath],
                    optionName = it[HistorySelectedAutocompleteOptionsTable.optionName],
                    jsonPath = it[HistorySelectedAutocompleteOptionsTable.jsonPath]
                )
            }
        if (result.isNotEmpty()) result[0]
        else SelectedAutocompleteOptionModel(0, "", "", "")
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
                    it[optionName] = model.optionName
                    it[jsonPath] = model.jsonPath
                }
            }
        }
    }

    /**
     * Delete the selected autocomplete option specified by the [asmFilePath] parameter
     *
     * @param asmFilePath The ASM file path for deleting the selected option
     */
    override suspend fun deleteSelectedAutocompleteOption(asmFilePath: String) {
        CallHandler.callHandler {
            transaction {
                HistorySelectedAutocompleteOptionsTable.deleteWhere {
                    HistorySelectedAutocompleteOptionsTable.asmFilePath eq asmFilePath
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
                    it[optionName] = model.optionName
                    it[jsonPath] = model.jsonPath
                    it[keywordColor] = model.keywordColor
                    it[variableColor] = model.variableColor
                    it[numberColor] = model.numberColor
                    it[sectionColor] = model.sectionColor
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
                it[SyntaxHighlightTable.id],
                it[SyntaxHighlightTable.optionName],
                it[SyntaxHighlightTable.jsonPath],
                it[SyntaxHighlightTable.keywordColor],
                it[SyntaxHighlightTable.variableColor],
                it[SyntaxHighlightTable.numberColor],
                it[SyntaxHighlightTable.sectionColor],
                it[SyntaxHighlightTable.commentColor],
                it[SyntaxHighlightTable.stringColor],
                it[SyntaxHighlightTable.labelColor],
            )
        }
    }

    /**
     * Updates a syntax highlight configuration with the provided [model]
     *
     * @param model The [SyntaxHighlightConfigModel] containing the upload configuration
     */
    override suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightConfigModel) {
        CallHandler.callHandler {
            transaction {
                SyntaxHighlightTable.update({
                    SyntaxHighlightTable.jsonPath eq model.jsonPath
                }){
                    it[keywordColor] = model.keywordColor
                    it[variableColor] = model.variableColor
                    it[numberColor] = model.numberColor
                    it[sectionColor] = model.sectionColor
                    it[commentColor] = model.commentColor
                    it[stringColor] = model.stringColor
                    it[labelColor] = model.labelColor
                }
            }
        }
    }

    /**
     * Deletes a syntax highlight configuration by it8s JSON path
     *
     * @param jsonPath The JSON path of the configuration to delete
     */
    override suspend fun deleteSyntaxHighlightConfig(jsonPath: String) {
        CallHandler.callHandler {
            transaction {
                SyntaxHighlightTable.deleteWhere { SyntaxHighlightTable.jsonPath eq jsonPath }
            }
        }
    }


    /**
     * Updates the JSON path of a syntax highlight configuration for a specific option
     *
     * @param newJsonPath The new JSON path to update to
     * @param oldJsonPath The existing JSON path to identify the configuration
     * @param optionName The name of the option associated with the configuration
     */
    override suspend fun updateSyntaxHighlightConfigJsonPath(
        newJsonPath: String,
        oldJsonPath: String,
        optionName: String
    ) {
        CallHandler.callHandler {
            transaction {
                SyntaxHighlightTable.update({ (SyntaxHighlightTable.jsonPath eq oldJsonPath) and (SyntaxHighlightTable.optionName eq optionName) }){
                    it[jsonPath] = newJsonPath
                }
            }
        }
    }


}