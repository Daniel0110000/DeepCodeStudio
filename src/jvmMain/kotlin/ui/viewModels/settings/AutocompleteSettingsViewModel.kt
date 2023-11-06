package ui.viewModels.settings

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.model.AutocompleteOptionModel
import domain.model.SyntaxHighlightConfigModel
import domain.repository.SettingRepository

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
     * Add a new autocomplete option and its corresponding syntax  highlight configuration to the repository
     *
     * @param autocompleteModel The [AutocompleteOptionModel] to be added
     * @param syntaxHighlightModel The [SyntaxHighlightConfigModel] to be added
     */
    suspend fun addAutocompleteOptionAndSyntaxHighlightConfig(
        autocompleteModel: AutocompleteOptionModel,
        syntaxHighlightModel: SyntaxHighlightConfigModel
    ){
        repository.addAutocompleteOption(autocompleteModel)
        repository.createSyntaxHighlightConfig(syntaxHighlightModel)
    }

    /**
     * Updates the jSON path of an existing autocomplete option and its corresponding syntax highlight configuration
     *
     * @param newJsonPath The new JSON path
     * @param model The [AutocompleteOptionModel] containing the current JSON path and option details
     */
    suspend fun updateAutocompleteAndSyntaxHighlightJsonPath(
        newJsonPath: String,
        model: AutocompleteOptionModel
    ){
        repository.updateAutocompleteOptionJsonPath(newJsonPath, model)
        repository.updateSyntaxHighlightConfigJsonPath(newJsonPath, model.jsonPath, model.optionName)
    }

    /**
     * Suspended function to delete an autocomplete option and its associated syntax highlight configuration
     *
     * @param model The [AutocompleteOptionModel] to be deleted, which contains option information and JSON path
     */
    suspend fun deleteAutocompleteOptionAndroidSyntaxHighlightConfig(
        model: AutocompleteOptionModel
    ){
        repository.deleteAutocompleteOption(model)
        repository.deleteSyntaxHighlightConfig(model.jsonPath)
    }

    /**
     * Updates the option name with the provided [value]
     *
     * @param value The new option name
     */
    fun updateOptionName(value: String){
        _optionName.value = value
    }

    /**
     * Updates the JSON path with the provided [value]
     *
     * @param value The new JSON path
     */
    fun updateJsonPath(value: String){
        _jsonPath.value = value
    }

}