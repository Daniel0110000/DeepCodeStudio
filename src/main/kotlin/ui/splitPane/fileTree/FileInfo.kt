package ui.splitPane.fileTree

import java.io.File

data class FileInfo(
    val file: File,
    val depthLevel: Int,
    var isExpanded: Boolean
)
