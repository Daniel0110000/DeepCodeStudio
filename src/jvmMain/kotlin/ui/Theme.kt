package ui

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp

object ThemeApp {

    val colors: Colors = Colors()
    val text: Text = Text()
    val scrollbar: Scrollbar = Scrollbar()
    val code: Code = Code()

    class Colors(
        val background: Color = Color(0xFF282C34),
        val secondColor: Color = Color(0xFF1E2229),
        val textColor: Color = Color(0xFFABB2BF),
        val folderOpenTextColor: Color = Color(0xFFFC6161),
        val folderCloseTextColor: Color = Color(0xFFD19A66),
        val buttonColor: Color = Color(0xFF3498DB),
        val asmIconColor: Color = Color(0xFFD35400),
        val hoverTab: Color = Color(0x10FFFFFF),
        val lineNumberTextColor: Color = Color(0xFF515A6C)
    )

    class Text(
        val fontFamily: FontFamily = FontFamily(Font(resource = "font/Inter-Regular.ttf")),
        val codeTextFontFamily: FontFamily = FontFamily(Font(resource = "font/JetBrainsMonoItalic.ttf"))
    )

    class Scrollbar(
        val scrollbarStyle: ScrollbarStyle =  defaultScrollbarStyle().copy(
            unhoverColor = Color(0x20FFFFFF),
            hoverColor = Color(0x20FFFFFF),
            thickness = 8.dp
        ),
        val tabsScrollbarStyle: ScrollbarStyle = defaultScrollbarStyle().copy(
            unhoverColor = colors.background,
            hoverColor = colors.background
        )
    )

    class Code(
        val simple: SpanStyle = SpanStyle(Color(0xFFABB2BF)),
        val comment: SpanStyle = SpanStyle(Color(0xFF5C6370)),
        val keyword: SpanStyle = SpanStyle(Color(0xFFC678DD)),
        val string: SpanStyle = SpanStyle(Color(0xFF98C379)),
        val variable: SpanStyle = SpanStyle(Color(0xFF61AFEF)),
        val sectionAndLabel: SpanStyle = SpanStyle(Color(0xFF56B6C2)),
        val number: SpanStyle = SpanStyle(Color(0xFFD19A66))
    )

}