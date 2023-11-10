package domain.repository

import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel
import domain.model.SyntaxHighlightConfigModel

interface SettingRepository {

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

    suspend fun createSyntaxHighlightConfig(model: SyntaxHighlightConfigModel)

    fun getAllSyntaxHighlightConfigs(): List<SyntaxHighlightConfigModel>

    fun getSyntaxHighlightConfig(uuid: String): SyntaxHighlightConfigModel

    suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightConfigModel)

    suspend fun deleteSyntaxHighlightConfig(uuid: String)

    // Change the name
    suspend fun updateSyntaxHighlightConfigJsonPath(jsonPath: String, uuid: String)

}