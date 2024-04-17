package ui.splitPane.fileTree

import com.dr10.common.utilities.DocumentsManager
import java.io.File

/**
 * Performs file or directory-related actions based on the specified [type], [file], and [name]
 *
 * @param type The type of action to perform, specified by the [Actions] enum
 * @param file The target file or directory on which the action will be performed
 * @param name The new name for the file or directory
 */
fun performAction(type: Actions, file: File, name: String){
    // Checks if the provided new name is not blank before performing any action
    if(name.isNotBlank()){
        when(type){
            Actions.NEW_FILE -> { DocumentsManager.createFile(file.absolutePath, name) }
            Actions.NEW_FOLDER -> { DocumentsManager.createDirectory(file.absolutePath, name) }
            Actions.RENAME -> { DocumentsManager.renameFileOrDirectory(file.absolutePath, name) }
            Actions.NONE -> {}
        }
    }
}