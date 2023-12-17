package ui.terminal

import com.jediterm.terminal.emulator.ColorPalette
import java.awt.Color

class TerminalColorPalette private constructor(private val colors: Array<Color>) : ColorPalette() {
    public override fun getForegroundByColorIndex(colorIndex: Int): Color {
        return colors[colorIndex]
    }

    override fun getBackgroundByColorIndex(colorIndex: Int): Color {
        return colors[colorIndex]
    }

    companion object {
        private val XTERM_COLORS = arrayOf(
            Color(0xABB2BF),
            Color(0xcd0000),
            Color(0x00cd00),
            Color(0xcdcd00),
            Color(0x1e90ff),
            Color(0xcd00cd),
            Color(0x00cdcd),
            Color(0x1E2229),
            // --------------------------------
            Color(0xABB2BF),
            Color(0xff0000),
            Color(0x00ff00),
            Color(0xffff00),
            Color(0x4682b4),
            Color(0xff00ff),
            Color(0x00ffff),
            Color(0x1E2229)
        )
        val PALETTE: ColorPalette = TerminalColorPalette(XTERM_COLORS)
    }
}