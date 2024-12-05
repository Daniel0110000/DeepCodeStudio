package com.dr10.common.ui.notification

import java.awt.Point
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.Timer

class NotificationManager(private val parentWindow: JFrame){

    private val notifications = mutableListOf<Notification>()
    private var currentY = 20

    private var timer: Timer? = null

    /**
     * Shows the notification on the screen
     *
     * @param message The message to be displayed in the notification
     * @param type The type of the notification
     * @param isAutoDismiss Whether the notification should be automatically dismissed
     * @param delay The delay in milliseconds before the notification is dismissed
     * @param xMargin The horizontal margin to be added to the notification
     * @param yMargin The vertical margin to be added to the notification
     */
    fun show(
        message: String,
        type: NotificationType,
        isAutoDismiss: Boolean = false,
        delay: Int = 5000,
        xMargin: Int = 0,
        yMargin: Int = 0
    ) {
        val notification = Notification(notificationMessage = message, notificationType = type) {
            timer?.stop()
            closeNotification(it)
        }
        notifications.add(notification)
        setPositionNotification(notification, xMargin, yMargin)
        setupAutoDismiss(notification, isAutoDismiss, delay)
        notification.isVisible = true

    }

    /**
     * Sets the position of the notification on the screen
     *
     * @param notification The notification to be positioned
     * @param xMargin The horizontal margin to be added to the notification
     * @param yMargin The vertical margin to be added to the notification
     */
    private fun setPositionNotification(
        notification: Notification,
        xMargin: Int = 0,
        yMargin: Int = 0
    ) {
        val windowLocation: Point = parentWindow.locationOnScreen
        val x = windowLocation.x + parentWindow.width - xMargin - notification.width
        val y = windowLocation.y + parentWindow.height - yMargin - notification.height - currentY
        notification.setLocation(x, y)
        currentY += notification.height + 10
    }

    /**
     * Sets up the auto-dismiss feature for the notification
     *
     * @param notification The notification to be closed
     * @param isAutoDismiss Whether the notification should be automatically dismissed
     * @param delay The delay in milliseconds before the notification is dismissed
     */
    private fun setupAutoDismiss(
        notification: Notification,
        isAutoDismiss: Boolean,
        delay: Int
    ) {
        if (isAutoDismiss) {
            timer = Timer(delay) { closeNotification(notification) }.apply {
                isRepeats = false
                start()
            }
        }
    }

    /**
     * Closes the notification and removes it from the list of notifications
     *
     * @param notification The notification to be closed
     */
    private fun closeNotification(notification: Notification) {
        SwingUtilities.invokeLater {
            notification.isVisible = false
            notifications.remove(notification)
            currentY = 20
            notifications.forEach { setPositionNotification(it, xMargin = 10) }
        }
    }

}