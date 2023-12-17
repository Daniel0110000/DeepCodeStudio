package ui.editor.tabs

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import java.io.File

class TabsState {

    // A mutable list to hold the open editor tabs
    var tabs = mutableStateListOf<TabModel>()
        private set

    // Mutable state that holds the file path of a recently closed tab
    val closedTabFilePath = mutableStateOf("")

    /**
     * Opens a new tav for the given file
     *
     * @param file The file to be opened in a new tab
     */
    fun openTab(file: File){
        val newTab = TabModel(file.name, file.absolutePath)
        if(!tabs.contains(newTab)) tabs.add(newTab)
    }

    /**
     * Close the specified tab
     *
     * @param tabModel The EditorTabsModel representing the tab to be closed
     * @param close Callback to notify when a tab has been closed
     */
    fun closeTab(tabModel: TabModel, close: () -> Unit){
        if(tabs.contains(tabModel)){
            closedTabFilePath.value = tabModel.filePath
            close()
            tabs.remove(tabModel)
        }
    }

}