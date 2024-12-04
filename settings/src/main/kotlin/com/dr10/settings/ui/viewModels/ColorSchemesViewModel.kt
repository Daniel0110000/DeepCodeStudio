package com.dr10.settings.ui.viewModels

import com.dr10.common.models.ColorSchemeModel
import com.dr10.common.utilities.Constants
import com.dr10.database.domain.repositories.ColorSchemeRepository
import com.dr10.settings.ui.screens.colorScheme.components.ColorSchemeOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ColorSchemesViewModel(
    private val colorSchemesRepository: ColorSchemeRepository
) {

    private val _state: MutableStateFlow<ColorSchemesState> = MutableStateFlow(ColorSchemesState())
    val state: StateFlow<ColorSchemesState> = _state

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    data class ColorSchemesState(
        val allColorSchemes: List<ColorSchemeModel> = emptyList(),
        val selectedOptionIndex: Int = 0,
        val isExpandColorsList: List<Boolean> = emptyList(),
        val selectedOption: ColorSchemeModel? = null
    )

    init { retrieveColorSchemes() }

    private fun retrieveColorSchemes() {
        coroutineScope.launch {
            colorSchemesRepository.getAllColorSchemes().collect {
                val currentExpanded = state.value.isExpandColorsList
                val newExpandedList = List(it.size) { index ->
                    if (index < currentExpanded.size) currentExpanded[index]
                    else false
                }
                updateState { copy(
                    allColorSchemes = it,
                    isExpandColorsList = newExpandedList,
                    selectedOption = _state.value.selectedOption?.let {
                            model -> it.find { fm -> fm.uniqueId == model.uniqueId }
                    } ?: it.firstOrNull()
                ) }
            }
        }
    }

    fun setSelectedOptionIndex(index: Int) {
        coroutineScope.launch {
            updateState { copy(
                selectedOptionIndex = index,
                selectedOption = allColorSchemes[index]
            ) }
        }
    }

    fun setIsExpandColorsList(index: Int) {
        updateState {
            val updatesList = isExpandColorsList.toMutableList()
            if (index in updatesList.indices) updatesList[index] = !updatesList[index]

            copy(isExpandColorsList = updatesList)
        }
    }

    fun updateColor(
        uniqueId: String,
        option: ColorSchemeOption,
        newColorValue: String
    ) {
        coroutineScope.launch {
            colorSchemesRepository.updateColor(
                uniqueId = uniqueId,
                simpleColor = if (option.title == Constants.SIMPLE) newColorValue else null,
                commentColor = if (option.title == Constants.COMMENT) newColorValue else null,
                reservedWordColor = if (option.title == Constants.RESERVED_WORD) newColorValue else null,
                reservedWord2Color = if (option.title == Constants.RESERVED_WORD_2) newColorValue else null,
                hexadecimalColor = if (option.title == Constants.HEXADECIMAL) newColorValue else null,
                numberColor = if (option.title == Constants.NUMBER) newColorValue else null,
                functionColor = if (option.title == Constants.FUNCTION) newColorValue else null,
                stringColor = if (option.title == Constants.STRING) newColorValue else null,
                dataTypeColor = if (option.title == Constants.DATA_TYPE) newColorValue else null,
                variableColor = if (option.title == Constants.VARIABLE) newColorValue else null,
                operatorColor = if (option.title == Constants.OPERATOR) newColorValue else null,
                processorColor = if (option.title == Constants.PROCESSOR) newColorValue else null
            )
        }
    }

    fun getIndex(model: ColorSchemeModel): Int = _state.value.allColorSchemes.indexOfFirst { it.uniqueId == model.uniqueId }

    private fun updateState(update: ColorSchemesState.() -> ColorSchemesState) { _state.update(update) }

}