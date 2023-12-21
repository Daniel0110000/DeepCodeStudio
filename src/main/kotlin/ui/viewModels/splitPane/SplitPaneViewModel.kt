package ui.viewModels.splitPane

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.utilies.Constants
import domain.utilies.DocumentsManager
import java.io.File

class SplitPaneViewModel: ViewModel() {

    private val _currentPath: MutableLiveData<String> = MutableLiveData("${DocumentsManager.getUserHome()}${File.separator}${Constants.DEFAULT_PROJECTS_DIRECTORY_NAME}")
    val currentPath: LiveData<String> = _currentPath

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
     * Sets the [_widthSplittable] using the provided [value]
     *
     * @param value The value to assign
     */
    fun setWidthSplittable(value: Float){
        _widthSplittable.value += value
    }

}