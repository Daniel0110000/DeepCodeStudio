package domain.repositories

import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel

interface AutocompleteSettingsRepository {
    suspend fun addAutocompleteOption(model: AutocompleteOptionModel)

    suspend fun deleteAutocompleteOption(uuid: String)

    suspend fun updateAutocompleteOptionJsonPath(jsonPath: String, uuid: String)

    fun getAllAutocompleteOptions(): List<AutocompleteOptionModel>

    fun existsSelectedAutocompleteOption(asmFilePath: String): Boolean

    suspend fun addSelectedAutocompleteOption(model: SelectedAutocompleteOptionModel)

    fun getSelectedAutocompleteOption(asmFilePath: String): SelectedAutocompleteOptionModel

    suspend fun updateSelectedAutocompleteOption(model: SelectedAutocompleteOptionModel)

    suspend fun updateSelectedAutocompleteOptionJsonPath(jsonPath: String, uuid: String)

    suspend fun deleteSelectedAutocompleteOption(asmFilePath: String = "", uuid: String = "")
}