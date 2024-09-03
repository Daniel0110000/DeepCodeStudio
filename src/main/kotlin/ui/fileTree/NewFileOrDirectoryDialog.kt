package ui.fileTree

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import ui.components.DragPanel
import ui.components.TextField
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener
import java.io.File
import javax.swing.GroupLayout
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel

/**
 * Dialog for creating a new file or directory
 *
 * @property window The parent JFrame that this dialog will be centered relative to
 * @property typeAction The type of action being performed
 * @property file The target directory where the new file or folder will be created
 */
class NewFileOrDirectoryDialog(
    private val window: JFrame,
    private val typeAction: Actions,
    private val file: File
): JDialog(window) {

    init { onCreate() }

    private fun onCreate() {
        isUndecorated = true
        setSize(300, 80)
        setLocationRelativeTo(window)
        isModal = false

        val dialogPanel = JPanel().apply {
            background = ThemeApp.colors.secondColor.toAWTColor()
        }

        val layout = GroupLayout(dialogPanel)
        dialogPanel.layout = layout

        val dragPanel = DragPanel(this, typeAction)
        val textField = TextField(typeAction, file) {
            isVisible = false
            dispose()
        }

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addComponent(dragPanel, 300, 300, 300)
                .addComponent(textField, 300, 300, 300)
        )

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(dragPanel, 35, 35, 35)
                .addComponent(textField, 45, 45, 45)
        )


        // Add a listener to close the dialog when it loses focus
        addWindowFocusListener(object : WindowFocusListener {
            override fun windowGainedFocus(e: WindowEvent) {}

            override fun windowLostFocus(e: WindowEvent) {
                isVisible = false
                dispose()
            }

        })

        contentPane.add(dialogPanel)
        isVisible = true
    }
}