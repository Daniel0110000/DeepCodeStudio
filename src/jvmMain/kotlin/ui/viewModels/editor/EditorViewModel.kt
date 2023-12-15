package ui.viewModels.editor

import androidx.compose.ui.text.input.TextFieldValue
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel
import domain.repositories.AutocompleteSettingsRepository
import domain.repositories.SyntaxHighlightSettingsRepository
import domain.utilies.JsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.editor.EditorErrorState
import ui.editor.EditorState
import ui.editor.tabs.TabModel
import java.io.File

class EditorViewModel(
    private val autocompleteRepository: AutocompleteSettingsRepository,
    private val syntaxHighlightRepository: SyntaxHighlightSettingsRepository
): ViewModel() {

    private val _isDisplayEditor: MutableLiveData<Boolean> = MutableLiveData(false)
    val isDisplayEditor: LiveData<Boolean> = _isDisplayEditor

    private val _selectedTabIndex: MutableLiveData<Int> = MutableLiveData(-1)
    val selectedTabIndex: LiveData<Int> = _selectedTabIndex

    private val _editorStates: MutableLiveData<List<EditorState>> = MutableLiveData(emptyList())
    val editorStates: LiveData<List<EditorState>> = _editorStates

    private val _allAutocompleteOptions: MutableLiveData<List<AutocompleteOptionModel>> = MutableLiveData(emptyList())
    val allAutocompleteOptions: LiveData<List<AutocompleteOptionModel>> = _allAutocompleteOptions

    private val _autocompleteOptionsViewWidth: MutableLiveData<Float> = MutableLiveData(300f)
    val autocompleteOptionsViewWidth: LiveData<Float> = _autocompleteOptionsViewWidth

    private val _selectedOption: MutableLiveData<AutocompleteOptionModel> = MutableLiveData(AutocompleteOptionModel())
    val selectedOption: LiveData<AutocompleteOptionModel> = _selectedOption

    private val _tabs: MutableLiveData<List<TabModel>> = MutableLiveData(emptyList())
    private val scope = CoroutineScope(Dispatchers.IO)


    /**
     * Handles the creation fo a new tab with the specified [filePath]
     *
     * @param filePath The file path associated with the new tab
     */
    fun onNewTab(
        filePath: String,
        fileName: String,
        errorState: EditorErrorState
    ){
        // Add a new state for the new tab
        _editorStates.value = _editorStates.value.plus(EditorState())

        // Set the selected tab index to the last added tab
        _selectedTabIndex.value = _editorStates.value.lastIndex

        // Set file path and initial code text for the tab
        _editorStates.value[_selectedTabIndex.value].apply {
            this.fileName.value = fileName
            this.filePath.value = filePath
            this.codeText.value = TextFieldValue(File(filePath).readText())
        }

        //  Check if there is a selected autocomplete option for the file
        if(!autocompleteRepository.existsSelectedAutocompleteOption(filePath)){
            _editorStates.value[_selectedTabIndex.value].displayAutocompleteOptions.value = true
        } else {
            // If a selected autocomplete option exists, load its associated syntax highlight configuration
            val option = autocompleteRepository.getSelectedAutocompleteOption(filePath)

            // Load keywords and variable directives from the JSON path specified in the selected autocomplete option
            _editorStates.value[_selectedTabIndex.value].apply {
                uuid.value = option.uuid
                syntaxHighlightConfig.value = syntaxHighlightRepository.getSyntaxHighlightConfig(option.uuid)
                syntaxHighlightRegexModel.value = JsonUtils.jsonToSyntaxHighlightRegexModel(option.jsonPath, errorState)
                keywords.value = JsonUtils.jsonToListString(option.jsonPath, errorState)
                variableDirectives.value = JsonUtils.extractVariablesAndConstantsKeywordsFromJson(option.jsonPath, errorState)
            }
        }

    }

    /**
     * Handles the selection of an autocomplete option specified by the [model]
     *
     * @param model The [AutocompleteOptionModel] representing rhe selected autocomplete option
     */
    fun selectedOption(
        model: AutocompleteOptionModel,
        errorState: EditorErrorState
    ){
        // Sets syntax highlight configuration, keywords and variable directives based on the selected autocomplete option
        _editorStates.value[_selectedTabIndex.value].apply {
            uuid.value = model.uuid
            syntaxHighlightConfig.value = syntaxHighlightRepository.getSyntaxHighlightConfig(model.uuid)
            syntaxHighlightRegexModel.value = JsonUtils.jsonToSyntaxHighlightRegexModel(model.jsonPath, errorState)
            keywords.value = JsonUtils.jsonToListString(model.jsonPath, errorState)
            variableDirectives.value = JsonUtils.extractVariablesAndConstantsKeywordsFromJson(model.jsonPath, errorState)

            // Hide the autocomplete options display
            displayAutocompleteOptions.value = false
        }

        // Asynchronously add the selected autocomplete option to the database
        scope.launch {
            autocompleteRepository.addSelectedAutocompleteOption(
                SelectedAutocompleteOptionModel(
                    uuid = model.uuid,
                    asmFilePath = editorStates.value[_selectedTabIndex.value].filePath.value,
                    optionName = model.optionName,
                    jsonPath = model.jsonPath)
            )
        }
    }

    /**
     * Updates the selected autocomplete option in the database and refreshes the editor state
     *
     * @param model The [AutocompleteOptionModel] representing the autocomplete option to be updated
     * @param editorState The current state of the editor
     */
    fun updateSelectedOption(
        model: AutocompleteOptionModel,
        editorState: EditorState,
        errorState: EditorErrorState
    ){
        scope.launch {
            // Updates the selected autocomplete option in the repository
            autocompleteRepository.updateSelectedAutocompleteOption(
                SelectedAutocompleteOptionModel(
                    uuid = model.uuid,
                    asmFilePath = editorState.filePath.value,
                    optionName = model.optionName,
                    jsonPath = model.jsonPath
                )
            )

            // Updates the editor state
            editorState.apply {
                uuid.value = model.uuid
                syntaxHighlightConfig.value = syntaxHighlightRepository.getSyntaxHighlightConfig(model.uuid)
                syntaxHighlightRegexModel.value = JsonUtils.jsonToSyntaxHighlightRegexModel(model.jsonPath, errorState)
                keywords.value = JsonUtils.jsonToListString(model.jsonPath, errorState)
                variableDirectives.value = JsonUtils.extractVariablesAndConstantsKeywordsFromJson(model.jsonPath, errorState)
                displayUpdateAutocompleteOption.value = false
            }
        }
    }

    fun deleteConfigs(uuid: String){
        CoroutineScope(Dispatchers.IO).launch {
            autocompleteRepository.deleteAutocompleteOption(uuid)
            syntaxHighlightRepository.deleteSyntaxHighlightConfig(uuid)
            autocompleteRepository.deleteSelectedAutocompleteOption(uuid = uuid)
        }
    }

    /**
     * Gets all autocomplete options from the repository and assigns them to [_allAutocompleteOptions]
     */
    fun getAllAutocompleteOptions(){
        _allAutocompleteOptions.value = autocompleteRepository.getAllAutocompleteOptions()
    }

    /**
     * Handles the deletion of a tab at the specified [filePath]
     *
     * @param filePath The file path associated with the tab deleted
     */
    fun onDeleteTab(filePath: String){
        _editorStates.value = _editorStates.value.filterIndexed { _, value -> value.filePath.value != filePath }
        _selectedTabIndex.value = _editorStates.value.lastIndex
    }

    /**
     * Sets the display editor using the provided [value]
     *
     * @param value The value to assign
     */
    fun setDisplayEditor(value: Boolean){
        _isDisplayEditor.value = value
    }

    /**
     * Sets the selected tab index using the provided [value]
     *
     * @param value The value to assign
     */
    fun setSelectedTabIndex(value: Int){
        _selectedTabIndex.value = value
    }


    /**
     * Sets the tabs using the provided [value]
     *
     * @param value The valor to assign
     */
    fun setTabs(value: List<TabModel>){
        _tabs.value = value
    }

    /**
     * Sets the autocomplete options view width using the provided [value]
     *
     * @param value The valor to assign
     */
    fun setAutocompleteOptionsViewWidth(value: Float){
        if(_autocompleteOptionsViewWidth.value + value > 250){
            _autocompleteOptionsViewWidth.value += value
        }
    }

    /**
     * Sets the selected option using the provided [value]
     *
     * @param value The valor to assign
     */
    fun setSelectedOption(value: AutocompleteOptionModel){
        _selectedOption.value = value
    }

}