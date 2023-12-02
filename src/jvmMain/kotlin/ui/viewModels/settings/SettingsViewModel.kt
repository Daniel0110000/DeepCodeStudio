package ui.viewModels.settings

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import ui.settings.Screens

class SettingsViewModel: ViewModel() {

    private val _screen: MutableLiveData<Screens> = MutableLiveData(Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS)
    val screen: LiveData<Screens> = _screen

    val options: MutableLiveData<List<String>> = MutableLiveData(listOf("Syntax Keyword Highlighter", "Autocomplete"))
    val screens: MutableLiveData<List<Screens>> = MutableLiveData(listOf(Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS, Screens.AUTOCOMPLETE_SETTINGS))

    private val _selectedItem: MutableLiveData<Int> = MutableLiveData(0)
    val selectedItem: MutableLiveData<Int> = _selectedItem

    private val _displayErrorMessage: MutableLiveData<Boolean> = MutableLiveData(false)
    val displayErrorMessage: LiveData<Boolean> = _displayErrorMessage

    private val _errorDescription: MutableLiveData<String> = MutableLiveData("")
    val errorDescription: LiveData<String> = _errorDescription

    private val _settingsOptionsWidth: MutableLiveData<Float> = MutableLiveData(220f)
    val settingsOptionsWidth: LiveData<Float> = _settingsOptionsWidth

    /**
     * Sets the screen using the provided [value]
     *
     * @param value The value to assign
     */
    fun setScreen(value: Screens){
        _screen.value = value
    }

    /**
     * Sets the selected item using the provided [value]
     *
     * @param value The value to assign
     */
    fun setSelectedItem(value: Int){
        _selectedItem.value = value
    }

    /**
     * Sets the display error message using the provided [value]
     *
     * @param value The value to assign
     */
    fun setDisplayErrorMessage(value: Boolean){
        _displayErrorMessage.value = value
    }

    /**
     * Sets the error description using the provided [value]
     *
     * @param value The value to assign
     */
    fun setErrorDescription(value: String){
        _errorDescription.value = value
    }

    /**
     * Sets the settings options width using the provided [value]
     *
     * @param value The value to assign
     */
    fun setSettingsOptionsWidth(value: Float){
        // If [(_settingsOptionsWidth + value)] is greater than [200], it allows further changes to the width
        if((_settingsOptionsWidth.value + value) > 200) _settingsOptionsWidth.value += value
    }

}