package com.dr10.common.utilities

import androidx.compose.runtime.mutableStateOf

class SettingsErrorState {
    // // State for the UUID associated with the option that caused the error
    val id = mutableStateOf("")
    // State to display the error message
    val displayErrorMessage = mutableStateOf(false)
    // State for the description of the occurred error
    val errorDescription = mutableStateOf("")
}