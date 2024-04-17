package com.dr10.database.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.deepCodeStudio.database.AppDatabase
import app.deepCodeStudio.database.AutocompleteOptionsQueries
import app.deepCodeStudio.database.AutocompleteSelectionHistoryQueries
import com.dr10.common.models.AutocompleteOptionModel
import com.dr10.common.models.AutocompleteSelectionHistoryModel
import com.dr10.common.utilities.CallHandler
import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AutocompleteSettingsRepositoryImpl(
    appDatabase: AppDatabase
): AutocompleteSettingsRepository {

    private val autocompleteOptionsQueries: AutocompleteOptionsQueries = appDatabase.autocompleteOptionsQueries
    private val autocompleteSelectionHistoryQueries: AutocompleteSelectionHistoryQueries = appDatabase.autocompleteSelectionHistoryQueries


    /**
     * Inserts a new option for autocomplete into the database
     *
     * @param model The data to be inserted into the database
     */
    override suspend fun addAutocompleteOption(model: AutocompleteOptionModel) {
        CallHandler.callHandler {
            autocompleteOptionsQueries.insert(
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
        CallHandler.callHandler { autocompleteOptionsQueries.delete(uuid) }
    }

    /**
     * Updates the JSOn path of an autocomplete option specified by the [uuid]
     *
     * @param jsonPath The new JSON path to set for the autocomplete option
     * @param uuid The UUID of the autocomplete option to update
     */
    override suspend fun updateAutocompleteOptionJsonPath(jsonPath: String, uuid: String) {
        CallHandler.callHandler { autocompleteOptionsQueries.updateJsonPath(jsonPath, uuid) }
    }

    /**
     * Retrieves all autocomplete options from the database
     *
     * @return A [Flow] a [List] of [AutocompleteOptionModel] objects representing the options
     */
    override fun getAllAutocompleteOptions(): Flow<List<AutocompleteOptionModel>> = autocompleteOptionsQueries.selectAll { _, uuid, optionName, jsonPath ->
        AutocompleteOptionModel(uuid, optionName, jsonPath)
    }.asFlow().map { it.executeAsList() }

    /**
     * Retrieves all autocomplete options from the database and returns them as a list.
     *
     * @return A list of AutocompleteOptionModel objects representing the options.
     */
    override fun getAllAutocompleteOptionsAsList(): List<AutocompleteOptionModel> = autocompleteOptionsQueries.selectAll { _, uuid, optionName, jsonPath ->
        AutocompleteOptionModel(uuid, optionName, jsonPath)
    }.executeAsList()

    /**
     * Check if a selected autocomplete option already exists in the database
     *
     * @param asmFilePath The parameter to check if the option already exists in the database
     * @return Returns 'true' if the option exists, and 'false' if it does not.
     */
    override fun existsSelectedAutocompleteOption(asmFilePath: String): Boolean =
        autocompleteSelectionHistoryQueries.exists(asmFilePath).executeAsOne() > 0

    /**
     * Inserts a new selected autocomplete option entry into the history database
     *
     * @param model The data model representing the selected autocomplete option
     */
    override suspend fun addSelectedAutocompleteOption(model: AutocompleteSelectionHistoryModel) {
        CallHandler.callHandler {
            autocompleteSelectionHistoryQueries.insert(
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
    override fun getSelectedAutocompleteOption(asmFilePath: String): AutocompleteSelectionHistoryModel {
        val result = autocompleteSelectionHistoryQueries.select(asmFilePath){ _, uuid, filePath, optionName, jsonPath ->
            AutocompleteSelectionHistoryModel(uuid, filePath, optionName, jsonPath)
        }.executeAsList()

        return if(result.isNotEmpty()) result[0]
        else AutocompleteSelectionHistoryModel("", "", "", "")
    }

    /**
     * Updates the selected autocomplete option for a specific ASM file
     *
     * @param model The model containing the updated information for the selected autocomplete option
     */
    override suspend fun updateSelectedAutocompleteOption(model: AutocompleteSelectionHistoryModel) {
        CallHandler.callHandler {
            autocompleteSelectionHistoryQueries.updateSelectedOption(
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
            autocompleteSelectionHistoryQueries.updateSelectedOptionJsonPath(jsonPath, uuid)
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
            autocompleteSelectionHistoryQueries.deleteSelectedOption(asmFilePath, uuid)
        }
    }
}