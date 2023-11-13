package ui.viewModels.terminal

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.utilies.DocumentsManager

class TerminalViewModel: ViewModel() {

    private val _results: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val results: LiveData<List<String>> = _results

    private val _commandsExecuted: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val commandsExecuted: LiveData<List<String>> = _commandsExecuted

    private val _directories: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val directories: LiveData<List<String>> = _directories

    private val _currentDirectory: MutableLiveData<String> = MutableLiveData(DocumentsManager.getUserHome())
    val currentDirectory: LiveData<String> = _currentDirectory

    private val _isKeyBeingPressed: MutableLiveData<Boolean> = MutableLiveData(false)
    val isKeyBeingPressed: LiveData<Boolean> = _isKeyBeingPressed

    /**
     * Sets the result using the provided [value]
     *
     * @param value The value to assign
     */
    fun setResult(value: String){
        _results.value = _results.value.plus(value)
    }

    /**
     * Sets the directory using the provided [value]
     *
     * @param value The value to assign
     */
    fun setDirectory(value: String){
        _directories.value = _directories.value.plus(value)
    }

    /**
     * Sets the command executed using the provided [value]
     *
     * @param value The value to assign
     */
    fun setCommandExecuted(value: String){
        _commandsExecuted.value = _commandsExecuted.value.plus(value)
    }

    /**
     * Sets the current directory using the provided [value]
     *
     * @param value The value to assign
     */
    fun setCurrentDirectory(value: String){
        _currentDirectory.value = value
    }

    /**
     * Sets the key being pressed using the provided [value]
     *
     * @param value The value to assign
     */
    fun setIsKeyBeingPressed(value: Boolean){
        _isKeyBeingPressed.value = value
    }

}