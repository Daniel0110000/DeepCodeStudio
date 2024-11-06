package com.dr10.settings.ui.viewModels

import com.dr10.settings.model.Option
import com.dr10.settings.ui.screens.Screens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel {

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    data class SettingsState(
        val options: List<Option> = listOf(
            Option("Syntax & Suggestions", Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS, true),
            Option("Color Scheme", Screens.COLOR_SCHEME_SETTINGS, false),
        )
    )
}