package com.dr10.settings.ui

import androidx.compose.ui.awt.ComposePanel
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.settings.ui.viewModels.SettingsViewModel
import com.dr10.settings.ui.viewModels.SyntaxHighlightSettingsViewModel
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

/**
 * Settings window for configuring the code editor
 *
 * @param window The main [JFrame] of the application
 * @param settingsViewModel The viewModel that manages the state of the settings window
 * @param syntaxHighlightSettingsViewModel The viewModel that manages the state of the syntax highlighting settings
 * @param onCloseWindow Callback function invoked when the settings window is closed
 */
class SettingsWindow(
    private val window: JFrame,
    private val settingsViewModel: SettingsViewModel,
    private val syntaxHighlightSettingsViewModel: SyntaxHighlightSettingsViewModel,
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

            // Add a [ComposePanel] width the content of the settings window created in Jetpack Compose
            contentPane.add(
                ComposePanel().apply {
                    background = ThemeApp.colors.background.toAWTColor()
                    setContent {
                        SettingsComposePanel(
                            viewModel = settingsViewModel,
                            syntaxHighlightSettingsViewModel = syntaxHighlightSettingsViewModel
                        )
                    }
                }
            )

            window.isEnabled = false
            isVisible = true

        }

    }

}