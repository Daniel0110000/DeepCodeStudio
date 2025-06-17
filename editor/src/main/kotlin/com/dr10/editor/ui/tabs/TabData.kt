package com.dr10.editor.ui.tabs

import javax.swing.JComponent

data class TabData(
    val tabModel: TabModel,
    var component: JComponent,
    var index: Int
)
