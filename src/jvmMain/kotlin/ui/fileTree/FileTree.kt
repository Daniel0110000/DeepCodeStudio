package ui.fileTree

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.editor.tabs.TabsState
import java.io.File

class FileTree(private val path: String, private val tabsState: TabsState) {

    // Represents the root directory FileInfo with depth 0 and as expanded
    private val rootFile = FileInfo(File(path), 0, true)
    // Initializes the list of files starting with the root directory and its expanded subdirectories.
    val listFiles = mutableStateOf(listOf(rootFile) + expandDirectory(rootFile))

    init {
        CoroutineScope(Dispatchers.IO).launch {
            // Register callbacks for file changes using the [FileObserver] class
            FileObserver(path).registerCallbacksForChanges(
                // Callback when a new file or directory is created
                onCreate = {
                    // Check if the [FileInfo] list already contains a directory with the same parent
                    if(listFiles.value.contains(FileInfo(File(it.parent), calculateRelativeDirectoryDepth(it.parent), true))){
                        // Add the created file to the list of FileInfo with appropriate depth
                        listFiles.value = listFiles.value.plus(FileInfo(it, calculateRelativeDirectoryDepth(it.absolutePath), false))
                    } },
                // Callback when a file or directory is deleted
                onDelete = { file ->
                    val filePath = file.absolutePath
                    // Remove the deleted file from the list of FileInfo and its subdirectories
                    listFiles.value = listFiles.value.filter { it.file != file && !it.file.absolutePath.contains(filePath) }
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
                listFiles.value = listFiles.value.filterNot { fileInfo ->
                    fileInfo.file != item.file && fileInfo.file.absolutePath.startsWith(directoryPath)
                }
            } else {
                // Open the directory if it has contents and update the list of displayed files
                item.file.listFiles()?.let { if(it.isNotEmpty()) item.isExpanded = true }
                if(!listFiles.value.containsAll(expandedFiles)){
                    listFiles.value = listFiles.value + expandedFiles
                }
            }
        } else{
            // If it's an ASM file, open it in the editor tab using [tabsState.openTab]
            if(item.file.extension == "asm") tabsState.openTab(item.file)
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
        val basePathParts = path.split("/")
        val targetPathParts = targetPath.split("/")

        val commonParts = basePathParts.zip(targetPathParts).takeWhile { (a, b) -> a == b }

        return targetPathParts.size - commonParts.size
    }

}