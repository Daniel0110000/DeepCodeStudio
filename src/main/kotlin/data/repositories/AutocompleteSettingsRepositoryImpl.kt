package data.repositories

import app.cash.sqldelight.db.SqlDriver
import dev.daniel.database.AppDatabase
import dev.daniel.database.AutocompleteTableQueries
import dev.daniel.database.SelectedAutocompleteOptionHistoryTableQueries
import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel
import domain.repositories.AutocompleteSettingsRepository
import domain.utilies.CallHandler

class AutocompleteSettingsRepositoryImpl(driver: SqlDriver): AutocompleteSettingsRepository {

    private val appDatabase: AppDatabase = AppDatabase(driver)
    private val autocompleteTableQueries: AutocompleteTableQueries = appDatabase.autocompleteTableQueries
    private val selectedAutocompleteTableQueries: SelectedAutocompleteOptionHistoryTableQueries = appDatabase.selectedAutocompleteOptionHistoryTableQueries

    init { AppDatabase.Schema.create(driver) }

    /**
     * Inserts a new option for autocomplete into the database
     *
     * @param model The data to be inserted into the database
     */
    override suspend fun addAutocompleteOption(model: AutocompleteOptionModel) {
        CallHandler.callHandler {
            autocompleteTableQueries.insert(
                uuid = model.uuid,
                optionName = model.optionName,
                jsonPath = model.jsonPath
            )
        }
    }

    /**
     * Deletes an autocomplete option with the specified [uuid] from the database
     *
     * @param uuid The UUID of the autocomplete option to delete
     */
    override suspend fun deleteAutocompleteOption(uuid: String) {
        CallHandler.callHandler {
            autocompleteTableQueries.delete(uuid)
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
            autocompleteTableQueries.updateJsonPath(jsonPath, uuid)
        }
    }

    /**
     * Retrieves all autocomplete options from the database and returns them as a list of [AutocompleteOptionModel]
     *
     * @return A list of AutocompleteOptionModel containing all autocomplete options
     */
    override fun getAllAutocompleteOptions(): List<AutocompleteOptionModel> =
        autocompleteTableQueries.selectAll { _, uuid, optionName, jsonPath ->
            AutocompleteOptionModel(uuid, optionName, jsonPath)
        }.executeAsList()

    /**
     * Check if a selected autocomplete option already exists in the database
     *
     * @param asmFilePath The parameter to check if the option already exists in the database
     * @return Returns 'true' if the option exists, and 'false' if it does not.
     */
    override fun existsSelectedAutocompleteOption(asmFilePath: String): Boolean =
        selectedAutocompleteTableQueries.exists(asmFilePath).executeAsOne() > 0

    /**
     * Inserts a new selected autocomplete option entry into the history database
     *
     * @param model The data model representing the selected autocomplete option
     */
    override suspend fun addSelectedAutocompleteOption(model: SelectedAutocompleteOptionModel) {
        CallHandler.callHandler {
            selectedAutocompleteTableQueries.insert(
                uuid = model.uuid,
                asmFilePath = model.asmFilePath,
                optionName = model.optionName,
                jsonPath = model.jsonPath
            )
        }
    }

    /**
     * Retrieves the selected autocomplete option for a specific ASM file
     *
     * @param asmFilePath The path of the ASM file for which the option was selected
     * @return The selected autocomplete option for the specified ASM file
     */
    override fun getSelectedAutocompleteOption(asmFilePath: String): SelectedAutocompleteOptionModel{
        val result = selectedAutocompleteTableQueries.select(asmFilePath){ _, uuid, filePath, optionName, jsonPath ->
            SelectedAutocompleteOptionModel(uuid, filePath, optionName, jsonPath)
        }.executeAsList()

        return if(result.isNotEmpty()) result[0]
        else SelectedAutocompleteOptionModel("", "", "", "")
    }

    /**
     * Updates the selected autocomplete option for a specific ASM file
     *
     * @param model The model containing the updated information for the selected autocomplete option
     */
    override suspend fun updateSelectedAutocompleteOption(model: SelectedAutocompleteOptionModel) {
        CallHandler.callHandler {
            selectedAutocompleteTableQueries.updateSelectedOption(
                uuid = model.uuid,
                optionName = model.optionName,
                jsonPath = model.jsonPath,
                asmFilePath = model.asmFilePath
            )
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
            selectedAutocompleteTableQueries.updateSelectedOptionJsonPath(jsonPath, uuid)
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
            selectedAutocompleteTableQueries.deleteSelectedOption(asmFilePath, uuid)
        }
    }

}