package ui.fileTree

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ExpandableFile(
    val file: File,
    val level: Int
) {

    // List of children for this file, starts as an empty list
    var children: List<ExpandableFile> by mutableStateOf(emptyList())

    // Property indicating whether the file can be expanded
    val canExpand: Boolean get() = file.hasChildren

    /**
     * Toggles the expansion state of the current file node
     *
     * If the children list is empty, this function expands the node by populating the
     * children list and sorting them
     * If the children list is not empty, it collapse the node by setting the children
     * list to an empty list
     */
    fun toggleExpanded(){
        children = if(children.isEmpty()){
            file.children
                .map { ExpandableFile(it, level + 1) }
                .sortedWith(compareBy({it.file.isDirectory}, {it.file.name}))
                .sortedBy { !it.file.isDirectory }
        } else emptyList()
    }

}