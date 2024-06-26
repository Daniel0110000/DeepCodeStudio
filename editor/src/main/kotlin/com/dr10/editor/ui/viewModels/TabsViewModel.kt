package com.dr10.editor.ui.viewModels

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel

class TabsViewModel: ViewModel() {

    private val _tabSelected: MutableLiveData<String> = MutableLiveData("")
    val tabSelected: LiveData<String> = _tabSelected

    private val _previousTabCount: MutableLiveData<Int> = MutableLiveData(0)
    val previousTabCount: LiveData<Int> = _previousTabCount

    private val _closedTabFilePath: MutableLiveData<String> = MutableLiveData("")
    val closedTabFilePath: LiveData<String> = _closedTabFilePath

    /**
     * Sets the tab selected using the provided [value]
     *
     * @param value The value to assign
     */
    fun setTabSelected(value: String){
        if(value != _tabSelected.value){
            _tabSelected.value = value
        }
    }

    /**
     * Sets the previous tab count using the provided [value]
     *
     * @param value The value to assign
     */
    fun setPreviousTabCount(value: Int){
        _previousTabCount.value = value
    }

    /**
     * Sets the closed tab file path using the provided [value]
     *
     * @param value The value to assign
     */
    fun setClosedTabFilePath(value: String){
        _closedTabFilePath.value = value
    }

}