package ui.terminal

import com.jediterm.terminal.HyperlinkStyle
import com.jediterm.terminal.TerminalColor
import com.jediterm.terminal.TextStyle
import com.jediterm.terminal.emulator.ColorPalette
import com.jediterm.terminal.model.LinesBuffer
import com.jediterm.terminal.ui.TerminalActionPresentation
import com.jediterm.terminal.ui.settings.SettingsProvider
import java.awt.Color
import java.awt.Font
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

class TerminalSettingsProvider: SettingsProvider{
    override fun getNewSessionActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "New Session",
        KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK or InputEvent.SHIFT_DOWN_MASK)
    )


    override fun getOpenUrlActionPresentation(): TerminalActionPresentation =
        TerminalActionPresentation("Open as URL", emptyList())


    override fun getCopyActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "Copy",
        KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK or InputEvent.SHIFT_DOWN_MASK)
    )

    override fun getPasteActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "Paste",
        KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK or InputEvent.SHIFT_DOWN_MASK)
    )

    override fun getClearBufferActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "Clear Buffer",
        KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK)
    )

    override fun getPageUpActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "Page Up", KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.SHIFT_DOWN_MASK)
    )

    override fun getPageDownActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "Page Down",
        KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.SHIFT_DOWN_MASK)
    )

    override fun getLineUpActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "Line Up",
        KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK)
    )

    override fun getLineDownActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "Line Down",
        KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK)
    )

    override fun getCloseSessionActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "Close Session",
        KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK or InputEvent.SHIFT_DOWN_MASK)
    )

    override fun getFindActionPresentation(): TerminalActionPresentation = TerminalActionPresentation(
        "Find",
        KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK)
    )

    override fun getSelectAllActionPresentation(): TerminalActionPresentation =
        TerminalActionPresentation("Select All", emptyList())

    override fun getTerminalColorPalette(): ColorPalette = TerminalColorPalette.PALETTE

    override fun getTerminalFont(): Font = Font("Monospaced", Font.PLAIN, terminalFontSize.toInt())

    override fun getTerminalFontSize(): Float = 13f

    override fun getDefaultStyle(): TextStyle = TextStyle(TerminalColor.BLACK, TerminalColor.WHITE)

    override fun getSelectionColor(): TextStyle = TextStyle(TerminalColor.WHITE, TerminalColor.rgb(82, 109, 165))

    override fun getFoundPatternColor(): TextStyle = TextStyle(TerminalColor.BLACK, TerminalColor.rgb(255, 255, 0))

    override fun getHyperlinkColor(): TextStyle = TextStyle(TerminalColor.awt(Color.BLUE), TerminalColor.WHITE)

    override fun getHyperlinkHighlightingMode(): HyperlinkStyle.HighlightMode = HyperlinkStyle.HighlightMode.HOVER

    override fun useInverseSelectionColor(): Boolean = true

    override fun copyOnSelect(): Boolean = emulateX11CopyPaste()

    override fun pasteOnMiddleMouseClick(): Boolean {
        return emulateX11CopyPaste()
    }

    override fun emulateX11CopyPaste(): Boolean = false

    override fun useAntialiasing(): Boolean = true

    override fun maxRefreshRate(): Int = 50

    override fun audibleBell(): Boolean = true

    override fun enableMouseReporting(): Boolean = true

    override fun caretBlinkingMs(): Int = 505

    override fun scrollToBottomOnTyping(): Boolean = true

    override fun DECCompatibilityMode(): Boolean = true

    override fun forceActionOnMouseReporting(): Boolean = false

    override fun getBufferMaxLinesCount(): Int = LinesBuffer.DEFAULT_MAX_LINES_COUNT

    override fun altSendsEscape(): Boolean = false

    override fun ambiguousCharsAreDoubleWidth(): Boolean = false
}