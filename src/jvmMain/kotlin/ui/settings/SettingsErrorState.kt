package ui.settings

import androidx.compose.runtime.mutableStateOf

class SettingsErrorState {
    // // State for the UUID associated with the option that caused the error
    val uuid = mutableStateOf("")
    // State to display the error message
    val displayErrorMessage = mutableStateOf(false)
    // State for the description of the occurred error
    val errorDescription = mutableStateOf("")
}