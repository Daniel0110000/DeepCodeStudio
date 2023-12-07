package ui.editor.navigation

import androidx.compose.runtime.Composable

class NavigationHost(
    val navController: NavController,
    val contents: @Composable NavigationGraphBuilder.() -> Unit
){

    /**
     * Builds and renders the navigation graph defined in the [contents] [Composable] block
     */
    @Composable
    fun build(){
        NavigationGraphBuilder().renderContents()
    }

    /**
     * Inned class that provides a DSL for building the navigation graph within the [NavigationHost]
     *
     * @param navController The [NavController] associated with this [NavigationGraphBuilder]
     */
    inner class NavigationGraphBuilder(
        val navController: NavController = this@NavigationHost.navController
    ){
        @Composable
        fun renderContents(){
            this@NavigationHost.contents(this)
        }
    }
}

/**
 * Adds a composable screen to the navigation graph for a specific route
 *
 * @param route The route associated with the composable screen
 * @param content The content of the composable screen to be displayed when the route is the current screen
 */
@Composable
fun NavigationHost.NavigationGraphBuilder.composable(
    route: String,
    content: @Composable () -> Unit
){
    if(navController.currentScreen.value == route){
        content()
    }
}