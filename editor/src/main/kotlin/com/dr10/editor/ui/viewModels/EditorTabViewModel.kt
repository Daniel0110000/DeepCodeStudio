package com.dr10.editor.ui.viewModels

import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.utilities.JsonUtils
import com.dr10.database.domain.repositories.EditorRepository
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
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
 * @property syntaxAndSuggestionsRepository Repository for accessing syntax and suggestion configurations
 * @property editorRepository Repository for managing editor-related data
 */
class EditorTabViewModel(
    private val syntaxAndSuggestionsRepository: SyntaxAndSuggestionsRepository,
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
        val allConfigs: List<SyntaxAndSuggestionModel> = emptyList(),
        val isEditable: Boolean = true,
        val isCollapseAutocompleteOptions: Boolean = true,
        val selectedConfig: SelectedConfigHistoryModel? = null,
        val suggestionsFromJson: List<String> = emptyList()
    )


    /**
     * Retrieves the selected configuration for the current assembler file
     */
    fun getSelectedConfig() {
        coroutineScope.launch {
            val config = editorRepository.getSelectedConfig(_state.value.currentFilePath)
            setIsCollapseAutocompleteOptions(config != null)
            if (config != null) _state.update {
                it.copy(
                    selectedConfig = config,
                    suggestionsFromJson = JsonUtils.jsonToListString(config.jsonPath)
                )
            }
        }
    }

    /**
     * Inserts or updates the selected configuration based on the provided model
     *
     * @param model The [SyntaxAndSuggestionModel] to use for the configuration
     */
    fun insertOrUpdateSelectedConfig(model: SyntaxAndSuggestionModel) {
        coroutineScope.launch {
            val newModel = SelectedConfigHistoryModel(
                uniqueId = model.uniqueId,
                optionName = model.optionName,
                className = model.className,
                asmFilePath = _state.value.currentFilePath,
                jsonPath = model.jsonPath
            )
            if (_state.value.selectedConfig != null) editorRepository.updateSelectedConfig(newModel)
            else editorRepository.insertSelectedConfig(newModel)

            _state.update {
                it.copy(
                    selectedConfig = newModel,
                    suggestionsFromJson = JsonUtils.jsonToListString(newModel.jsonPath)
                )
            }

            setIsCollapseAutocompleteOptions(true)
        }
    }

    /**
     * Retrieves all available configurations from the repository
     */
    fun getAllConfigs() {
        coroutineScope.launch {
            _state.update { it.copy(
                allConfigs = syntaxAndSuggestionsRepository.getConfigsAsList()
            ) }
        }
    }

    fun setCurrentFilePath(value: String) {
        _state.update { it.copy(currentFilePath = value) }
    }

    fun setIsEditable(value: Boolean) {
        _state.update { it.copy(isEditable = value) }
    }

    fun setIsCollapseAutocompleteOptions(value: Boolean) {
        _state.update { it.copy(isCollapseAutocompleteOptions = value) }
    }

}