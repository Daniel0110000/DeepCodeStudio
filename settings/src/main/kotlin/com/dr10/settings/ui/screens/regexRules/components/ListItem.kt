package com.dr10.settings.ui.screens.regexRules.components

import javax.swing.JComponent

data class ListItem<T>(
    val data: T,
    val component: JComponent
)