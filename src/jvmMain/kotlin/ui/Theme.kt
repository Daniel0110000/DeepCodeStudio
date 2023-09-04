package ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font

object ThemeApp {

    val colors: Colors = Colors()
    val text: Text = Text()

    class Colors(
        val background: Color = Color(0xFF282C34),
        val secondColor: Color = Color(0xFF1E2229),
        val textColor: Color = Color(0xFFABB2BF),
        val folderOpenTextColor: Color = Color(0xFFFC6161),
        val folderCloseTextColor: Color = Color(0xFFD19A66),
        val buttonColor: Color = Color(0xFF3498DB),
        val asmIconColor: Color = Color(0xFFD35400)
    )

    class Text(
        val fontFamily: FontFamily = FontFamily(Font(resource = "font/Inter-Regular.ttf"))
    )

}