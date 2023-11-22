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
 * Composable function that represents a visual indicator for an error line
 *
 * @param coordinatesY The vertical position of the error line indicator
 * @return A Box composable representing the error line indicator
 */
@Composable
fun errorLine(coordinatesY: Int) = Box(
    modifier = Modifier
        .absoluteOffset { IntOffset(0, coordinatesY) }
        .fillMaxWidth()
        .background(ThemeApp.colors.error)
        .height(19.dp)
)