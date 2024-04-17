package com.dr10.editor.ui.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class NavController(
    private val startDestination: String,
    private var backStackScreens: MutableSet<String> = mutableSetOf()
){
    var currentScreen: MutableState<String> = mutableStateOf(startDestination)

    /**
     * Navigates to the specified route, updating the current screen and managing the back stack
     *
     * @param route The destination route to navigate to
     */
    fun navigate(route: String){
        // Check if the destination route is different from the current screen
        if(route != currentScreen.value){
            // If the current screen is in the back stack and not the start destination, remove it
            if(backStackScreens.contains(currentScreen.value) && currentScreen.value != startDestination){
                backStackScreens.remove(currentScreen.value)
            }

            // If the route is the start destination, reset the back stack
            if(route == startDestination){
                backStackScreens = mutableSetOf()
            } else {
                // Add the current screen to the back stack
                backStackScreens.add(currentScreen.value)
            }

            // Update the current screen to the specified route
            currentScreen.value = route
        }
    }
}