package ui.editor

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.TextContextMenu
import androidx.compose.runtime.Composable

/**
 * Object that creates an empty context menu by implementing the [TextContextMenu] interface for its creation
 */
@OptIn(ExperimentalFoundationApi::class)
object EmptyContextMenu: TextContextMenu {
    @ExperimentalFoundationApi
    @Composable
    override fun Area(textManager: TextContextMenu.TextManager, state: ContextMenuState, content: @Composable () -> Unit) {
        ContextMenuArea({ emptyList() }, state, content = content)
    }
}