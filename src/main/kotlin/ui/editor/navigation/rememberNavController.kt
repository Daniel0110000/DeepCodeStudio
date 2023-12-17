package ui.editor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * Composable function to create and remember a [NavController] instance
 *
 * @param startDestination The initial screen to be set as the starting destination for the [NavController]
 * @param backStackScreens A mutable set to provide an initial set of screens for the [NavController] back stack
 * @return A [MutableState] containing the remembered [NavController] instance
 */
@Composable
fun rememberNavController(
    startDestination: String,
    backStackScreens: MutableSet<String> = mutableSetOf()
): MutableState<NavController> = rememberSaveable{
    mutableStateOf(NavController(startDestination, backStackScreens))
}