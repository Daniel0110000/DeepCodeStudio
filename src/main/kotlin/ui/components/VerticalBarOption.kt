package ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.ImageResourceUtils
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * A [JPanel] that represents an individual option in the [VerticalBarOptions]
 *
 * @param cornerRadius The radius of the corners for the background round rectangle
 * @param enteredBackgroundColor The background color when the mouse is over the component
 * @param exitedBackgroundColor The background color when the mouse is not over the component
 * @param iconResourcePath The path to the icon resource used for this option
 * @param iconWidth The width of the icon
 * @param iconHeight The height of the icon
 * @param onClickListener The callback function to be invoked when the component is clicked
 */
class VerticalBarOption(
    private val cornerRadius: Int,
    private var enteredBackgroundColor: Color,
    private var exitedBackgroundColor: Color,
    private val iconResourcePath: String,
    private val iconWidth: Int = 23,
    private val iconHeight: Int = 23,
    private val onClickListener: () -> Unit
): JPanel() {

    var backgroundColor = exitedBackgroundColor

    // Internal state to track of the option is selected
    private var _isSelected = false
    var isSelected: Boolean
        get() = _isSelected
        set(value) {
            _isSelected = value

            // Update background color based on selection state
            backgroundColor = if(value) ThemeApp.colors.buttonColor.toAWTColor()
            else exitedBackgroundColor

            repaint()
        }

    init { onCreate() }

    private fun onCreate() {
        layout = GridBagLayout()
        preferredSize = Dimension(30, 30)
        isOpaque = false

        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(event: MouseEvent) {
                // Change background color on mouse enter if not selected
                if(!isSelected) {
                    backgroundColor = enteredBackgroundColor
                    repaint()
                }
            }

            override fun mouseExited(event: MouseEvent) {
                // Revert background color on mouse exit if not selected
                if(!isSelected) {
                    backgroundColor = exitedBackgroundColor
                    repaint()
                }
            }

            override fun mouseClicked(event: MouseEvent) {
                onClickListener()
            }

        })

        add(
            JLabel(ImageResourceUtils.loadResourceImage(iconResourcePath, iconWidth, iconHeight)),
            GridBagConstraints().apply {
                anchor = GridBagConstraints.CENTER
            }
        )
    }

    /**
     * Paints the component with the current background color and rounded corners
     *
     * @param graphics The Graphics context to be used for painting
     */
    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = backgroundColor
        graphics.fillRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius)

    }
}