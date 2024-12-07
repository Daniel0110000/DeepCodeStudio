package com.dr10.settings.ui.viewModels

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CodeExtractionViewModel(
    private val syntaxAndSuggestionRepository: SyntaxAndSuggestionsRepository
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow(CodeExtractionState())
    val state: StateFlow<CodeExtractionState> = _state.asStateFlow()

    /**
     * Data class for the state of the code extraction screen
     *
     * @property allConfigs A list of all the syntax and suggestion configurations
     */
    data class CodeExtractionState(
        val allConfigs: List<SyntaxAndSuggestionModel> = emptyList()
    )

    init { retrieveConfigs() }

    /**
     * Retrieves all the syntax and suggestion configurations
     */
    private fun retrieveConfigs() {
        scope.launch {
            syntaxAndSuggestionRepository.getConfigs().collect { configs ->
                val newState = _state.value.copy(allConfigs = configs)
                _state.emit(newState)
            }
        }
    }

}