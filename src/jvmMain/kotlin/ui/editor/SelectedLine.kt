package ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ui.ThemeApp

/**
 * Composable function that represents a visual indicator for the selected line
 *
 * @param cursorY The vertical position of the cursor, used to determine the selected line
 * @return A Box composable representing the selected line indicator
 */
@Composable
fun selectedLine(cursorY: Int) = Box(
    modifier = Modifier
        .absoluteOffset { IntOffset(0, cursorY) }
        .fillMaxWidth()
        .background(ThemeApp.colors.hoverTab)
        .height(19.dp)
)