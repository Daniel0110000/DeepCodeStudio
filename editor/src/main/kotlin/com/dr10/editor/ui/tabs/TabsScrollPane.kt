package com.dr10.editor.ui.tabs

import java.awt.Container
import java.awt.Dimension
import javax.swing.JScrollPane
import javax.swing.JViewport
import javax.swing.ScrollPaneLayout

class TabsScrollPane: JScrollPane() {
    init {
        horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_AS_NEEDED
        verticalScrollBarPolicy = VERTICAL_SCROLLBAR_NEVER
        horizontalScrollBar.unitIncrement = 20
        border = null
        preferredSize = Dimension(0, 46)
        viewport.scrollMode = JViewport.SIMPLE_SCROLL_MODE
        horizontalScrollBar.setUI(TabsScrollbar())

        layout = object: ScrollPaneLayout() {
            override fun layoutContainer(parent: Container?) {
                super.layoutContainer(parent)

                val hsbHeight = 3
                hsb.setBounds(0, 0, width, hsbHeight)
                viewport.setBounds(0, hsbHeight, width, height - hsbHeight)
            }
        }
    }
}