package ui.viewModels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CodeEditorViewModel {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(CodeEditorState())
    val state: StateFlow<CodeEditorState> = _state.asStateFlow()

    /**
     * Data class for the state of the code editor
     *
     * @property isCollapseSplitPane Indicates if the split pane is collapse
     * @property isOpenTerminal Indicated if the terminal is open
     * @property isOpenSettings Indicated if the settings is open
     */
    data class CodeEditorState(
        val currentPathSelected: String = "",
        val isCollapseSplitPane: Boolean = false,
        val isOpenTerminal: Boolean = false,
        val isOpenSettings: Boolean = false
    )

    /**
     * Sets the [CodeEditorState.isCollapseSplitPane] using the provided [value]
     *
     * @param value The value to assign
     */
    fun setIsCollapseSplitPane(value: Boolean) {
        coroutineScope.launch {
            _state.update { it.copy(
                isCollapseSplitPane = value
            ) }
        }
    }

    /**
     * Sets the [CodeEditorState.isOpenSettings] using the provided [value]
     *
     * @param value The value to assign
     */
    fun setIsOpenSettings(value: Boolean) {
        coroutineScope.launch {
            _state.update { it.copy(
                isOpenSettings = value
            ) }
        }
    }

    /**
     * Sets the [CodeEditorState.isOpenTerminal] using the provided [value]
     *
     * @param value The value to assign
     */
    fun setIsOpenTerminal(value: Boolean) {
        coroutineScope.launch {
            _state.update { it.copy(
                isOpenTerminal = value
            ) }
        }
    }

    /**
     * Sets the [CodeEditorState.currentPathSelected] using the provided [path]
     *
     * @param path The path to assign
     */
    fun setCurrentPath(path: String) {
        coroutineScope.launch {
            _state.update { it.copy(
                currentPathSelected = path
            ) }
        }
    }

}