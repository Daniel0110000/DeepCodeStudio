package com.dr10.settings.ui.viewModels

import com.dr10.common.models.ColorSchemeModel
import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.database.domain.repositories.ColorSchemeRepository
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

/**
 * ViewModel for managing syntax and suggestions screen state
 *
 * @property syntaxAndSuggestionsRepository Repository for accessing syntax and suggestion configurations
 * @property colorSchemeRepository Repository for accessing color scheme of the syntax highlight
 */
class SyntaxAndSuggestionsViewModel(
    private val syntaxAndSuggestionsRepository: SyntaxAndSuggestionsRepository,
    private val colorSchemeRepository: ColorSchemeRepository
) {

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
        val isLoading: Boolean? = null,
        val defaultIndexSelected: Int = -1,
        val isCollapseJsonPreviewContainer: Boolean = false
    )

    init { retrieveConfigs() }

    private fun retrieveConfigs() {
        // Retrieves all the syntax and suggestion configurations
        scope.launch {
            syntaxAndSuggestionsRepository.getConfigs().collect { configs ->
                val newState = if (configs.isNotEmpty()) {
                    _state.value.copy(
                        allConfigs = configs,
                        selectedOption = configs.first(),
                        isCollapseJsonPreviewContainer = false
                    )
                } else {
                    _state.value.copy(
                        allConfigs = configs,
                        isCollapseJsonPreviewContainer = true
                    )
                }
                _state.emit(newState)
            }
        }
    }

    /**
     * Adds a new syntax and suggestion configuration
     */
    fun addConfig() {
        val currentState = _state.value
        if (currentState.optionName.isNotEmpty() && currentState.jsonPath.isNotEmpty()) {
            scope.launch {
                val uniqueId = UUID.randomUUID().toString()
                syntaxAndSuggestionsRepository.addConfig(
                    SyntaxAndSuggestionModel(
                        uniqueId = uniqueId,
                        optionName = currentState.optionName,
                        jsonPath = currentState.jsonPath
                    )
                ) {
                    if (!it) colorSchemeRepository.insertColorScheme(ColorSchemeModel(uniqueId = uniqueId))
                    setIsLoading(it)
                }
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
            updateState { copy(defaultIndexSelected = 1) }
        }
    }

    private fun clearFields() {
        updateState { copy(optionName = "", jsonPath = "") }
    }

    fun setOptionName(name: String) {
        updateState { copy(optionName = name) }
    }

    fun setJsonPath(path: String) {
        updateState { copy(jsonPath = path) }
    }

    fun setSelectedOption(option: SyntaxAndSuggestionModel) { _state.value = _state.value.copy(selectedOption = option) }

    private fun setIsLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }

    private fun updateState(update: SyntaxAndSuggestionsState.() -> SyntaxAndSuggestionsState) {
        _state.value = _state.value.update()
    }

}