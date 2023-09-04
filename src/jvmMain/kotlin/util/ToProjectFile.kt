package util

import java.io.File

/**
 * Converts a File object into a custom ui.fileTree.File object
 *
 * This extension function takes a regular File object and transforms it into a custom
 * ui.fileTree.File object, providing a way to represent file system items in a specific format
 *
 * @return The converted ui.fileTree.File object
 */
fun File.toProjectFile(): ui.fileTree.File = object : ui.fileTree.File {
    override val name: String get() = this@toProjectFile.name
    override val isDirectory: Boolean get() = this@toProjectFile.isDirectory
    override val children: List<ui.fileTree.File>
        get() = this@toProjectFile
            .listFiles{ _, name -> !name.startsWith(".") }
            .orEmpty()
            .map { it.toProjectFile() }

    private val numberOfFiles get() = listFiles()?.size ?: 0

    override val hasChildren: Boolean
        get() = isDirectory && numberOfFiles > 0
    override val filePath: String
        get() = this@toProjectFile.absolutePath

}