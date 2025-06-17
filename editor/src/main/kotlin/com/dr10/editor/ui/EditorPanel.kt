package com.dr10.editor.ui

import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.editor.ui.tabs.*
import com.dr10.editor.ui.viewModels.TabsViewModel
import java.awt.BorderLayout
import java.awt.CardLayout
import javax.swing.JPanel

/**
 * Panel that will contain all the editor tabs
 *
 * @property tabsState Current tabs state
 * @property onChangeTabSelected Callback function invoked when the state of the selected tab needs to be changed
 * @property onCloseTab Callback function invoked when a tab needs to be closed
 */
class EditorPanel(
    private val tabsState: FlowStateHandler.StateWrapper<TabsViewModel.TabsState>,
    private val tabsViewModel: TabsViewModel,
    private val onChangeTabSelected: (String) -> Unit,
    private val onCloseTab: (TabModel) -> Unit
): JPanel() {

    // Map to keep track of the open tabs
    private val openTabs = mutableMapOf<String, EditorTab>()

    init { onCreate() }

    private fun onCreate() {
        layout = BorderLayout()

        val cardLayout = CardLayout()
        val mainPanel = JPanel(cardLayout)
        val tabRow = TabRow { onChangeTabSelected(it) }
        val emptyPanel = EmptyEditorPanel()

        mainPanel.add(emptyPanel, EMPTY_PANEL)
        mainPanel.add(tabRow, TABS_PANEL)

        add(mainPanel, BorderLayout.CENTER)

        setState(tabsState, TabsViewModel.TabsState::tabs) { tabs ->
            openTabs.keys.retainAll(tabs.map { it.filePath }.toSet())
            tabs.forEach { tab ->
                if (!openTabs.containsKey(tab.filePath)) {
                    val editorTab = EditorTab(tab)
                    openTabs[tab.filePath] = editorTab

                    val tabData = TabData(tab, editorTab, tabRow.getTabsSize())
                    tabRow.addTab(
                        tabData,
                        TabView(
                            tabData = tabData,
                            onSelectTab = { tabRow.selectTab(it) },
                            onCloseTab = {
                                openTabs.remove(it.tabModel.filePath)
                                tabRow.removeTabAt(it.index)
                                onCloseTab(it.tabModel)
                                editorTab.cancelAutoSaveProcess()
                                if (tabRow.getTabsSize() == 0) {
                                    cardLayout.show(mainPanel, EMPTY_PANEL)
                                    onChangeTabSelected("~")
                                }
                            }
                        )
                    )
                }
            }

            // Show the appropriate panel based on whether there are tabs or not
            if (tabs.isEmpty()) cardLayout.show(mainPanel, EMPTY_PANEL)
            else cardLayout.show(mainPanel, TABS_PANEL)
        }

        setState(tabsState, TabsViewModel.TabsState::openedTabAgain) { tab ->
            if (tab != null) {
                val tabIndex = tabRow.getIndexOfTabByTabModel(tab)
                tabRow.selectTab(tabIndex)
                tabsViewModel.clearOpenedTabAgain()
            }
        }
    }

     companion object {
        private const val TABS_PANEL = "TABS_PANEL"
        private const val EMPTY_PANEL = "EMPTY_PANEL"
    }
}