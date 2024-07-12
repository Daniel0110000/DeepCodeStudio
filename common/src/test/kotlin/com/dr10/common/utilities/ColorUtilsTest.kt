package com.dr10.common.utilities

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test


class ColorUtilsTest {

    @Before
    fun setUp() {
        mockkObject(ColorUtils)
    }

    @Test
    fun `isHexadecimalColor should return true for valid colors`() {
        assertThat(ColorUtils.isHexadecimalColor("#ABB2BF")).isTrue()
        assertThat(ColorUtils.isHexadecimalColor("#C678DD")).isTrue()
        assertThat(ColorUtils.isHexadecimalColor("#FFF")).isTrue()
        assertThat(ColorUtils.isHexadecimalColor("#C678DD")).isTrue()
    }

    @Test
    fun `isHexadecimalColor should return false for invalid colors`() {
        assertThat(ColorUtils.isHexadecimalColor("ABB2BF")).isFalse()
        assertThat(ColorUtils.isHexadecimalColor("C678DD")).isFalse()
        assertThat(ColorUtils.isHexadecimalColor("123234")).isFalse()
        assertThat(ColorUtils.isHexadecimalColor("#ZZZZZZ")).isFalse()
    }

    @Test
    fun `hexToColor should convert hex string to composable Color`() {
        val color = ColorUtils.hexToColor("#ABB2BF")
        val expectedColor = Color(0xFFABB2BF)

        verify { ColorUtils.hexToColor("#ABB2BF") }
        assertThat(color).isEqualTo(expectedColor)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}