package com.dr10.common.utilities

import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import java.awt.Image
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO
import javax.swing.ImageIcon

object ImageResourceUtils {

    /**
     * Loads an image from teh given path and scales it to the specified width and height
     *
     * @param imageResourcePath The path to the image resource within the classpath
     * @param width The desired width of the resulting image
     * @param height The desired height of the resulting image
     * @return An [ImageIcon] containing the loaded and scaled image
     */
    fun loadResourceImage(
        imageResourcePath: String,
        width: Int,
        height: Int
    ): ImageIcon {
        val inputStreamImage = javaClass.classLoader.getResourceAsStream(imageResourcePath)

        return if(imageResourcePath.endsWith(".svg")) {
            svgToImageIcon(inputStreamImage!!, width, height)
        }
        else {
            val image = ImageIO.read(inputStreamImage)
            val scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH)
            ImageIcon(scaledImage)
        }

    }

    /**
     * Converts an SVG input stream into a raster ImageIcon with specified dimensions
     *
     * @param inputStream The input stream of the SVG file
     * @param width The desired width of the resulting image
     * @param height The desired height of the resulting image
     * @return An [ImageIcon] containing the rasterized and scaled image
     */
    private fun svgToImageIcon(
        inputStream: InputStream,
        width: Int,
        height: Int
    ): ImageIcon {
        val transcoderInput = TranscoderInput(inputStream)
        val outputStream = ByteArrayOutputStream()
        val transcoderOutput = TranscoderOutput(outputStream)

        PNGTranscoder().apply {
            transcode(transcoderInput, transcoderOutput)
        }

        val pngData = outputStream.toByteArray()
        val bufferedImage = ImageIO.read(pngData.inputStream())
        val scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        return ImageIcon(scaledImage)

    }

}