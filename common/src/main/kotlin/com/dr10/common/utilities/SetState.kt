package com.dr10.common.utilities

import javax.swing.JComponent
import kotlin.reflect.KProperty1

/**
 * Extension function for [JComponent] that provides a convenient way to bind a state property to UI updates
 *
 * @param state The StateWrapper containing the value to observe
 * @param prop The specific property of the state to bind to
 * @param updateUI The callback function that will be called to update the UI when the [prop] changes
 */
fun <T, R> JComponent.setState(state: FlowStateHandler.StateWrapper<T>, prop: KProperty1<T, R>, updateUI: (R) -> Unit) {
    FlowStateHandler().run { bindTo(state, prop, updateUI) }
}

/**
 * Function to set state without binding to UI
 */
fun <T, R> setState(state: FlowStateHandler.StateWrapper<T>, prop: KProperty1<T, R>, updateUI: (R) -> Unit) {
    FlowStateHandler().run { bindTo(state, prop, updateUI) }
}