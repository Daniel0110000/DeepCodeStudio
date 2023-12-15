package ui.viewModels.settings

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.repositories.AutocompleteSettingsRepository
import domain.repositories.SyntaxHighlightSettingsRepository
import ui.settings.Screens

class SettingsViewModel(
    private val autocompleteRepository: AutocompleteSettingsRepository,
    private val syntaxHighlightRepository: SyntaxHighlightSettingsRepository
): ViewModel() {

    private val _screen: MutableLiveData<Screens> = MutableLiveData(Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS)
    val screen: LiveData<Screens> = _screen

    val options: MutableLiveData<List<String>> = MutableLiveData(listOf("Syntax Keyword Highlighter", "Autocomplete"))
    val screens: MutableLiveData<List<Screens>> = MutableLiveData(listOf(Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS, Screens.AUTOCOMPLETE_SETTINGS))

    private val _selectedItem: MutableLiveData<Int> = MutableLiveData(0)
    val selectedItem: MutableLiveData<Int> = _selectedItem

    private val _settingsOptionsWidth: MutableLiveData<Float> = MutableLiveData(220f)
    val settingsOptionsWidth: LiveData<Float> = _settingsOptionsWidth

    /**
     * Deletes an autocomplete option and its related entities
     *
     * @param uuid Identifier associated with the configuration to be deleted
     */
    suspend fun deleteConfigs(uuid: String){
        autocompleteRepository.deleteAutocompleteOption(uuid)
        syntaxHighlightRepository.deleteSyntaxHighlightConfig(uuid)
        autocompleteRepository.deleteSelectedAutocompleteOption(uuid = uuid)
    }

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
     * Sets the settings options width using the provided [value]
     *
     * @param value The value to assign
     */
    fun setSettingsOptionsWidth(value: Float){
        // If [(_settingsOptionsWidth + value)] is greater than [200], it allows further changes to the width
        if((_settingsOptionsWidth.value + value) > 200) _settingsOptionsWidth.value += value
    }

}