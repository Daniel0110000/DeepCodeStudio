package com.dr10.editor.ui.tabs

import com.dr10.common.ui.ThemeApp
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Point
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.SwingUtilities

class TabRow(
    private val onFilePathChange: (String) -> Unit
): JPanel() {

    private val tabHeaderPanel = JPanel()
    private val tabScrollPane = TabsScrollPane()
    private val contentPanel = JPanel(BorderLayout())
    private val tabs = mutableListOf<TabData>()
    var selectedTabIndex = -1
    private var currentContentComponent: JComponent? = null

    init {
        layout = BorderLayout()
        initializeComponents()
    }

    private fun initializeComponents() {
        tabHeaderPanel.layout = FlowLayout(FlowLayout.LEFT, 0, 0)
        tabScrollPane.viewport.view = tabHeaderPanel

        add(tabScrollPane, BorderLayout.NORTH)
        add(contentPanel, BorderLayout.CENTER)
    }

    /**
     * Adds a new tab
     *
     * @param tabData Data of the tab to be created
     * @param tabView View of the tab to be created
     */
    fun addTab(
        tabData: TabData,
        tabView: TabView
    ) {
        tabs.add(tabData)
        tabHeaderPanel.add(tabView)
        selectTab(tabs.size - 1)
    }

    /**
     * Deletes the tab associated with the given index
     *
     * @param index Index of the tab to remove
     */
    fun removeTabAt(index: Int) {
        if (index !in 0 until tabs.size) return
        val wasSelected = index == selectedTabIndex

        tabHeaderPanel.remove(index)
        tabs.removeAt(index)

        for (i in index until tabs.size) { tabs[i].index = i }

        when {
            tabs.isEmpty() -> {
                selectedTabIndex = -1
                showContent(null)
            }
            wasSelected -> {
                val indexToSelect = index - 1
                selectTab(if (indexToSelect == -1) 0 else indexToSelect)
            }
            selectedTabIndex > index -> {
                selectedTabIndex--
            }
        }

        tabHeaderPanel.revalidate()
        tabHeaderPanel.repaint()
    }

    /**
     * Updates everything necessary for the tab that was selected
     *
     * @param index Index of the tab that was selected
     */
    fun selectTab(index: Int) {
        if (index !in 0 until tabs.size) return

        val oldIndex = selectedTabIndex
        selectedTabIndex = index

        if (oldIndex >= 0 && oldIndex < tabHeaderPanel.componentCount) updateTabAppearance(oldIndex, false)
        updateTabAppearance(index, true)

        showContent(tabs[index].component)
        onFilePathChange(tabs[selectedTabIndex].tabModel.filePath)

        scrollToTab(index)
    }

    /**
     * Update the tab's UI based on whether the tab is selected or not
     *
     * @param index Index of the tab whose UI need to be updated
     * @param selected Flag indicating whether the tab is selected
     */
    private fun updateTabAppearance(index: Int, selected: Boolean) {
        if (index >= tabHeaderPanel.componentCount) return

        val tabPanel = tabHeaderPanel.getComponent(index) as JPanel

        tabPanel.border = if (selected) BorderFactory.createMatteBorder(0, 0, 3, 0, ThemeApp.awtColors.complementaryColor)
        else BorderFactory.createEmptyBorder(0, 0, 3, 0)

        tabPanel.repaint()
    }

    /**
     * Displays the content associated with the selected tab
     *
     * @param component Component to display in the screen
     */
    private fun showContent(component: JComponent?) {
        currentContentComponent?.let { contentPanel.remove(it) }

        component?.let {
            contentPanel.add(it, BorderLayout.CENTER)
            currentContentComponent = it
        }

        contentPanel.revalidate()
        contentPanel.repaint()
    }

    /**
     * Automatically scrolls to the selected tab
     *
     * @param index The index of the tab to scroll to
     */
    private fun scrollToTab(index: Int) {
        if (index >= tabHeaderPanel.componentCount) return

        SwingUtilities.invokeLater {
            val tabComponent = tabHeaderPanel.getComponent(index)
            val bounds = tabComponent.bounds

            val viewport = tabScrollPane.viewport
            val viewRect = viewport.viewRect

            if (bounds.x < viewRect.x || bounds.x + bounds.width > viewRect.x + viewRect.width) {
                val newX = bounds.x - (viewRect.width - bounds.width) / 2
                val constrainedX = maxOf(0, minOf(newX, tabHeaderPanel.width - viewRect.width))

                viewport.viewPosition = Point(constrainedX, 0)
            }
        }
    }

    /**
     * Returns the index of the tab associated with the given [TabModel]
     *
     * @param tabModel The value to search for in order to return its index
     * @return The index of the tab associated with the given parameter
     */
    fun getIndexOfTabByTabModel(tabModel: TabModel): Int = tabs.indexOfFirst { it.tabModel == tabModel }

    /**
     * Returns the size of the tabs list
     */
    fun getTabsSize(): Int = tabs.size
}