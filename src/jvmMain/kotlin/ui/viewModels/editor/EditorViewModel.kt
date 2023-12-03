package ui.viewModels.editor

import androidx.compose.ui.text.input.TextFieldValue
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.model.AutocompleteOptionModel
import domain.model.SelectedAutocompleteOptionModel
import domain.repository.SettingRepository
import domain.utilies.JsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.editor.EditorComposable
import ui.editor.EditorState
import ui.editor.EditorTabComposable
import ui.editor.tabs.EditorTabsModel
import java.io.File

class EditorViewModel(
    private val repository: SettingRepository
): ViewModel() {

    private val _isDisplayEditor: MutableLiveData<Boolean> = MutableLiveData(false)
    val isDisplayEditor: LiveData<Boolean> = _isDisplayEditor

    private val _selectedTabIndex: MutableLiveData<Int> = MutableLiveData(0)
    val selectedTabIndex: LiveData<Int> = _selectedTabIndex

    private val _editorComposables: MutableLiveData<List<EditorComposable>> = MutableLiveData(emptyList())
    val editorComposables: LiveData<List<EditorComposable>> = _editorComposables

    private val _editorStates: MutableLiveData<List<EditorState>> = MutableLiveData(emptyList())
    val editorStates: LiveData<List<EditorState>> = _editorStates

    private val _allAutocompleteOptions: MutableLiveData<List<AutocompleteOptionModel>> = MutableLiveData(emptyList())
    val allAutocompleteOptions: LiveData<List<AutocompleteOptionModel>> = _allAutocompleteOptions

    private val _autocompleteOptionsViewWidth: MutableLiveData<Float> = MutableLiveData(300f)
    val autocompleteOptionsViewWidth: LiveData<Float> = _autocompleteOptionsViewWidth

    private val _selectedOption: MutableLiveData<AutocompleteOptionModel> = MutableLiveData(AutocompleteOptionModel())
    val selectedOption: LiveData<AutocompleteOptionModel> = _selectedOption

    private val _tabs: MutableLiveData<List<EditorTabsModel>> = MutableLiveData(emptyList())
    private val scope = CoroutineScope(Dispatchers.IO)


    /**
     * Handles the creation fo a new tab with the specified [filePath]
     *
     * @param filePath The file path associated with the new tab
     */
    fun onNewTab(filePath: String){
        // Add a new composable and state for the new tab
        _editorComposables.value = _editorComposables.value.plus(EditorTabComposable)
        _editorStates.value = _editorStates.value.plus(EditorState())

        // Set the selected tab index to the last added tab
        _selectedTabIndex.value = editorComposables.value.lastIndex

        // Set file path and initial code text for the tab
        _editorStates.value[_selectedTabIndex.value].apply {
            this.filePath.value = filePath
            this.codeText.value = TextFieldValue(File(filePath).readText())
        }

        //  Check if there is a selected autocomplete option for the file
        if(!repository.existsSelectedAutocompleteOption(filePath)){
            _editorStates.value[_selectedTabIndex.value].displayAutocompleteOptions.value = true
        } else {
            // If a selected autocomplete option exists, load its associated syntax highlight configuration
            val option = repository.getSelectedAutocompleteOption(filePath)

            // Load keywords and variable directives from the JSON path specified in the selected autocomplete option
            _editorStates.value[_selectedTabIndex.value].apply {
                syntaxHighlightConfig.value = repository.getSyntaxHighlightConfig(option.uuid)
                keywords.value = JsonUtils.jsonToListString(option.jsonPath)
                variableDirectives.value = JsonUtils.extractVariablesAndConstantsKeywordsFromJson(option.jsonPath)
            }
        }

    }

    /**
     * Handles the selection of an autocomplete option specified by the [model]
     *
     * @param model The [AutocompleteOptionModel] representing rhe selected autocomplete option
     */
    fun selectedOption(model: AutocompleteOptionModel){
        // Sets syntax highlight configuration, keywords and variable directives based on the selected autocomplete option

        _editorStates.value[_selectedTabIndex.value].apply {
            syntaxHighlightConfig.value = repository.getSyntaxHighlightConfig(model.uuid)
            keywords.value = JsonUtils.jsonToListString(model.jsonPath)
            variableDirectives.value = JsonUtils.extractVariablesAndConstantsKeywordsFromJson(model.jsonPath)

            // Hide the autocomplete options display
            displayAutocompleteOptions.value = false
        }

        // Asynchronously add the selected autocomplete option to the database
        scope.launch {
            repository.addSelectedAutocompleteOption(
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
    fun updateSelectedOption(model: AutocompleteOptionModel, editorState: EditorState){
        scope.launch {
            // Updates the selected autocomplete option in the repository
            repository.updateSelectedAutocompleteOption(
                SelectedAutocompleteOptionModel(
                    uuid = model.uuid,
                    asmFilePath = editorState.filePath.value,
                    optionName = model.optionName,
                    jsonPath = model.jsonPath
                )
            )

            // Updates the editor state
            editorState.apply {
                syntaxHighlightConfig.value = repository.getSyntaxHighlightConfig(model.uuid)
                keywords.value = JsonUtils.jsonToListString(model.jsonPath)
                variableDirectives.value = JsonUtils.extractVariablesAndConstantsKeywordsFromJson(model.jsonPath)
                displayUpdateAutocompleteOption.value = false
            }
        }
    }

    /**
     * Gets all autocomplete options from the repository and assigns them to [_allAutocompleteOptions]
     */
    fun getAllAutocompleteOptions(){
        _allAutocompleteOptions.value = repository.getAllAutocompleteOptions()
    }

    /**
     * Parses and displays an error line or warning line based on the result string
     *
     * @param result The result string containing error information
     */
    fun displayErrorOrWarningLine(result: String){
        val splitMessage = result.split("\n")
        val regex = Regex("""^([^:]+):(\d+): (error|warning):""")

        val matchResult = regex.find(splitMessage[0])

        // If there is a match, extract the file name and line number
        if(matchResult != null){
            val (fileName, lineNumber, type) = matchResult.destructured

            _tabs.value.forEachIndexed { index, editorTabsModel ->
                if(editorTabsModel.fileName == fileName){
                    _editorStates.value[index].errorLineIndex.value = lineNumber.toInt()
                    if(type == "error") _editorStates.value[index].displayErrorLine.value = true
                    else _editorStates.value[index].displayWarningLine.value = true
                    setSelectedTabIndex(index)
                }
            }
        }

    }

    /**
     * Handles the deletion of a tab at the specified [index] and associated file [filePath]
     *
     * @param index The index of the tab to be selected
     * @param filePath The file path associated with the tab deleted
     */
    fun onDeleteTab(index: Int, filePath: String){
        _editorComposables.value = _editorComposables.value.filterIndexed { i, _ -> i != index  }
        _editorStates.value = _editorStates.value.filterIndexed { _, value -> value.filePath.value != filePath }

        _selectedTabIndex.value = _editorComposables.value.lastIndex
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
    fun setTabs(value: List<EditorTabsModel>){
        _tabs.value = value
    }

    /**
     * Sets the autocomplete options view width using the provided [value]
     *
     * @param value The valor to assign
     */
    fun setAutocompleteOptionsViewWidth(value: Float){
        _autocompleteOptionsViewWidth.value = value
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