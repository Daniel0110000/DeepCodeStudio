package com.dr10.settings.ui

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.settings.ui.screens.SettingsScreen
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

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

    init { onCreate() }

    private fun onCreate() {
        SwingUtilities.invokeLater {
            defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
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

            contentPane.add(SettingsScreen())

            window.isEnabled = false
            isVisible = true
        }
    }
}