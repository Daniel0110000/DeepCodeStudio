package ui.viewModels.terminal

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.repository.TerminalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class TerminalViewModel(
    private val repository: TerminalRepository
): ViewModel() {

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

    private val _terminalHeight: MutableLiveData<Float> = MutableLiveData(250f)
    val terminalHeight: LiveData<Float> = _terminalHeight

    private val _allCommandHistory: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val allCommandHistory: LiveData<List<String>> = _allCommandHistory

    private val _commandIndex: MutableLiveData<Int> = MutableLiveData(-1)
    val commandIndex: LiveData<Int> = _commandIndex

    // All the formats supported by [NASM]
    private val nasmFormats: List<String> = listOf(
        "bin", "ith", "srec", "aout", "aoutb", "coff", "elf32", "elf64", "elfx32", "as86",
        "obj", "win32", "win64", "ieee", "macho32", "macho64", "dbg", "elf", "macho", "win"
    )

    // All the emulators supported by [ld]
    private val ldEmulators: List<String> = listOf(
        "elf_x86_64", "elf32_x86_64", "elf_i386", "elf_iamcu", "i386pep", "i386pe", "elf64bpf"
    )

    init {
        // Fetches the entire command history and assigns it to the variable [_allCommandHistory]
        _allCommandHistory.value = repository.getAllCommandHistory()
    }

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
     * Generates suggestions based on the current command of [_command]
     *
     * @param input The user input for generating suggestions
     */
    fun getSuggestions(input: String){
        // Get all the files from the current path
        val files = File(_currentDirectory.value).listFiles() ?: return
        // Get the current command
        val currentCommand = _command.value.text

        _suggestions.value = when {
            currentCommand.contains("cd") -> {
                // If the current command contains [cd], we filter all the directories in the current path
                // ... and the directory names that match the value of [input]
                files.filter { it.isDirectory }
                     .filter { input.isEmpty() || it.name.lowercase().contains(input.lowercase()) }
                     .map { it.name }
            }
            currentCommand.contains("ld") -> {
                // If the current command contains [ld], we filter all the .o files in the current path
                // ... and create the ld command to display in suggestions. We also filter the names of the files that match the value of [input]
                files.filter { it.isFile && it.extension == "o" }
                     .flatMap { file ->
                        ldEmulators.map { emulator ->
                            "ld -m $emulator -o ${file.nameWithoutExtension} ${file.name}"
                        }
                     }
                     .filter { it.contains(currentCommand) }
            }
            currentCommand.contains("./") -> {
                // If the current command contains [./], we filter all the executables in the current path and
                // ... also filter all executable names that match the value of [input]
                files.filter { it.isFile && it.extension.isEmpty() }
                     .map { file -> "./${file.name}" }
                     .filter { it.contains(currentCommand) }
            }
            currentCommand.contains("nasm") -> {
                // If the current command contains [nasm], we filter all the .asm or .s files in the current path and
                // ... also filter the files that match the value of [input]. Then, we create the [nasm] command to display in suggestions
                files.filter { it.isFile && (it.extension == "asm" || it.extension == "s") }
                    .flatMap { file ->
                        nasmFormats.map { format ->
                            "nasm -f $format ${file.name} -o ${file.nameWithoutExtension}.o"
                        }
                    }
                    .filter { it.contains(currentCommand) }
            }
            // Return empty list
            else -> emptyList()
        }
    }

    /**
     * Adds the selected command from suggestions to [_command]
     *
     * @param commandSelected The selected command from the suggestions
     */
    fun setSuggestionToInput(commandSelected: String){
        if(_command.value.text.contains("cd")){
            val commandToInsert = "cd $commandSelected"
            _command.value = TextFieldValue(commandToInsert, TextRange(commandToInsert.length))
        } else {
            _command.value = TextFieldValue(commandSelected, TextRange(commandSelected.length))
        }
    }

    /**
     * Inserts the command into the command history and updates the variable [_allCommandHistory]
     *
     * @param command Command to insert
     */
    fun addCommand(command: String){
        CoroutineScope(Dispatchers.IO).launch {
            repository.addCommand(command)
            _allCommandHistory.value = repository.getAllCommandHistory()
        }
    }

    /**
     * Clears the list of suggestions, removing any existing suggestions
     */
    fun clearSuggestions(){
        _suggestions.value = emptyList()
    }

    /**
     * Sets the command index using the provided [value]
     *
     * @param value The value to assign
     */
    fun setCommandIndex(value: Int){
        _commandIndex.value = value
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

    fun setTerminalHeight(value: Float){
        _terminalHeight.value += -value
    }

}