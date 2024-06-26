package com.dr10.settings.ui.viewModels

import com.dr10.common.models.AutocompleteOptionModel
import com.dr10.common.models.SyntaxHighlightOptionModel
import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import com.dr10.database.domain.repositories.SyntaxHighlightSettingsRepository
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class AutocompleteSettingsViewModel(
    private val autocompleteRepository: AutocompleteSettingsRepository,
    private val syntaxHighlightRepository: SyntaxHighlightSettingsRepository
): ViewModel() {

    private val _allAutocompleteOptions: MutableLiveData<List<AutocompleteOptionModel>> = MutableLiveData(emptyList())
    val allAutocompleteOptions: LiveData<List<AutocompleteOptionModel>> = _allAutocompleteOptions

    private val _optionName: MutableLiveData<String> = MutableLiveData("")
    val optionName: LiveData<String> = _optionName

    private val _jsonPath: MutableLiveData<String> = MutableLiveData("")
    val jsonPath: LiveData<String> = _jsonPath

    private val _selectedOption: MutableLiveData<AutocompleteOptionModel> = MutableLiveData(AutocompleteOptionModel())
    val selectedOption: LiveData<AutocompleteOptionModel> = _selectedOption

    private val _jsonAutocompleteOptionContainerWidth: MutableLiveData<Float> = MutableLiveData(300f)
    val jsonAutocompleteOptionContainerWidth: LiveData<Float> = _jsonAutocompleteOptionContainerWidth

    init {
        CoroutineScope(Dispatchers.IO).launch {
            autocompleteRepository.getAllAutocompleteOptions().collect {
                _allAutocompleteOptions.value = it
                if(_allAutocompleteOptions.value.isNotEmpty()) setSelectedOption(_allAutocompleteOptions.value.first())
            }
        }
    }

    /**
     * Adds a configuration by creating both an autocomplete option and a syntax highlight configuration
     *
     * @param autocompleteModel The [AutocompleteOptionModel] to be added
     * @param syntaxHighlightModel The [SyntaxHighlightOptionModel] to be added
     */
    suspend fun addConfig(
        autocompleteModel: AutocompleteOptionModel,
        syntaxHighlightModel: SyntaxHighlightOptionModel
    ){
        val uuid = UUID.randomUUID().toString()
        autocompleteRepository.addAutocompleteOption(autocompleteModel.copy(uuid = uuid))
        syntaxHighlightRepository.createSyntaxHighlightConfig(syntaxHighlightModel.copy(uuid = uuid))
        setOptionName("")
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
        autocompleteRepository.updateAutocompleteOptionJsonPath(newJsonPath, model.uuid)
        syntaxHighlightRepository.updateSyntaxHighlightConfigJsonPath(newJsonPath, model.uuid)
        autocompleteRepository.updateSelectedAutocompleteOptionJsonPath(newJsonPath, model.uuid)
    }

    /**
     * Deletes an autocomplete option and its related entities
     *
     * @param uuid Identifier associated with the configuration to be deleted
     */
    suspend fun deleteConfig(uuid: String){
        autocompleteRepository.deleteAutocompleteOption(uuid)
        syntaxHighlightRepository.deleteSyntaxHighlightConfig(uuid)
        autocompleteRepository.deleteSelectedAutocompleteOption(uuid = uuid)
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

    /**
     * Sets the selected option using the provided [value]
     *
     * @param value The value to assign
     */
    fun setSelectedOption(value: AutocompleteOptionModel){
        _selectedOption.value = value
    }

    /**
     * Sets the JSON autocomplete option container width using the provided [value]
     *
     * @param value The value to assign
     */
    fun setJsonAutocompleteOptionContainerWidth(value: Float){
        // If [(_jsonAutocompleteOptionContainerWidth + -value)] is greater than [220], it allows further changes to the width
        if((_jsonAutocompleteOptionContainerWidth.value + -value) > 220) _jsonAutocompleteOptionContainerWidth.value += -value
    }

}