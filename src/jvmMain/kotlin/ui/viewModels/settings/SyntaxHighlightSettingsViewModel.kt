package ui.viewModels.settings

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.model.SyntaxHighlightConfigModel
import domain.repository.SettingRepository

class SyntaxHighlightSettingsViewModel(
    private val settingsRepository: SettingRepository
): ViewModel() {

    private val _allSyntaxHighlightConfigs: MutableLiveData<List<SyntaxHighlightConfigModel>> = MutableLiveData(emptyList())
    val allSyntaxHighlightConfigs: LiveData<List<SyntaxHighlightConfigModel>> = _allSyntaxHighlightConfigs

    private val _selectedOptionIndex: MutableLiveData<Int> = MutableLiveData(0)
    val selectedOptionIndex: LiveData<Int> = _selectedOptionIndex

    private val _isExpandColorOptionsList: MutableLiveData<List<Boolean>> = MutableLiveData(emptyList())
    val isExpandColorOptionsList: LiveData<List<Boolean>> = _isExpandColorOptionsList

    private val _codeText: MutableLiveData<String> = MutableLiveData("")
    val codeText: LiveData<String> = _codeText

    init {
        // Load all syntax highlight configurations from the repository
        _allSyntaxHighlightConfigs.value = settingsRepository.getAllSyntaxHighlightConfigs()

        // Initialize the color options expansion list with 'false' for each configuration
        _isExpandColorOptionsList.value = List(_allSyntaxHighlightConfigs.value.size){ false }
    }

    /**
     * Updates the list of syntax highlight configurations and maintains the selected option index and
     * ... color options expansion list
     */
    fun updateSyntaxHighlightConfigs(){
        // Load all syntax highlight configurations from the repository
        _allSyntaxHighlightConfigs.value = settingsRepository.getAllSyntaxHighlightConfigs()

        // Ensure the selected option index remains valid
        if(_selectedOptionIndex.value > _allSyntaxHighlightConfigs.value.size - 1){
            _selectedOptionIndex.value = if(_selectedOptionIndex.value == 0) 0 else selectedOptionIndex.value - 1
        }

        // Ensure the color options expansion list matches the size of the configurations
        if(_isExpandColorOptionsList.value.size < _allSyntaxHighlightConfigs.value.size){
            _isExpandColorOptionsList.value += false
        }

        if(_isExpandColorOptionsList.value.size > _allSyntaxHighlightConfigs.value.size){
            // Remove the last element from the expansion list if it exceeds the number of configurations
            val updateExpandedColorOptions = _isExpandColorOptionsList.value.toMutableList()
            updateExpandedColorOptions.removeLast()
            _isExpandColorOptionsList.value = updateExpandedColorOptions

        }
    }

    /**
     * Suspended function to update a syntax highlight configuration in the repository
     *
     * @param model The [SyntaxHighlightConfigModel] containing the updated configuration
     */
    suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightConfigModel) =
        settingsRepository.updateSyntaxHighlightConfig(model)

    /**
     * Update the selected option index with the specified [index]
     *
     * @param index The new index of the selected option
     */
    fun updateSelectedIndex(index: Int){
        _selectedOptionIndex.value = index
    }

    /**
     * Updates the expansion state for a specified item at the given [index]
     *
     * @param index The index of the item in the list
     * @param value The new expansion state to set
     */
    fun updateIsExpanded(index: Int, value: Boolean){
        // Create a mutable copy of the current list of expansion states
        val currentList = _isExpandColorOptionsList.value.toMutableList()

        // Check if the index is within the valid range
        if(index in 0 until currentList.size){
            currentList[index] = value
            _isExpandColorOptionsList.value = currentList
        }
    }

    /**
     * Deletes an autocomplete option and its related entities
     *
     * @param uuid Identifier associated with the configuration to be deleted
     */
    suspend fun deleteConfig(uuid: String){
        settingsRepository.deleteAutocompleteOption(uuid)
        settingsRepository.deleteSyntaxHighlightConfig(uuid)
        settingsRepository.deleteSelectedAutocompleteOption(uuid)
    }

    /**
     * Updates the content of [_codeText]
     *
     * @param value The content to update
     */
    fun updateCodeTex(value: String){
        _codeText.value = value
    }
}