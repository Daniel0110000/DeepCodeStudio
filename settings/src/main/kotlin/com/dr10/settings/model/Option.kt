package com.dr10.settings.model

import com.dr10.settings.ui.screens.Screens

data class Option(
    val title: String,
    val screen: Screens,
    val isSelected: Boolean
)