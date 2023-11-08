package ui.viewModels.settings

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.model.AutocompleteOptionModel
import domain.model.SyntaxHighlightConfigModel
import domain.repository.SettingRepository
import java.util.UUID

class AutocompleteSettingsViewModel(
    private val repository: SettingRepository
): ViewModel() {

    private val _allAutocompleteOptions: MutableLiveData<List<AutocompleteOptionModel>> = MutableLiveData(emptyList())
    val allAutocompleteOptions: LiveData<List<AutocompleteOptionModel>> = _allAutocompleteOptions

    private val _optionName: MutableLiveData<String> = MutableLiveData("")
    val optionName: LiveData<String> = _optionName

    private val _jsonPath: MutableLiveData<String> = MutableLiveData("")
    val jsonPath: LiveData<String> = _jsonPath

    init {
        // Load all autocomplete options from the repository
        _allAutocompleteOptions.value = repository.getAllAutocompleteOptions()
    }

    /**
     * Update the list of autocomplete options with the options retrieved from the repository
     */
    fun updateAutocompleteOptions(){
        _allAutocompleteOptions.value = repository.getAllAutocompleteOptions()
    }

    /**
     * Adds a configuration by creating both an autocomplete option and a syntax highlight configuration
     *
     * @param autocompleteModel The [AutocompleteOptionModel] to be added
     * @param syntaxHighlightModel The [SyntaxHighlightConfigModel] to be added
     */
    suspend fun addConfig(
        autocompleteModel: AutocompleteOptionModel,
        syntaxHighlightModel: SyntaxHighlightConfigModel
    ){
        val uuid = UUID.randomUUID().toString()
        repository.addAutocompleteOption(autocompleteModel.copy(uuid = uuid))
        repository.createSyntaxHighlightConfig(syntaxHighlightModel.copy(uuid = uuid))
    }

    /**
     * Updates the JSON path for an autocomplete option and related entities
     *
     * @param newJsonPath The new JSON path to set for the autocomplete option
     * @param model The [AutocompleteOptionModel] to be updated
     */
    suspend fun updateJsonPath(
        newJsonPath: String,
        model: AutocompleteOptionModel
    ){
        repository.updateAutocompleteOptionJsonPath(newJsonPath, model.uuid)
        repository.updateSyntaxHighlightConfigJsonPath(newJsonPath, model.uuid)
        repository.updateSelectedAutocompleteOptionJsonPath(newJsonPath, model.uuid)
    }

    /**
     * Deletes an autocomplete option and its related entities
     *
     * @param model The [AutocompleteOptionModel] to be deleted
     */
    suspend fun deleteConfig(
        model: AutocompleteOptionModel
    ){
        repository.deleteAutocompleteOption(model.uuid)
        repository.deleteSyntaxHighlightConfig(model.uuid)
        repository.deleteSelectedAutocompleteOption(uuid = model.uuid)
    }

    /**
     * Sets the option name using the provided [value]
     *
     * @param value The value to assign
     */
    fun setOptionName(value: String){
        _optionName.value = value
    }

    /**
     * Sets the JSON path using the provided [value]
     *
     * @param value The value to assign
     */
    fun setJsonPath(value: String){
        _jsonPath.value = value
    }

}