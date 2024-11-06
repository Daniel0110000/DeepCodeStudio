package com.dr10.settings.ui.viewModels

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.database.domain.repositories.EditorRepository
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel for managing syntax and suggestions screen state
 *
 * @property syntaxAndSuggestionsRepository Repository for accessing syntax and suggestion configurations
 * @property editorRepository Repository for managing editor-related data
 */
class SyntaxAndSuggestionsViewModel(
    private val syntaxAndSuggestionsRepository: SyntaxAndSuggestionsRepository,
    private val editorRepository: EditorRepository
): ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow(SyntaxAndSuggestionsState())
    val state: StateFlow<SyntaxAndSuggestionsState> = _state.asStateFlow()

    /**
     * Data class for the state of the syntax and suggestions screen
     *
     * @property optionName The name of the option
     * @property jsonPath The path to the JSON file
     * @property allConfigs A list of all the configs
     * @property selectedOption The selected option
     * @property isLoading Whether the screen is loading
     * @property defaultIndexSelected The default index selected
     * @property isCollapseJsonPreviewContainer Whether the JSON preview container is collapsed
     */
    data class SyntaxAndSuggestionsState(
        val optionName: String = "",
        val jsonPath: String = "",
        val allConfigs: List<SyntaxAndSuggestionModel> = emptyList(),
        val selectedOption: SyntaxAndSuggestionModel = SyntaxAndSuggestionModel(),
        val isLoading: Boolean = false,
        val defaultIndexSelected: Int = -1,
        val isCollapseJsonPreviewContainer: Boolean = false
    )

    init {
        // Retrieves all the syntax and suggestion configurations
        scope.launch {
            syntaxAndSuggestionsRepository.getConfigs().collect {
                _state.value = if (it.isNotEmpty()) {
                    _state.value.copy(
                        allConfigs = it,
                        selectedOption = it.first(),
                        isCollapseJsonPreviewContainer = false
                    )
                } else {
                    _state.value.copy(
                        allConfigs = it,
                        isCollapseJsonPreviewContainer = true
                    )
                }
            }
        }
    }

    /**
     * Adds a new syntax and suggestion configuration
     */
    fun addConfig() {
        if (
            _state.value.optionName.isNotEmpty() &&
            _state.value.jsonPath.isNotEmpty()
        ) {
            scope.launch {
                syntaxAndSuggestionsRepository.addConfig(
                    SyntaxAndSuggestionModel(
                        uniqueId = UUID.randomUUID().toString(),
                        optionName = _state.value.optionName,
                        jsonPath = _state.value.jsonPath
                    )
                ) { setIsLoading(it) }
                clearFields()
            }
        }
    }

    /**
     * Deletes the currently selected syntax and suggestion configuration
     */
    fun deleteConfig() {
        scope.launch {
            val selectedOption = _state.value.selectedOption
            syntaxAndSuggestionsRepository.deleteConfig(selectedOption)
            editorRepository.deleteSelectedConfig(selectedOption.uniqueId)
            _state.value =_state.value.copy(defaultIndexSelected = 1)
        }
    }

    private fun clearFields() { _state.value = _state.value.copy(optionName = "", jsonPath = "") }

    fun setOptionName(name: String) { _state.value = _state.value.copy(optionName = name) }

    fun setJsonPath(path: String) { _state.value = _state.value.copy(jsonPath = path) }

    fun setSelectedOption(option: SyntaxAndSuggestionModel) { _state.value = _state.value.copy(selectedOption = option) }

    private fun setIsLoading(isLoading: Boolean) { _state.value = _state.value.copy(isLoading = isLoading) }

}