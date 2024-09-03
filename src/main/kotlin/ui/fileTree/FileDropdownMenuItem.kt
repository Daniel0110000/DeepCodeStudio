package ui.fileTree

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.GroupLayout
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel

class FileDropdownMenuItem(
    private val optionLabel: String,
    private val imageIcon: ImageIcon,
    private val onClickListener: () -> Unit
): JPanel() {


    private var backgroundColor = ThemeApp.colors.secondColor.toAWTColor()

    init { onCreate() }

    private fun onCreate() {
        val layout = GroupLayout(this)
        this.layout = layout
        this.preferredSize = Dimension(200, 35)
        this.isOpaque = false

        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {
                backgroundColor = ThemeApp.colors.hoverTab.toAWTColor()
                repaint()
            }

            override fun mouseExited(e: MouseEvent?) {
                backgroundColor = ThemeApp.colors.secondColor.toAWTColor()
                repaint()
            }

            override fun mouseClicked(e: MouseEvent?) {
                onClickListener()
            }
        })

        val label = JLabel(optionLabel)
        label.font = ThemeApp.text.fontInterRegular(13f)

        val optionIcon = JLabel(imageIcon)

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGap(15)
                .addComponent(optionIcon)
                .addGap(8)
                .addComponent(label)

        )

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(0, Short.MAX_VALUE.toInt(), Short.MAX_VALUE.toInt())
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent(optionIcon)
                        .addComponent(label)
                )
                .addGap(0, Short.MAX_VALUE.toInt(), Short.MAX_VALUE.toInt())
        )
    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = backgroundColor
        graphics.fillRect(0, 0, width - 1, height - 1)
    }

}