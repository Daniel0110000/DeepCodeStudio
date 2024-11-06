package ui.viewModels

import com.dr10.common.utilities.Constants
import com.dr10.common.utilities.DocumentsManager
import com.dr10.database.domain.repositories.EditorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class FileTreeViewModel(
    private val editorRepository: EditorRepository
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(FileTreeState())
    val state: StateFlow<FileTreeState> = _state.asStateFlow()

    /**
     * Data class for the state of the file tree
     *
     * @property currentPath The current path for the file tree
     */
    data class FileTreeState(
        val currentPath: String = "${DocumentsManager.getUserHome()}/${Constants.DEFAULT_PROJECTS_DIRECTORY_NAME}",
    )

    fun deleteSelectedConfig(file: File) {
        coroutineScope.launch {
            if (file.name.endsWith(".s") || file.name.endsWith(".asm")) {
                editorRepository.deleteSelectedConfig(asmFilePath = file.absolutePath)
            }
        }

    }

    /**
     * Sets the [FileTreeState.currentPath] using the provided [value]
     *
     * @param value The valur to assign
     */
    fun setCurrentPath(value: String) {
        coroutineScope.launch {
            _state.update { it.copy(
                currentPath = value
            ) }
        }
    }

}