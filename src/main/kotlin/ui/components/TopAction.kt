package ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.extensions.mouseEventListener
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagConstraints
import java.awt.RenderingHints
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.JPanel

class TopAction(
    icon: Icon,
    private val onClickListener: () -> Unit
): JPanel() {

    private var backgroundColor = ThemeApp.awtColors.secondaryColor

    private var _isSelected = false
    var isSelected: Boolean
        get() = _isSelected
        set(value) {
            _isSelected = value

            backgroundColor = if(value) ThemeApp.colors.buttonColor.toAWTColor()
            else ThemeApp.awtColors.secondaryColor

            repaint()
        }

    init {
        isOpaque = false

        mouseEventListener(
            onEnter = {
                if (!_isSelected) backgroundColor = ThemeApp.awtColors.hoverColor
            },
            onExit = { if (!_isSelected) backgroundColor = ThemeApp.awtColors.secondaryColor },
            onClick = { onClickListener() }
        )

        add(
            JLabel(icon),
            GridBagConstraints().apply {
                anchor = GridBagConstraints.CENTER
            }
        )

    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = backgroundColor
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10)

    }
}