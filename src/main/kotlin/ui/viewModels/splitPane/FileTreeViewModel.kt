package ui.viewModels.splitPane

import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import com.dr10.editor.ui.tabs.TabModel
import com.dr10.editor.ui.tabs.TabsState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.splitPane.fileTree.FileInfo
import ui.splitPane.fileTree.FileObserver
import java.io.File

class FileTreeViewModel(
    private val repository: AutocompleteSettingsRepository,
    private val path: String,
    private val tabsState: TabsState
): ViewModel() {

    private val rootFile = FileInfo(File(path), 0, true)

    private val _listFiles: MutableLiveData<List<FileInfo>> = MutableLiveData(listOf(rootFile) + expandDirectory(rootFile))
    val listFiles: LiveData<List<FileInfo>> = _listFiles

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            // Register callbacks for file changes using the [FileObserver] class
            FileObserver(path).registerCallbacksForChanges(
                // Callback when a new file or directory is created
                onCreate = {
                    // Check if the [FileInfo] list already contains a directory with the same parent
                    if(_listFiles.value.contains(FileInfo(File(it.parent), calculateRelativeDirectoryDepth(it.parent), true))){
                        // Add the created file to the list of FileInfo with appropriate depth
                        _listFiles.value = _listFiles.value.plus(FileInfo(it, calculateRelativeDirectoryDepth(it.absolutePath), false))
                    } },
                // Callback when a file or directory is deleted
                onDelete = { file ->
                    val filePath = file.absolutePath
                    // Remove the deleted file from the list of FileInfo and its subdirectories
                    _listFiles.value = _listFiles.value.filter { it.file != file && it.file.absolutePath != filePath }

                    // If the deleted file is open, its tab is closed
                    tabsState.closeTab(TabModel(file.name, filePath)){ }

                    if(file.extension == "asm" || file.extension == "s"){
                        // If the extension of the deleted file is 'asm' or 's', delete the selected autocomplete option from the database
                        scope.launch { repository.deleteSelectedAutocompleteOption(asmFilePath = file.absolutePath) }
                    }
                }
            )
        }
    }

    /**
     * Toggles the expansion state of a directory or opens an ASM file in the editor tab
     *
     * @param item The FileInfo representing the directory or file to be opened or closed
     */
    fun toggleDirectoryExpansion(item: FileInfo){
        if(item.file.isDirectory){
            val expandedFiles = expandDirectory(item)
            if(item.isExpanded){
                // Close the directory by setting the [isExpanded] flag to false and removing its contents
                item.isExpanded = false
                val directoryPath = item.file.absolutePath
                _listFiles.value = _listFiles.value.filterNot { fileInfo ->
                    fileInfo.file != item.file && fileInfo.file.absolutePath.startsWith(directoryPath)
                }
            } else {
                // Open the directory if it has contents and update the list of displayed files
                item.file.listFiles()?.let { if(it.isNotEmpty()) item.isExpanded = true }
                if(!_listFiles.value.containsAll(expandedFiles)) _listFiles.value += expandedFiles
            }
        } else{
            // If it's an ASM file, open it in the editor tab using [tabsState.openTab]
            if(item.file.extension == "asm" ||
                item.file.extension == "s") tabsState.openTab(item.file)
        }
    }

    /**
     * Expands a directory by creating a list of [FileInfo] objects fot its contents
     *
     * @param fileItem The [FileInfo] representing the directory to expand
     * @return A list of [FileInfo] objects for the contents of the directory
     */
    private fun expandDirectory(fileItem: FileInfo): List<FileInfo> {
        val directoryContents = File(fileItem.file.absolutePath).listFiles()
        return directoryContents?.map { FileInfo(it, fileItem.depthLevel + 1, false) } ?: emptyList()
    }

    /**
     * Calculates the relative depth of a directory within a base path by comparing their common parts
     *
     * @param targetPath The target directory's path to calculate depth for
     * @return The depth of the target directory relative to the base path
     */
    private fun calculateRelativeDirectoryDepth(targetPath: String): Int{
        val basePathParts = path.split(File.separator)
        val targetPathParts = targetPath.split(File.separator)

        val commonParts = basePathParts.zip(targetPathParts).takeWhile { (a, b) -> a == b }

        return targetPathParts.size - commonParts.size
    }

}