package com.dr10.settings.ui

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.notification.NotificationManager
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.settings.di.Inject
import com.dr10.settings.ui.screens.SettingsScreen
import com.dr10.settings.ui.viewModels.SettingsNotificationsViewModel
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities

/**
 * Settings window for configuring the code editor
 *
 * @param window The main [JFrame] of the application
 * @param onCloseWindow Callback function invoked when the settings window is closed
 */
class SettingsWindow(
    private val window: JFrame,
    private val onCloseWindow: () -> Unit
): JFrame() {

    private val notificationsViewModel: SettingsNotificationsViewModel = Inject().settingsNotificationsViewModel
    private val notificationManager = NotificationManager(this)
    private val notificationStates = FlowStateHandler().run {
        notificationsViewModel.state.collectAsState(SettingsNotificationsViewModel.SettingsNotificationsState())
    }

    init { onCreate() }

    private fun onCreate() = SwingUtilities.invokeLater {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        setSize(1200, 800)
        contentPane.background = ThemeApp.colors.background.toAWTColor()
        title = "Settings"
        setLocationRelativeTo(window)

        addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                onCloseWindow()
                window.isEnabled = true
            }
        })

        contentPane.add(SettingsScreen { data -> notificationManager.show(data.copy(xMargin = 10)) })

        window.isEnabled = false
        isVisible = true

        setState(notificationStates, SettingsNotificationsViewModel.SettingsNotificationsState::data) { notificationData ->
            if (notificationData != null) {
                notificationManager.show(notificationData.copy(xMargin = 15))

                notificationsViewModel.clearNotificationData()
            }
        }

        setState(notificationStates, SettingsNotificationsViewModel.SettingsNotificationsState::notificationIdToClose) { id ->
            if (id != null) {
                notificationManager.closeNotificationById(id)

                notificationsViewModel.clearNotificationId()
            }
        }
    }
}