package ui.viewModels

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import ui.editor.tabs.TabsState

class CodeEditorViewModel: ViewModel() {

    val tabState: MutableLiveData<TabsState> = MutableLiveData(TabsState())

    private val _isCollapseSplitPane: MutableLiveData<Boolean> = MutableLiveData(false)
    val isCollapseSplitPane: LiveData<Boolean> = _isCollapseSplitPane

    private val _isOpenTerminal: MutableLiveData<Boolean> = MutableLiveData(false)
    val isOpenTerminal: LiveData<Boolean> = _isOpenTerminal

    private val _isOpenSettings: MutableLiveData<Boolean> = MutableLiveData(false)
    val isOpenSettings: LiveData<Boolean> = _isOpenSettings

    /**
     * Sets the [_isCollapseSplitPane] using the provided [value]
     *
     * @param value The value to assign
     */
    fun setIsCollapseSplitPane(value: Boolean){
        _isCollapseSplitPane.value = value
    }


    /**
     * Sets the [_isOpenTerminal] using the provided [value]
     *
     * @param value The value to assign
     */
    fun setIsOpenTerminal(value: Boolean){
        _isOpenTerminal.value = value
    }

    /**
     * Sets the [_isOpenSettings] using the provided [value]
     *
     * @param value The value to assign
     */
    fun setIsOpenSettings(value: Boolean){
        _isOpenSettings.value = value
    }

}