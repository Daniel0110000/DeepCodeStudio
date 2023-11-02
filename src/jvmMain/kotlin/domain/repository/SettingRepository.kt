package domain.repository

import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel
import domain.model.SyntaxHighlightConfigModel

interface SettingRepository {

    suspend fun addAutocompleteOption(model: AutocompleteOptionModel)

    suspend fun deleteAutocompleteOption(model: AutocompleteOptionModel)

    suspend fun updateAutocompleteOptionJsonPath(jsonPath: String, model: AutocompleteOptionModel)

    fun getAllAutocompleteOptions(): List<AutocompleteOptionModel>

    fun existsAutocompleteOption(asmFilePath: String): Boolean

    suspend fun addSelectedAutocompleteOption(model: SelectedAutocompleteOptionModel)

    fun getSelectedAutocompleteOption(asmFilePath: String): SelectedAutocompleteOptionModel

    suspend fun updateSelectedAutocompleteOption(model: SelectedAutocompleteOptionModel)

    suspend fun deleteSelectedAutocompleteOption(asmFilePath: String)

    suspend fun createSyntaxHighlightConfig(model: SyntaxHighlightConfigModel)

    fun getAllSyntaxHighlightConfigs(): List<SyntaxHighlightConfigModel>

    suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightConfigModel)

    suspend fun deleteSyntaxHighlightConfig(jsonPath: String)

    // Change the name
    suspend fun updateSyntaxHighlightConfigJsonPath(newJsonPath: String, oldJsonPath: String, optionName: String)

}