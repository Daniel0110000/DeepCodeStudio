package ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import ui.fileTree.Actions
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.GroupLayout
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * JPanel that allows dragging a JDialog by clicking and dragging on this panel
 *
 * @property dialog The [JDialog] instance that will be moved when dragging this panel
 * @property typeAction The action type associated with this panel, used to set the title
 */
class DragPanel(
    private val dialog: JDialog,
    private val typeAction: Actions
): JPanel()  {

    private var initialClick: Point = Point()

    private val mouseHandler = object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            initialClick = e.point
        }

        override fun mouseDragged(e: MouseEvent) {
            val dialogLocation = dialog.location
            val xMoved = e.x - initialClick.x
            val yMoved = e.y - initialClick.y

            dialog.setLocation(dialogLocation.x + xMoved, dialogLocation.y + yMoved)
        }
    }

    init {
        addMouseListener(mouseHandler)
        addMouseMotionListener(mouseHandler)
        onCreate()
    }

    private fun onCreate() {
        val layout = GroupLayout(this)
        this.layout = layout
        background = java.awt.Color(47,52,63)

        val title = JLabel(typeAction.getTitle()).apply {
            font = ThemeApp.text.fontInterBold()
            foreground = ThemeApp.colors.textColor.toAWTColor()
        }

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(title)
                .addGap(0, 0, Short.MAX_VALUE.toInt())
        )

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(title)
                .addGap(0, 0, Short.MAX_VALUE.toInt())
        )

    }
}