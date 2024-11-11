package com.dr10.terminal.ui.viewModel

import com.dr10.common.utilities.Constants
import com.dr10.common.utilities.DocumentsManager
import com.dr10.terminal.model.ShellInfo
import com.dr10.terminal.utils.ShellUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class TerminalViewModel {

    private val _state = MutableStateFlow(TerminalState())
    val state: StateFlow<TerminalState> = _state.asStateFlow()

    /**
     * Data class for terminal state
     *
     * @property availableShells List of available shells in the system
     * @property terminals List of open terminals
     * @property currentShellSelected The current shell selected in the JComboBox
     * @property currentFileTreePath The current file path in the file tree
     */
    data class TerminalState(
        val availableShells: List<ShellInfo> = emptyList(),
        val terminals: List<ShellInfo> = emptyList(),
        val currentShellSelected: ShellInfo = ShellUtils.getDefaultShell(),
        val currentFileTreePath: String = "${DocumentsManager.getUserHome()}/${Constants.DEFAULT_PROJECTS_DIRECTORY_NAME}"
    )

    /**
     * Retrieves all the available shells in the system
     */
    init {
        _state.value = _state.value.copy(
            availableShells = ShellUtils.getAvailableShells(),
        )
    }

    /**
     * Opens the initial terminal in the default shell if there are no open terminals
     */
    fun openInitialTerminal() {
        if (_state.value.terminals.isEmpty()) {
            _state.value = _state.value.copy(
                terminals = listOf(
                    ShellUtils.getDefaultShell().copy(
                        terminalID = UUID.randomUUID().toString(),
                        path = _state.value.currentFileTreePath
                    )
                )
            )
        }
    }

    /**
     * Opens a new terminal in the current shell selected
     */
    fun openNewTerminal() {
        _state.value = _state.value.copy(
            terminals = _state.value.terminals + _state.value.currentShellSelected
                .copy(
                    terminalID = UUID.randomUUID().toString(),
                    path = _state.value.currentFileTreePath
                )
        )
    }

    /**
     * Closes the terminal with the given [ShellInfo.terminalID]
     *
     * @param shellInfo The shell info of the terminal to be closed
     */
    fun closeTerminal(shellInfo: ShellInfo) {
        _state.value = _state.value.copy(
            terminals = _state.value.terminals.filterNot { it.terminalID == shellInfo.terminalID }
        )
    }

    fun setCurrentShellSelected(shellInfo: ShellInfo) {
        _state.value = _state.value.copy(
            currentShellSelected = shellInfo
        )
    }

    fun setCurrentFileTreePath(path: String) {
        _state.value = _state.value.copy(
            currentFileTreePath = path
        )
    }

}