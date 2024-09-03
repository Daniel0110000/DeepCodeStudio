package ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import ui.fileTree.Actions
import ui.fileTree.performAction
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.io.File
import javax.swing.JTextField
import javax.swing.border.EmptyBorder

/**
 * [JTextField] for handling text input related to file or directory actions like renaming or creating new files
 *
 * @property typeAction The action type
 * @property file The target directory where the new file or folder will be created
 * @property closeDialog A callback function to close the dialog one the action is performed
 */
class TextField(
    private val typeAction: Actions,
    private val file: File,
    private val closeDialog: () -> Unit
): JTextField() {

    init {
        border = EmptyBorder(5, 5, 5, 5)
        caretColor = ThemeApp.colors.textColor.toAWTColor()
        background = ThemeApp.colors.secondColor.toAWTColor()
        selectionColor = ThemeApp.colors.buttonColor.toAWTColor()
        font = ThemeApp.text.fontInterRegular(12f)

        text = if (typeAction == Actions.RENAME) file.name else ""

        // Add a key listener to handle the Enter key press event
        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    val currentName = text
                    if(currentName.isNotBlank()) {
                        performAction(typeAction, file, currentName)
                        closeDialog()
                    }
                }
            }
        })
    }
}