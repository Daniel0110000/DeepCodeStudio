package com.dr10.common.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Manages the state of the UI by observing changes in a [StateFlow] and executing a callback when the state changes
 *
 * @param stateFlow A [StateFlow] representing the state to observe
 * @param onStateChanged A callback function to execute when the state changes
 */
class UIStateManager<T>(
    private val stateFlow: StateFlow<T>,
    private val onStateChanged: (T) -> Unit
) {

    private var stateListener: StateListener<T>? = null

    init {
        collectStateFlow()
    }

    /**
     * Collects the [StateFlow] and invokes the callback when the state change
     */
    private fun collectStateFlow() {
        CoroutineScope(Dispatchers.Default).launch {
            stateFlow.collect { state ->
                stateListener?.onStatedChanged(state)
                onStateChanged(state)
            }
        }
    }

}