package com.dr10.editor.ui

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.UIStateManager
import com.dr10.editor.ui.tabs.EditorTab
import com.dr10.editor.ui.tabs.TabView
import com.dr10.editor.ui.viewModels.TabsViewModel
import java.awt.BorderLayout
import java.awt.CardLayout
import javax.swing.JPanel
import javax.swing.JTabbedPane

/**
 * Panel that will contain all the editor tabs
 *
 * @property tabsViewModel ViewModel that manages the state of the open tabs
 */
class EditorPanel(private val tabsViewModel: TabsViewModel): JPanel() {

    // Map to keep track of the open tabs
    private val openTabs = mutableMapOf<String, EditorTab>()

    init { onCreate() }

    private fun onCreate() {
        layout = BorderLayout()

        val cardLayout = CardLayout()
        val mainPanel = JPanel(cardLayout)
        val tabPanel = JTabbedPane().apply { background = ThemeApp.colors.secondColor.toAWTColor() }
        val emptyPanel = EmptyEditorPanel()

        mainPanel.add(emptyPanel, EMPTY_PANEL)
        mainPanel.add(tabPanel, TABS_PANEL)

        add(mainPanel, BorderLayout.CENTER)

        UIStateManager(
            stateFlow = tabsViewModel.state,
            onStateChanged = { state: TabsViewModel.TabsState ->
                // Retain only the tabs that are still in the state, removing closed tabs from the map
                openTabs.keys.retainAll(state.tabs.map { it.filePath }.toSet())

                // Iterate through the tabs in the state and add any new ones to the UI
                state.tabs.forEach { tab ->
                    if (!openTabs.containsKey(tab.filePath)) {
                        // Create a new EditorTab for the tab and add it to the openTabs map
                        val editorTab = EditorTab(tab)
                        openTabs[tab.filePath] = editorTab

                        // Add the new tab to the [JTabbedPane]
                        tabPanel.addTab(tab.filePath.hashCode().toString(), editorTab)

                        // A custom design is assigned to the tab
                        tabPanel.setTabComponentAt(tabPanel.tabCount - 1, TabView(tab) { tabToClose ->
                            // Find the index of the tab to close using the hashCode of its filePath
                            val tabIndex = tabPanel.indexOfTab(tabToClose.filePath.hashCode().toString())
                            // Get the index of the currently selected tab
                            val currentSelectedIndex = tabPanel.selectedIndex
                            if (tabIndex != -1 ) {
                                // Remove the tab from the map and the tab panel
                                openTabs.remove(tabToClose.filePath)
                                tabPanel.remove(tabIndex)
                                tabsViewModel.closeTab(tab)

                                // Adjust the selected tab after closing the current one
                                if (currentSelectedIndex == tabIndex) {
                                    val indexToSelected = tabIndex - 1
                                    tabPanel.selectedIndex = if (indexToSelected == -1) tabPanel.tabCount - 1
                                    else indexToSelected
                                }

                                // Show emptyPanel if no tabs are left
                                if (tabPanel.tabCount == 0) cardLayout.show(mainPanel, EMPTY_PANEL)
                            }
                        })
                        // Set the newly added tab as the selected tab
                        tabPanel.selectedIndex = tabPanel.tabCount - 1
                    }
                }

                // Show the appropriate panel based on whether there are tabs or not
                if (state.tabs.isEmpty()) cardLayout.show(mainPanel, EMPTY_PANEL)
                else cardLayout.show(mainPanel, TABS_PANEL)

            }
        )
    }

     companion object {
        private const val TABS_PANEL = "TABS_PANEL"
        private const val EMPTY_PANEL = "EMPTY_PANEL"
    }
}