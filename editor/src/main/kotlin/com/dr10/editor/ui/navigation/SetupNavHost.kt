package com.dr10.editor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import com.dr10.editor.ui.EditorState
import com.dr10.editor.ui.EditorViewTab

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
    autocompleteSettingsRepository: AutocompleteSettingsRepository
){
    NavigationHost(navController){
        states.forEach { editorState ->
            composable(editorState.filePath.value){
                EditorViewTab(modifier, editorState, autocompleteSettingsRepository)
            }
        }
    }.build()
}