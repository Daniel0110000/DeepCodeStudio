package data.repositories

import data.local.databaseConnection
import data.local.tables.AutocompleteTable
import data.local.tables.SelectedAutocompleteOptionHistoryTable
import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel
import domain.repositories.AutocompleteSettingsRepository
import domain.utilies.CallHandler
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class AutocompleteSettingsRepositoryImpl: AutocompleteSettingsRepository {

    init {
        // Execute the database connection
        databaseConnection()
        // Creating the tables
        transaction { SchemaUtils.create(AutocompleteTable, SelectedAutocompleteOptionHistoryTable) }
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
        val result = SelectedAutocompleteOptionHistoryTable
            .select { SelectedAutocompleteOptionHistoryTable.asmFilePath eq asmFilePath }
            .map {
                SelectedAutocompleteOptionModel(
                    uuid = it[SelectedAutocompleteOptionHistoryTable.uuid],
                    asmFilePath = it[SelectedAutocompleteOptionHistoryTable.asmFilePath],
                    optionName = it[SelectedAutocompleteOptionHistoryTable.optionName],
                    jsonPath = it[SelectedAutocompleteOptionHistoryTable.jsonPath]
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
                SelectedAutocompleteOptionHistoryTable.insert{
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
        val result = SelectedAutocompleteOptionHistoryTable
            .select { SelectedAutocompleteOptionHistoryTable.asmFilePath eq asmFilePath }
            .map {
                SelectedAutocompleteOptionModel(
                    uuid = it[SelectedAutocompleteOptionHistoryTable.uuid],
                    asmFilePath = it[SelectedAutocompleteOptionHistoryTable.asmFilePath],
                    optionName = it[SelectedAutocompleteOptionHistoryTable.optionName],
                    jsonPath = it[SelectedAutocompleteOptionHistoryTable.jsonPath]
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
                SelectedAutocompleteOptionHistoryTable.update({
                    SelectedAutocompleteOptionHistoryTable.asmFilePath eq model.asmFilePath
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
                SelectedAutocompleteOptionHistoryTable.update(
                    { SelectedAutocompleteOptionHistoryTable.uuid eq uuid }
                ) {
                    it[SelectedAutocompleteOptionHistoryTable.jsonPath] = jsonPath
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
                SelectedAutocompleteOptionHistoryTable.deleteWhere {
                    (SelectedAutocompleteOptionHistoryTable.asmFilePath eq asmFilePath) or
                            (SelectedAutocompleteOptionHistoryTable.uuid eq uuid)
                }
            }
        }
    }

}