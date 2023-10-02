package ui.editor.tabs

import androidx.compose.runtime.mutableStateListOf
import java.io.File

class TabsState {

    // A mutable list to hold the open editor tabs
    var tabs = mutableStateListOf<EditorTabsModel>()
        private set


    /**
     * Opens a new tav for the given file
     *
     * @param file The file to be opened in a new tab
     */
    fun openTab(file: File){
        val newTab = EditorTabsModel(file.name, file.absolutePath)
        if(!tabs.contains(newTab)) tabs.add(newTab)
    }

    /**
     * Close the specified tab
     *
     * @param editorTabsModel The EditorTabsModel representing the tab to be closed
     */
    fun closeTab(editorTabsModel: EditorTabsModel){
        if(tabs.contains(editorTabsModel)) tabs.remove(editorTabsModel)
    }

}