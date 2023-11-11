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

}