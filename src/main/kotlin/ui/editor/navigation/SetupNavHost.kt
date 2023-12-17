package ui.editor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.editor.EditorState
import ui.editor.EditorViewTab

/**
 * [Composable] function representing a navigation host for multiple [EditorViewTab]
 *
 * @param navController The [NavController] responsible for handling navigation withing the host
 * @param states A list of [EditorState] instances representing individual editor states
 * @param modifier [Modifier] for each [EditorViewTab]
 */
@Composable
fun SetupNavHost(
    navController: NavController,
    states: List<EditorState>,
    modifier: Modifier,
){
    NavigationHost(navController){
        states.forEach { editorState -> 
            composable(editorState.filePath.value){
                EditorViewTab(modifier, editorState)
            }
        }
    }.build()
}