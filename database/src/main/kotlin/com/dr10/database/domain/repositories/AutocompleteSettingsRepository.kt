package com.dr10.database.domain.repositories

import com.dr10.common.models.AutocompleteOptionModel
import com.dr10.common.models.AutocompleteSelectionHistoryModel
import kotlinx.coroutines.flow.Flow

interface AutocompleteSettingsRepository {
    suspend fun addAutocompleteOption(model: AutocompleteOptionModel)

    suspend fun deleteAutocompleteOption(uuid: String)

    suspend fun updateAutocompleteOptionJsonPath(jsonPath: String, uuid: String)

   fun getAllAutocompleteOptions(): Flow<List<AutocompleteOptionModel>>

   fun getAllAutocompleteOptionsAsList(): List<AutocompleteOptionModel>

    fun existsSelectedAutocompleteOption(asmFilePath: String): Boolean

    suspend fun addSelectedAutocompleteOption(model: AutocompleteSelectionHistoryModel)

    fun getSelectedAutocompleteOption(asmFilePath: String): AutocompleteSelectionHistoryModel

    suspend fun updateSelectedAutocompleteOption(model: AutocompleteSelectionHistoryModel)

    suspend fun updateSelectedAutocompleteOptionJsonPath(jsonPath: String, uuid: String)

    suspend fun deleteSelectedAutocompleteOption(asmFilePath: String = "", uuid: String = "")
}