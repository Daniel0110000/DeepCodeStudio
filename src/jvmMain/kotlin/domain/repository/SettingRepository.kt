package domain.repository

import domain.model.AutocompleteOptionModel

interface SettingRepository {

    suspend fun addAutocompleteOption(model: AutocompleteOptionModel)

    suspend fun deleteAutocompleteOption(model: AutocompleteOptionModel)

    suspend fun updateAutocompleteOptionJsonPath(jsonPath: String, model: AutocompleteOptionModel)

    fun getAllAutocompleteOptions(): List<AutocompleteOptionModel>
}