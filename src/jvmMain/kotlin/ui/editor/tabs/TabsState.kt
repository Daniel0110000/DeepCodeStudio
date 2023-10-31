package ui.editor.tabs

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import java.io.File

class TabsState {

    // A mutable list to hold the open editor tabs
    var tabs = mutableStateListOf<EditorTabsModel>()
        private set

    // Mutable state that holds the file path of a recently closed tab
    val closedTabFilePath = mutableStateOf("")

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
     * @param index A function to handle the index of the close tab
     */
    fun closeTab(editorTabsModel: EditorTabsModel, index: (Int) -> Unit){
        if(tabs.contains(editorTabsModel)){
            closedTabFilePath.value = editorTabsModel.filePath
            index(tabs.indexOf(editorTabsModel))
            tabs.remove(editorTabsModel)
        }
    }

}