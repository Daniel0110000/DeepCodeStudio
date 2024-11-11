package com.dr10.terminal.ui

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.terminal.model.ShellInfo
import com.dr10.terminal.ui.components.ActionTerminalButton
import com.dr10.terminal.ui.components.AvailableShellsCellRender
import com.dr10.terminal.ui.components.JediTermWidgetImpl
import com.dr10.terminal.ui.components.TerminalTabView
import com.dr10.terminal.ui.viewModel.TerminalViewModel
import com.dr10.terminal.utils.createTtyConnector
import java.awt.event.ItemEvent
import javax.swing.GroupLayout
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.border.EmptyBorder

class TerminalView(
    private val viewModel: TerminalViewModel,
    private val onCloseTerminalPanel: () -> Unit
): JPanel() {

    private val terminalState = FlowStateHandler().run {
        viewModel.state.collectAsState(TerminalViewModel.TerminalState())
    }

    private val openTerminals = mutableMapOf<String, String>()

    init { onCreate() }

    private fun onCreate() {
        val terminalLayout = GroupLayout(this)
        layout = terminalLayout

        val title = JLabel("Terminal").apply {
            font = ThemeApp.text.fontInterBold()
            foreground = ThemeApp.awtColors.textColor
        }

        val newTerminalButton = ActionTerminalButton(AppIcons.newTerminalIcon){ viewModel.openNewTerminal() }
        val shellOptions = JComboBox<ShellInfo>().apply {
            border = EmptyBorder(0, 0, 0, 0)
            background = ThemeApp.awtColors.secondaryColor
            renderer = AvailableShellsCellRender()
            addItemListener { e ->
                if (e.stateChange == ItemEvent.SELECTED) {
                    viewModel.setCurrentShellSelected(e.item as ShellInfo)
                }
            }
            setState(terminalState, TerminalViewModel.TerminalState::availableShells) { shells ->
                shells.forEach { addItem(it) }
            }
        }

        val closeTerminalButton = ActionTerminalButton(AppIcons.closeTerminalIcon){
            onCloseTerminalPanel()
        }

        val tabbedPane = JTabbedPane(JTabbedPane.RIGHT).apply {
            tabLayoutPolicy = JTabbedPane.SCROLL_TAB_LAYOUT
            setState(terminalState, TerminalViewModel.TerminalState::terminals) { terminals ->
                // Retain only the tabs that are still in the state, removing closed tabs from the map
                openTerminals.keys.retainAll(terminals.map { it.terminalID }.toSet())

                // Iterates through the tabs in the state and add any new ones to the UI
                terminals.forEach { terminal ->
                    // If the terminal is not already open, open it
                    if (!openTerminals.containsKey(terminal.terminalID)) {
                        // Adds the terminal to the map of open terminals
                        openTerminals[terminal.terminalID] = terminal.name
                        // Creates a new instance of the terminal widget and adds it to the [JTabbedPane]
                        val terminalWidget = JediTermWidgetImpl(TerminalSettingsProvider()).apply {
                            ttyConnector = createTtyConnector(terminal)
                            start()
                        }
                        addTab(terminal.terminalID, terminalWidget)
                        // Sets custom design to the tab
                        setTabComponentAt(tabCount - 1, TerminalTabView(terminal) {
                            // Stops and Closes the [terminalWidget] process
                            terminalWidget.apply {
                                stop()
                                close()
                            }

                            // Gets the index of the tab that was closed
                            val tabIndex = indexOfTab(terminal.terminalID)
                            val currentSelectedIndex = selectedIndex

                            if (tabIndex != -1) {
                                // If the tabIndex isn't -1, remove the tab from the [JTabbedPane]
                                openTerminals.remove(terminal.terminalID)
                                remove(tabIndex)
                                viewModel.closeTerminal(terminal)

                                if (currentSelectedIndex == tabIndex) {
                                    // If the selected tab is the tab that was closed, select the previous tab
                                    val indexToSelected = tabIndex - 1
                                    selectedIndex = if (indexToSelected == -1) tabCount -1
                                    else indexToSelected
                                }

                                // If there are no more tabs, close the terminal panel
                                if (tabCount == 0) onCloseTerminalPanel()
                            }

                        })
                        // Sets the selected tab to the last tab
                        selectedIndex = tabCount - 1
                    }
                }
            }
        }

        terminalLayout.setHorizontalGroup(
            terminalLayout.createParallelGroup()
                .addGroup(
                    terminalLayout.createSequentialGroup()
                        .addGap(7)
                        .addComponent(title)
                        .addGap(0, 0, Short.MAX_VALUE.toInt())
                        .addComponent(newTerminalButton, 0, 0, 30)
                        .addComponent(shellOptions, 0, 0, 100)
                        .addGap(7)
                        .addComponent(closeTerminalButton, 0, 0, 30)
                        .addGap(7)
                )
                .addComponent(tabbedPane)
        )

        terminalLayout.setVerticalGroup(
            terminalLayout.createSequentialGroup()
                .addGap(5)
                .addGroup(
                    terminalLayout.createParallelGroup()
                        .addComponent(title)
                        .addComponent(newTerminalButton, 25, 25, 25)
                        .addComponent(shellOptions, 25, 25, 25)
                        .addComponent(closeTerminalButton, 25, 25, 25)

                )
                .addGap(5)
                .addComponent(tabbedPane)
        )

    }

}