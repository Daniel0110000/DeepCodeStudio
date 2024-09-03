package ui.fileTree

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.DocumentsManager
import java.io.File
import javax.swing.GroupLayout
import javax.swing.JFrame
import javax.swing.JPopupMenu

class FileDropdownMenu(
    private val window: JFrame,
    private val file: File
): JPopupMenu() {

    private val preferredSize = GroupLayout.PREFERRED_SIZE

    init { onCreate() }

    private fun onCreate() {
        background = ThemeApp.colors.secondColor.toAWTColor()
        val groupLayout = GroupLayout(this)
        layout = groupLayout

        val newFileOption = FileDropdownMenuItem("New File", AppIcons.fileIcon){
            NewFileOrDirectoryDialog(window, Actions.NEW_FILE, file)
            isVisible = false
        }

        val newFolderOption = FileDropdownMenuItem("New Folder", AppIcons.folderIcon){
            NewFileOrDirectoryDialog(window, Actions.NEW_FOLDER, file)
            isVisible = false
        }
        val renameOption = FileDropdownMenuItem("Rename", AppIcons.renameIcon){
            NewFileOrDirectoryDialog(window, Actions.RENAME, file)
            isVisible = false
        }
        val deleteOption = FileDropdownMenuItem("Delete", AppIcons.deleteIcon){
            DocumentsManager.deleteFileOrDirectory(file)
            isVisible = false
        }

        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup().apply {
                if (file.isDirectory) {
                    addComponent(newFileOption, preferredSize, preferredSize, preferredSize)
                    addComponent(newFolderOption, preferredSize, preferredSize, preferredSize)
                }
                addComponent(renameOption, preferredSize, preferredSize, preferredSize)
                addComponent(deleteOption, preferredSize, preferredSize, preferredSize)
            }
        )

        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addGap(8)
                .apply {
                    if (file.isDirectory) {
                        addComponent(newFileOption, preferredSize, preferredSize, preferredSize)
                        addComponent(newFolderOption, preferredSize, preferredSize, preferredSize)
                    }
                    addComponent(renameOption, preferredSize, preferredSize, preferredSize)
                    addComponent(deleteOption, preferredSize, preferredSize, preferredSize)
                }
                .addGap(8)
        )

    }

}