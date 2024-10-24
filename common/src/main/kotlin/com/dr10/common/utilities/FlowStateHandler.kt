package com.dr10.common.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.swing.SwingUtilities
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty1

class FlowStateHandler: CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    /**
     * Extension function to collect [Flow] values into a [StateWrapper]
     *
     * @param initial The initial value for the state
     * @return A [StateWrapper] containing the Flow's current value
     */
    fun <T> Flow<T>.collectAsState(initial: T): StateWrapper<T> {
        val state = StateWrapper(initial)
        launch {
            collect { value -> state.updateValue(value) }
        }
        return state
    }

    /**
     * Wrapper class that maintains state and manages property-specific listeners
     *
     * @param initialValue The initial value for this state
     */
    class StateWrapper<T>(initialValue: T) {
        // Map to store the listeners
        private val listeners = mutableMapOf<KProperty1<T, *>, MutableList<(Any?) -> Unit>>()
        var value: T = initialValue

        /**
         * Adds a listener for a specific property
         *
         * @param prop The property to listen to
         * @param listener The callback function to execute when the property changes
         */
        fun <R> addListener(prop: KProperty1<T, R>, listener: (R) -> Unit) {
            listeners.getOrPut(prop) { mutableListOf() }.add(listener as ((Any?) -> Unit))
            listener(prop.get(value))
        }

        /**
         * Removes a listener for a specific property
         *
         * @param prop The property to stop listening to
         * @param listener The callback function to remove
         */
        fun <R> removeListener(prop: KProperty1<T, R>, listener: (R) -> Unit) {
            listeners[prop]?.remove(listener as ((Any?) -> Unit))
        }

        /**
         * Updates the wrapped value and notifies relevant listeners if properties have chnaged
         *
         * @param newValue The new value to update to
         */
        fun updateValue(newValue: T) {
            val oldValue = value
            value = newValue
            listeners.forEach { (prop, proListeners) ->
                val oldPropValue = prop.get(oldValue)
                val newPropValue = prop.get(newValue)
                if (oldPropValue != newPropValue) {
                    proListeners.forEach { it(newPropValue) }
                }
            }
        }
    }

    /**
     * Binds a property of a StateWrapper to a UI update function
     *
     * @param state The [StateWrapper] containing the value
     * @param prop The property to bind to
     * @param updateUI The callback function to update the UI
     */
    fun <T, R> bindTo(state: StateWrapper<T>, prop: KProperty1<T, R>, updateUI: (R) -> Unit) {
        state.addListener(prop) { value -> SwingUtilities.invokeLater { updateUI(value) } }
    }

    /**
     * Cleans up resources by cancelling the coroutine job
     */
    fun cleanup() {
        job.cancel()
    }

}