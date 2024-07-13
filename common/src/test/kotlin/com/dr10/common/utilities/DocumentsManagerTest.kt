package com.dr10.common.utilities

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifyOrder
import org.apache.commons.io.FileUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

class DocumentsManagerTest {

    private lateinit var tempFile: File
    private lateinit var tempDir: File

    @Before
    fun setUp() {
        mockkObject(DocumentsManager)
        mockkStatic(FileUtils::class)
        tempFile = File.createTempFile("testFile", ".txt")
        tempDir = Files.createTempDirectory("testDir").toFile()
    }

    @Test
    fun `getUserHome should return the user's home directory path`() {
        val homeDirectoryPath = "/home/mock/"

        every { DocumentsManager.getUserHome() } returns homeDirectoryPath
        val userHome = DocumentsManager.getUserHome()

        assertThat(userHome).isEqualTo(homeDirectoryPath)
    }

    @Test
    fun `createNecessaryDirectories should create the necessary directories`() {
        val mockProjectsDirectory = mockk<File>()
        val mockDatabaseDirectory = mockk<File>()

        every { mockProjectsDirectory.exists() } returns false
        every { mockDatabaseDirectory.exists() } returns false

        DocumentsManager.projectsDirectory = mockProjectsDirectory
        DocumentsManager.databaseDirectory = mockDatabaseDirectory

        DocumentsManager.createNecessaryDirectories()

        verify { mockDatabaseDirectory.exists() }
        verify { mockProjectsDirectory.exists() }

        verify { mockDatabaseDirectory.mkdir() }
        verify { mockProjectsDirectory.mkdir() }

        verifyOrder {
            mockProjectsDirectory.exists()
            mockProjectsDirectory.mkdir()
            mockDatabaseDirectory.exists()
            mockDatabaseDirectory.mkdir()
        }
    }

    @Test
    fun `writeFile should return true and write correct content on successful write`() {
        val content = "DeepCode Studio Tests"
        val result = DocumentsManager.writeFile(tempFile, content)

        assertThat(result).isTrue()
        assertThat(tempFile.readText()).isEqualTo(content)

    }

    @Test
    fun `writeFile should return false on write failure`() {
        tempFile.setReadOnly()
        val result = DocumentsManager.writeFile(tempFile, "This should fail")

        assertThat(result).isFalse()
        assertThat(tempFile.readText()).isEmpty()
    }

    @Test
    fun `deleteFilesOrDirectory should correctly delete files and directories`() {
        DocumentsManager.deleteFileOrDirectory(tempDir)
        verify { FileUtils.deleteDirectory(tempDir) }

        DocumentsManager.deleteFileOrDirectory(tempFile)
        verify { FileUtils.delete(tempFile) }

        assertThat(tempDir.exists()).isFalse()
        assertThat(tempFile.exists()).isFalse()
    }

    @Test
    fun `createDirectory should create a directory if it does not exist`() {
        val dirPath = tempDir.absolutePath
        val dirName = "newDir"

        val directory = File("$dirPath/$dirName")
        assertThat(directory.exists()).isFalse()

        DocumentsManager.createDirectory(dirPath, dirName)

        assertThat(directory.exists()).isTrue()
        assertThat(directory.isDirectory).isTrue()

    }

    @Test
    fun `createFile should create a file if it does not exist`() {
        val dirPath = tempDir.absolutePath
        val fileName = "textFile.txt"

        val file = File("$dirPath/$fileName")
        assertThat(file.exists()).isFalse()

        DocumentsManager.createFile(dirPath, fileName)

        assertThat(file.exists()).isTrue()
        assertThat(file.isFile).isTrue()

    }

    @Test
    fun `renameFileOrDirectory should rename a file`() {
        val dirPath = File(tempDir, "testFile.txt")
        dirPath.createNewFile()

        val newName = "renamedFile.txt"
        DocumentsManager.renameFileOrDirectory(dirPath.absolutePath, newName)

        val renamedFile = File(tempDir, newName)

        assertThat(renamedFile.exists()).isTrue()
        assertThat(dirPath.exists()).isFalse()
    }

    @Test
    fun `renameFileOrDirectory should rename a directory`() {
        val dirPath = File(tempDir, "testDir")
        dirPath.mkdir()

        val newName = "renamedDir"
        DocumentsManager.renameFileOrDirectory(dirPath.absolutePath, newName)

        val renameDir = File(tempDir, newName)

        assertThat(renameDir.exists()).isTrue()
        assertThat(dirPath.exists()).isFalse()

    }

    @Test
    fun `renameFileOrDirectory should handle non-existent source`() {
        val nonExistentFilePath = File(tempDir, "nonExistentFile.txt").absolutePath
        val newName = "renamedFile.txt"

        DocumentsManager.renameFileOrDirectory(nonExistentFilePath, newName)

        val renamedFile = File(tempFile, newName)
        assertThat(renamedFile.exists()).isFalse()
    }


    @Test
    fun `existsFile should return false for non-existent file and true for existing file`() {
        val testFile = File(tempDir, "testFile.txt")

        val nonExistentFileResult = DocumentsManager.existsFile(testFile.absolutePath)
        assertThat(nonExistentFileResult).isFalse()

        testFile.createNewFile()

        val existentFileResult = DocumentsManager.existsFile(testFile.absolutePath)

        assertThat(existentFileResult).isTrue()
    }

    @Test
    fun `existsFile should return false for non-existent directory and true for existing directory`() {
        val testDir = File(tempDir, "testDir")

        val nonExistentDirectoryResult = DocumentsManager.existsFile(testDir.absolutePath)
        assertThat(nonExistentDirectoryResult).isFalse()

        testDir.mkdir()

        val existentDirectoryResult = DocumentsManager.existsFile(testDir.absolutePath)

        assertThat(existentDirectoryResult).isTrue()

    }


    @Test
    fun `existsFile should handle invalid paths`() {
        val invalidPath = "/invalid/path"

        val result = DocumentsManager.existsFile(invalidPath)

        assertThat(result).isFalse()

    }

    @After
    fun tearDown() {
        unmockkAll()
        tempFile.deleteRecursively()
        tempDir.deleteRecursively()
    }

}