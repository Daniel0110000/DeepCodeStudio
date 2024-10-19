package com.dr10.settings.ui.viewModels

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class SyntaxAndSuggestionsViewModel(
    private val syntaxAndSuggestionsRepository: SyntaxAndSuggestionsRepository
): ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow(SyntaxAndSuggestionsState())
    val state: StateFlow<SyntaxAndSuggestionsState> = _state.asStateFlow()

    data class SyntaxAndSuggestionsState(
        val optionName: String = "",
        val jsonPath: String = "",
        val allConfigs: List<SyntaxAndSuggestionModel> = emptyList(),
        val jsonAutocompleteOptionContainerWidth: Float = 300f,
        val selectedOption: SyntaxAndSuggestionModel = SyntaxAndSuggestionModel(),
        val isLoading: Boolean = false
    )

    init {
        scope.launch {
            syntaxAndSuggestionsRepository.getConfigs().collect {
                _state.value = _state.value.copy(allConfigs = it)
                if (_state.value.allConfigs.isNotEmpty()) setSelectedOption(_state.value.allConfigs.first())
            }
        }
    }

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

    fun deleteConfig(model: SyntaxAndSuggestionModel) {
        scope.launch { syntaxAndSuggestionsRepository.deleteConfig(model) }
    }

    fun clearFields() { _state.value = _state.value.copy(optionName = "", jsonPath = "") }

    fun setOptionName(name: String) { _state.value = _state.value.copy(optionName = name) }

    fun setJsonPath(path: String) { _state.value = _state.value.copy(jsonPath = path) }

    fun setJsonAutocompleteOptionContainerWidth(width: Float) {
        if((_state.value.jsonAutocompleteOptionContainerWidth + -width) > 220) {
            _state.value = _state.value.copy(
                jsonAutocompleteOptionContainerWidth = _state.value.jsonAutocompleteOptionContainerWidth + -width
            )
        }
    }

    fun setSelectedOption(option: SyntaxAndSuggestionModel) { _state.value = _state.value.copy(selectedOption = option) }

    fun setIsLoading(isLoading: Boolean) { _state.value = _state.value.copy(isLoading = isLoading) }

}