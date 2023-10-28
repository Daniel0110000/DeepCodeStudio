package data.repository

import data.local.databaseConnection
import data.local.tables.AutocompleteTable
import data.local.tables.HistorySelectedAutocompleteOptionsTable
import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel
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
        // Creating the table [AutocompleteTable]
        transaction { SchemaUtils.create(AutocompleteTable, HistorySelectedAutocompleteOptionsTable) }
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

}