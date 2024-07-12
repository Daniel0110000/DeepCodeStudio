package com.dr10.common.utilities

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DirectoryChooserTest {

    @Before
    fun setUp() {
        mockkObject(DirectoryChooser)
    }

    @Test
    fun `chooseDirectory should return select directory path`() = runTest {
        val directoryPath = "/home/mock/DeepCodeProjects/"
        coEvery { DirectoryChooser.chooseDirectory() } returns directoryPath
        val selectedPath = DirectoryChooser.chooseDirectory()

        assertThat(selectedPath).isEqualTo(directoryPath)
    }

    @Test
    fun `chooseDirectory should return null when canceled`() = runTest {
        coEvery { DirectoryChooser.chooseDirectory() } returns null
        val selectedPath = DirectoryChooser.chooseDirectory()

        assertThat(selectedPath).isNull()

    }

    @Test
    fun `chooseDirectory should return null when an error occurs`() = runTest {
        coEvery { DirectoryChooser.chooseDirectorySwing() } throws RuntimeException("An error occurred while executing JFileChooser::showOpenDialog")
        val selectedPath = DirectoryChooser.chooseDirectory()

        assertThat(selectedPath).isNull()

    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}