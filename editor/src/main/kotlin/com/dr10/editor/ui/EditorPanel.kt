package com.dr10.editor.ui

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.UIStateManager
import com.dr10.editor.ui.tabs.EditorTab
import com.dr10.editor.ui.tabs.TabView
import com.dr10.editor.ui.viewModels.TabsViewModel
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.JTabbedPane

/**
 * Panel that will contain all the editor tabs
 *
 * @property tabsViewModel ViewModel that manages the state of the open tabs
 */
class EditorPanel(
    private val tabsViewModel: TabsViewModel
): JPanel() {

    // Map to keep track of the open tabs
    private val openTabs = mutableMapOf<String, EditorTab>()

    init { onCreate() }

    private fun onCreate() {
        val editorLayout = GroupLayout(this)
        layout = editorLayout

        val tabPanel = JTabbedPane().apply { background = ThemeApp.colors.secondColor.toAWTColor() }

        editorLayout.setHorizontalGroup(
            editorLayout.createParallelGroup()
                .addComponent(tabPanel)
        )

        editorLayout.setVerticalGroup(
            editorLayout.createSequentialGroup()
                .addComponent(tabPanel)
        )

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
                            // "Find the index of the tab to close using the hashCode of its filePath
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
                            }
                        })
                        // Set the newly added tab as the selected tab
                        tabPanel.selectedIndex = tabPanel.tabCount - 1
                    }
                }
            }
        )

    }
}