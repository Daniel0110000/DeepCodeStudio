package com.dr10.common.ui.notification

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import java.awt.Cursor
import java.awt.Dimension
import java.awt.event.MouseAdapter
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.Timer

class Notification(
    private val notificationMessage: String,
    private val notificationType: NotificationType,
    private val onDismiss: (Notification) -> Unit = {}
): JDialog() {

    private val notificationPanel: JPanel = JPanel()
    private var timer: Timer? = null

    init { onCreate() }

    private fun onCreate() {
        isAlwaysOnTop = true
        isUndecorated = true

        val notificationLayout = GroupLayout(notificationPanel)

        notificationPanel.apply {
            layout = notificationLayout
            preferredSize = Dimension(300, 60)
            background = ThemeApp.awtColors.secondaryColor
            border = BorderFactory.createLineBorder(ThemeApp.awtColors.hoverColor)
        }

        val infoIcon = JLabel(
            when (notificationType) {
                NotificationType.SUCCESS -> AppIcons.successIcon
                NotificationType.ERROR -> AppIcons.errorIcon
                NotificationType.INFO -> AppIcons.infoIcon
                NotificationType.WARNING -> AppIcons.warningIcon
            }
        )

        val messageLabel = messageLabel(notificationMessage)

        val closeIcon = JLabel(AppIcons.closeIcon).apply {
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            addMouseListener(object: MouseAdapter() {
                override fun mouseClicked(e: java.awt.event.MouseEvent?) {
                    onDismiss(this@Notification)
                    timer?.stop()
                }
            })
        }

        notificationLayout.setHorizontalGroup(
            notificationLayout.createSequentialGroup()
                .addGap(7)
                .addComponent(infoIcon)
                .addGap(5)
                .addComponent(messageLabel, 0, 0, Short.MAX_VALUE.toInt())
                .addGap(5)
                .addComponent(closeIcon)
                .addGap(7)
        )

        notificationLayout.setVerticalGroup(
            notificationLayout.createSequentialGroup()
                .addGap(7)
                .addGroup(
                    notificationLayout.createParallelGroup()
                        .addComponent(infoIcon)
                        .addComponent(messageLabel, 0, 0, Short.MAX_VALUE.toInt())
                        .addComponent(closeIcon)
                )
                .addGap(7)
        )

        contentPane.add(notificationPanel)
        pack()
    }

    /**
     * Creates the message label for the notification using a [JTextArea] for multiline text
     *
     * @param msg The message to be displayed in the label
     */
    private fun messageLabel(msg: String): JScrollPane = JScrollPane(
        JTextArea(msg).apply {
            isEditable = false
            border = null
            isFocusable = false
            wrapStyleWord = true
            lineWrap = true
            setCursor(null)
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
            background = ThemeApp.awtColors.secondaryColor
        }
    ).apply {
        border = null
        verticalScrollBar.setUI(CustomScrollBar(ThemeApp.awtColors.secondaryColor))
    }
}