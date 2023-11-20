package ui.viewModels.splitPane

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.utilies.Constants
import domain.utilies.DocumentsManager
import ui.editor.tabs.TabsState

class SplitPaneViewModel: ViewModel() {

    val tabState: MutableLiveData<TabsState> = MutableLiveData(TabsState())

    private val _currentPath: MutableLiveData<String> = MutableLiveData("${DocumentsManager.getUserHome()}/${Constants.DEFAULT_PROJECTS_DIRECTORY_NAME}")
    val currentPath: LiveData<String> = _currentPath

    private val _isOpenTerminal: MutableLiveData<Boolean> = MutableLiveData(false)
    val isOpenTerminal: LiveData<Boolean> = _isOpenTerminal

    private val _isCollapseSplitPane: MutableLiveData<Boolean> = MutableLiveData(false)
    val isCollapseSplitPane: LiveData<Boolean> = _isCollapseSplitPane

    private val _widthSplittable: MutableLiveData<Float> = MutableLiveData(300f)
    val widthSplittable: LiveData<Float> = _widthSplittable

    /**
     * Sets the [_currentPath] the provided [value]
     *
     * @param value The value to assign
     */
    fun setPath(value: String){
        _currentPath.value = value
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
     * Sets the [_isCollapseSplitPane] using the provided [value]
     *
     * @param value The value to assign
     */
    fun setIsCollapseSplitPane(value: Boolean){
        _isCollapseSplitPane.value = value
    }

    /**
     * Sets the [_widthSplittable] using the provided [value]
     *
     * @param value The value to assign
     */
    fun setWidthSplittable(value: Float){
        _widthSplittable.value += value
    }

}