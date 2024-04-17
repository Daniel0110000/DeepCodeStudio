package com.dr10.settings.ui.viewModels

import com.dr10.common.models.SyntaxHighlightOptionModel
import com.dr10.database.domain.repositories.SyntaxHighlightSettingsRepository
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyntaxHighlightSettingsViewModel(
    private val syntaxHighlightRepository: SyntaxHighlightSettingsRepository
): ViewModel() {

    private val _allSyntaxHighlightConfigs: MutableLiveData<List<SyntaxHighlightOptionModel>> = MutableLiveData(emptyList())
    val allSyntaxHighlightConfigs: LiveData<List<SyntaxHighlightOptionModel>> = _allSyntaxHighlightConfigs

    private val _selectedOptionIndex: MutableLiveData<Int> = MutableLiveData(0)
    val selectedOptionIndex: LiveData<Int> = _selectedOptionIndex

    private val _isExpandColorOptionsList: MutableLiveData<List<Boolean>> = MutableLiveData(emptyList())
    val isExpandColorOptionsList: LiveData<List<Boolean>> = _isExpandColorOptionsList

    private val _codeText: MutableLiveData<String> = MutableLiveData("")
    val codeText: LiveData<String> = _codeText

    init {
        CoroutineScope(Dispatchers.IO).launch {
            syntaxHighlightRepository.getAllSyntaxHighlightConfigs().collect {
                _allSyntaxHighlightConfigs.value = it
                _isExpandColorOptionsList.value = List(_allSyntaxHighlightConfigs.value.size){ false }
            }
        }
    }

    /**
     * Suspended function to update a syntax highlight configuration in the repository
     *
     * @param model The [SyntaxHighlightOptionModel] containing the updated configuration
     */
    suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightOptionModel) =
        syntaxHighlightRepository.updateSyntaxHighlightConfig(model)

    /**
     * Update the selected option index with the specified [index]
     *
     * @param index The new index of the selected option
     */
    fun updateSelectedIndex(index: Int){
        _selectedOptionIndex.value = index
    }

    /**
     * Updates the expansion state for a specified item at the given [index]
     *
     * @param index The index of the item in the list
     * @param value The new expansion state to set
     */
    fun updateIsExpanded(index: Int, value: Boolean){
        // Create a mutable copy of the current list of expansion states
        val currentList = _isExpandColorOptionsList.value.toMutableList()

        // Check if the index is within the valid range
        if(index in 0 until currentList.size){
            currentList[index] = value
            _isExpandColorOptionsList.value = currentList
        }
    }

    /**
     * Updates the content of [_codeText]
     *
     * @param value The content to update
     */
    fun updateCodeTex(value: String){
        _codeText.value = value
    }



}