package ui.viewModels.terminal

import androidx.compose.ui.text.input.TextFieldValue
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import java.io.File

class TerminalViewModel: ViewModel() {

    private val _results: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val results: LiveData<List<String>> = _results

    private val _commandsExecuted: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val commandsExecuted: LiveData<List<String>> = _commandsExecuted

    private val _directories: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val directories: LiveData<List<String>> = _directories

    private val _currentDirectory: MutableLiveData<String> = MutableLiveData("")
    val currentDirectory: LiveData<String> = _currentDirectory

    private val _isKeyBeingPressed: MutableLiveData<Boolean> = MutableLiveData(false)
    val isKeyBeingPressed: LiveData<Boolean> = _isKeyBeingPressed

    private val _suggestions: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val suggestions: LiveData<List<String>> = _suggestions

    private val _command: MutableLiveData<TextFieldValue> = MutableLiveData(TextFieldValue(""))
    val command: LiveData<TextFieldValue> = _command

    private val _cursorXCoordinates: MutableLiveData<Int> = MutableLiveData(0)
    val cursorXCoordinates: LiveData<Int> = _cursorXCoordinates

    private val _selectedItemIndex: MutableLiveData<Int> = MutableLiveData(0)
    val selectedItemIndex: LiveData<Int> = _selectedItemIndex

    /**
     * Clear the terminal by resetting the results, executed commands, and directories lists
     */
    fun clearTerminal(){
        _results.value = emptyList()
        _commandsExecuted.value = emptyList()
        _directories.value = emptyList()
        clearSuggestions()
    }

    /**
     * Generates suggestions based on the input for commands containing "cd"
     *
     * @param input The user input for generating suggestions
     */
    fun getSuggestions(input: String){
        if(_command.value.text.contains("cd")){
            _suggestions.value = File(_currentDirectory.value).listFiles()
                ?.filter { it.isDirectory }
                ?.filter { input.isEmpty() || it.name.lowercase().contains(input.lowercase()) }
                ?.map { it.name } ?: emptyList()
        }
    }

    /**
     * Clears the list of suggestions, removing any existing suggestions
     */
    fun clearSuggestions(){
        _suggestions.value = emptyList()
    }

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
        if(File(value).exists()) _currentDirectory.value = value
    }

    /**
     * Sets the key being pressed using the provided [value]
     *
     * @param value The value to assign
     */
    fun setIsKeyBeingPressed(value: Boolean){
        _isKeyBeingPressed.value = value
    }

    /**
     * Sets the command using the provided [value]
     *
     * @param value The value to assign
     */
    fun setCommand(value: TextFieldValue){
        _command.value = value
    }

    /**
     * Sets the cursor x coordinates using the provided [value]
     *
     * @param value The value to assign
     */
    fun setCursorXCoordinates(value: Int){
        _cursorXCoordinates.value = value
    }

    /**
     * Sets the selected item index using the provided [value]
     *
     * @param value The value to assign
     */
    fun setSelectedItemIndex(value: Int){
        _selectedItemIndex.value = value
    }
}