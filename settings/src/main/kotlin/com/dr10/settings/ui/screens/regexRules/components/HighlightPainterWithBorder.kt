package com.dr10.settings.ui.screens.regexRules.components

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.Shape
import java.awt.geom.Rectangle2D
import javax.swing.text.Highlighter
import javax.swing.text.JTextComponent

class HighlightPainterWithBorder(
    private val backgroundColor: Color,
    private val borderColor: Color
): Highlighter.HighlightPainter {


    override fun paint(g: Graphics, p0: Int, p1: Int, bounds: Shape?, c: JTextComponent) {
        val g2d = g.create() as Graphics2D
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        calculateHighlightShape(c, p0, p1)?.let { shape ->
            g2d.color = backgroundColor
            g2d.fill(shape)
            g2d.color = borderColor
            g2d.draw(shape)
        }
    }

    /**
     * Calculates the shape of the highlight
     *
     * @param c The [JTextComponent] component that will be used for highlighting
     * @param p0 The start index
     * @param p1 The end index
     *
     * @return A [Shape] object of type [Rectangle2D] with the correct dimensions
     */
    private fun calculateHighlightShape(c: JTextComponent, p0: Int, p1: Int): Shape? = try {
        val start = c.modelToView2D(p0).bounds
        val end = c.modelToView2D(p1).bounds
        if (start != null && end != null) {
            val width = end.bounds.x - start.bounds.x
            val height = c.font.size + 2
            Rectangle2D.Float(
                start.bounds.x.toFloat(),
                start.bounds.y.toFloat(),
                width.toFloat(),
                height.toFloat()
            )
        } else null
    } catch (e: Exception) {
        null
    }
}