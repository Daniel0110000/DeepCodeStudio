package domain.repository

import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel

interface SettingRepository {

    suspend fun addAutocompleteOption(model: AutocompleteOptionModel)

    suspend fun deleteAutocompleteOption(model: AutocompleteOptionModel)

    suspend fun updateAutocompleteOptionJsonPath(jsonPath: String, model: AutocompleteOptionModel)

    fun getAllAutocompleteOptions(): List<AutocompleteOptionModel>

    fun existsAutocompleteOption(asmFilePath: String): Boolean

    suspend fun addSelectedAutocompleteOption(model: SelectedAutocompleteOptionModel)

}