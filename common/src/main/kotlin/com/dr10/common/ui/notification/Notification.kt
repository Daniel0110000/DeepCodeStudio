package com.dr10.common.ui.notification

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseAdapter
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JWindow
import javax.swing.Timer

/**
 *
 * @param parentWindow The parent window to position the notification
 * @param notificationMessage The message to be displayed in the notification
 * @param notificationType The type of notification
 * @param xMargin The horizontal margin from the right edge of the parent window
 * @param yMargin The vertical margin from the bottom edge of the parent window
 * @param isAutoDismiss Whether the notification should automatically dismiss after a specified delay
 * @param delay The delay in milliseconds for auto-dismissing the notification
 * @param onDismiss Callback function invoked when the notification is dismissed
 */
class Notification(
    private val parentWindow: JFrame,
    private val notificationMessage: String,
    private val notificationType: NotificationType,
    private val xMargin: Int = 0,
    private val yMargin: Int = 0,
    private val isAutoDismiss: Boolean = false,
    private val delay: Int = 3000,
    private val onDismiss: () -> Unit = {}
): JWindow() {

    private val notificationPanel: JPanel = JPanel()

    init { onCreate() }

    private fun onCreate() {
        // Ensure notification stays visible above other windows
        isAlwaysOnTop = true

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
                    dismiss()
                    onDismiss()
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

        // Position the notification and configure auto-dismiss if enabled
        setPosition()
        setupAutoDismiss()
    }

    /**
     * Sets up auto-dismiss functionality if enabled
     */
    private fun setupAutoDismiss() {
        if (isAutoDismiss) {
            Timer(delay) { dismiss() }.apply {
                isRepeats = false
                start()
            }
        }
    }

    /**
     * Dismisses the notification window
     */
    private fun dismiss() {
        dispose()
        isVisible = false
    }

    /**
     * Calculates ans sets the position of the notification window relative to the parent window, considering the specified margins
     */
    private fun setPosition() {
        val windowLocation: Point = parentWindow.locationOnScreen
        val x = windowLocation.x + parentWindow.width - xMargin - notificationPanel.width
        val y = windowLocation.y + parentWindow.height - yMargin - notificationPanel.height
        setLocation(x, y)
    }
}