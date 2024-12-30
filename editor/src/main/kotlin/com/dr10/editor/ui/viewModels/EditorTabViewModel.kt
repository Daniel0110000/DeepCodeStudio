package com.dr10.editor.ui.viewModels

import com.dr10.common.models.RegexRuleModel
import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.utilities.JsonUtils
import com.dr10.database.domain.repositories.EditorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing editor tab state
 *
 * @property editorRepository Repository for managing editor-related data
 */
class EditorTabViewModel(
    private val editorRepository: EditorRepository
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(EditorTabState())
    val state: StateFlow<EditorTabState> = _state.asStateFlow()

    /**
     * Data class representing the editor tab's state
     *
     * @property currentFilePath Path of the currently opened file
     * @property allConfigs List of all available syntax configurations
     * @property isEditable Whether the editor is in editable mode
     * @property isCollapseAutocompleteOptions Whether the autocomplete options panel is collapses
     * @property selectedConfig Currently selected configuration for the assembler file
     */
    data class EditorTabState(
        val currentFilePath: String = "",
        val allConfigs: List<SelectedConfigHistoryModel> = emptyList(),
        val patterns: List<RegexRuleModel> = emptyList(),
        val isEditable: Boolean = true,
        val isCollapseAutocompleteOptions: Boolean = true,
        val isAnalyzing: Boolean = false,
        val selectedConfig: SelectedConfigHistoryModel? = null,
        val suggestionsFromJson: List<String> = emptyList()
    )


    /**
     * Retrieves the selected configuration for the current assembler file
     */
    fun getSelectedConfig() {
        coroutineScope.launch {
            val config = editorRepository.getSelectedConfig(_state.value.currentFilePath)
            val patterns = editorRepository.getRegexRules(config?.uniqueId ?: "")
            updateState {
                copy(
                    selectedConfig = config,
                    patterns = patterns,
                    suggestionsFromJson = config?.let { JsonUtils.jsonToListString(it.jsonPath) } ?: emptyList(),
                    isCollapseAutocompleteOptions = config != null
                )

            }
        }
    }

    /**
     * Inserts or updates the selected configuration based on the provided model
     *
     * @param model The [SyntaxAndSuggestionModel] to use for the configuration
     */
    fun insertOrUpdateSelectedConfig(model: SelectedConfigHistoryModel) {
        coroutineScope.launch {
            val currentState = _state.value
            val newModel = model.copy(asmFilePath = currentState.currentFilePath)
            if (currentState.selectedConfig != null) {
                editorRepository.updateSelectedConfig(
                    uniqueId = model.uniqueId,
                    asmFilePath = currentState.currentFilePath
                )
            } else {
                editorRepository.insertSelectedConfig(
                    uniqueId = model.uniqueId,
                    asmFilePath = currentState.currentFilePath
                )
            }

            updateState { copy(
                selectedConfig = newModel,
                suggestionsFromJson = JsonUtils.jsonToListString(newModel.jsonPath),
                isCollapseAutocompleteOptions = true
            ) }
        }
    }

    /**
     * Retrieves all available configurations from the repository
     */
    fun getAllConfigs() {
        coroutineScope.launch {
            val configs = editorRepository.getAllConfigs()
            updateState { copy(allConfigs = configs) }
        }
    }
    
    fun setCurrentFilePath(value: String) {
        updateState { copy(currentFilePath = value) }
    }

    fun setIsEditable(value: Boolean) {
        updateState { copy(isEditable = value) }
    }

    fun setIsCollapseAutocompleteOptions(value: Boolean) {
        updateState { copy(isCollapseAutocompleteOptions = value) }
    }

    fun setIsAnalyzing(value: Boolean) {
        updateState { copy(isAnalyzing = value) }
    }

    private fun updateState(update: EditorTabState.() -> EditorTabState) { _state.update(update) }

}