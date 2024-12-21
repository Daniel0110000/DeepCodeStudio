package com.dr10.settings.ui.screens.regexRules.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.extensions.mouseEventListener
import com.dr10.common.utilities.DrLogging
import java.awt.Color
import javax.swing.BoxLayout
import javax.swing.JPanel

class RegexRulesList<T>(
    private val defaultBackground: Color = ThemeApp.awtColors.primaryColor,
    private val hoverBackground: Color = ThemeApp.awtColors.hoverColor,
    private val selectedBackground: Color = ThemeApp.awtColors.hoverColor,
    private val onClickListener: (T) -> Unit
): JPanel() {

    private val logger = DrLogging(this::class.java)

    private val items: MutableList<ListItem<T>> = mutableListOf()
    private var itemUIClass: Class<out JPanel>? = null
    private var selectedIndex = -1
    var itemListener: ItemListener<T>? = null

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        isOpaque = false
    }

    fun setItemUI(uiClass: Class<out JPanel>) {
        itemUIClass = uiClass
    }

    fun setListData(data: List<T>) {
        clearAll()
        selectedIndex = -1
        data.forEach { addItem(it) }
        revalidate()
        repaint()
    }

    private fun clearAll() {
        items.clear()
        removeAll()
    }

    private fun addItem(data: T) {
        val constructor = itemUIClass?.getDeclaredConstructor(data!!::class.java, ItemListener::class.java)
        if (constructor == null) {
            logger.error("Constructor not found for ${itemUIClass?.name}")
            return
        }
        val component = constructor.newInstance(data, itemListener) as JPanel
        val newItem: ListItem<T> = ListItem(data, component)
        items.add(newItem)

        val componentIndex = items.indexOf(newItem)
        component.setupListeners(componentIndex, data)
        add(component)
    }

    private fun JPanel.setupListeners(componentIndex: Int, data: T) {
        this.mouseEventListener(
            onEnter = {
                if(selectedIndex != componentIndex) {
                    this.background = hoverBackground
                    this.repaint()
                }
            },
            onExit = {
                if(selectedIndex != componentIndex) {
                    this.background = defaultBackground
                    this.repaint()
                }
            },
            onClick = {
                onClickListener(data)
                setSelectedIndex(componentIndex)
            }
        )
    }

    private fun setSelectedIndex(index: Int) {
        if (index != selectedIndex && index in items.indices) {
            items.getOrNull(selectedIndex)?.component?.background = defaultBackground
            items[index].component.background = selectedBackground
            selectedIndex = index
            repaint()
        }
    }
}