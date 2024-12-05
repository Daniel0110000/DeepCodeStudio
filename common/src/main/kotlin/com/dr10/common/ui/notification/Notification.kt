package com.dr10.common.ui.notification

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import java.awt.Cursor
import java.awt.Dimension
import java.awt.event.MouseAdapter
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
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

        val messageLabel = JLabel(notificationMessage).apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
        }

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
                .addComponent(messageLabel)
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(closeIcon)
                .addGap(7)
        )

        notificationLayout.setVerticalGroup(
            notificationLayout.createSequentialGroup()
                .addGap(7)
                .addGroup(
                    notificationLayout.createParallelGroup()
                        .addComponent(infoIcon)
                        .addComponent(messageLabel)
                        .addComponent(closeIcon)
                )
                .addGap(7)
        )

        contentPane.add(notificationPanel)
        pack()
    }
}