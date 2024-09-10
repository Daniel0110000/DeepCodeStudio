package com.dr10.editor.ui.viewModels

import com.dr10.editor.ui.tabs.TabModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class TabsViewModel {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(TabsState())
    val state: StateFlow<TabsState> = _state.asStateFlow()

    /**
     * Data clas for the state of the Tabs
     *
     * @property tabs List of currently open tabs
     */
    data class TabsState(
        val tabs: List<TabModel> = emptyList(),
    )


    /**
     * Opens a new tab with the specified file
     *
     * @param file The file to open in a new tab
     */
    fun openTab(file: File) {
        coroutineScope.launch {
            val newTab = TabModel(file.name, file.absolutePath)
            _state.update { state -> state.copy(
                tabs = state.tabs + newTab
            ) }
        }
    }

    /**
     * Closes a tba specified by the [TabModel]
     *
     * @param tab The [TabModel] of the tab to close
     */
    fun closeTab(tab: TabModel) {
        coroutineScope.launch {
            _state.update { state -> state.copy(
                tabs = state.tabs.filterNot { it.filePath == tab.filePath }
            ) }
        }
    }
}