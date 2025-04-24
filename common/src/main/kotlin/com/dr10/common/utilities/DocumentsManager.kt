package com.dr10.common.utilities

import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object DocumentsManager {

    private val projectsDirectory = File("${getUserHome()}/${Constants.DEFAULT_PROJECTS_DIRECTORY_NAME}")
    val localDirectory = File("${getUserHome()}/${if (OsUtils.isWindows) Constants.DEFAULT_WINDOWS_LOCAL_DIRECTORY_NAME else Constants.DEFAULT_LINUX_LOCAL_DIRECTORY_NAME}")
    val javaFilesDirectory = File("${localDirectory}/${Constants.JAVA_DIRECTORY_NAME}")
    val classFilesDirectory = File("${localDirectory}/${Constants.CLASSES_DIRECTORY_NAME}")

    /**
     * Retrieve the path of the user's home directory
     *
     * @return The path of the user's home directory
     */
    fun getUserHome(): String = System.getProperty("user.home")

    /**
     * Creates the necessary directories if they do not exist
     */
    fun createNecessaryDirectories(){
        if(!projectsDirectory.exists()){
            kotlin.runCatching { projectsDirectory.mkdir() }
                .onFailure { exception -> println("An error occurred while creating the projects directory: ${exception.message}") }
        }
        if(!localDirectory.exists()) {
            kotlin.runCatching { localDirectory.mkdir() }
                .onFailure { exception -> println("An error occurred while creating the local directory: ${exception.message}") }
        }
    }

    /**
     * Writes the given content to a file
     *
     * @param file The File object representing the file to write to
     * @param content The content to be written to the file
     * @return `true` if the writing was successful, `false` otherwise
     */
    fun writeFile(file: File, content: String): Boolean?{
        return runCatching {
            file.bufferedWriter().use { writer -> writer.write(content)}
            true
        }
            .onFailure { exception ->
                println("An error occurred while writing to the file: ${exception.message}")
                return false
            }
            .getOrNull()
    }

    /**
     * Delete a file or directory specified by the [file] parameter
     *
     * @param file The file or directory to delete
     */
    fun deleteFileOrDirectory(file: File){
        if(file.isDirectory) FileUtils.deleteDirectory(file)
        else FileUtils.delete(file)
    }

    /**
     * Creates a new directory with the specified [name] at the given [path]
     *
     * @param path The path where the new directory will be created
     * @param name
     *  The name of the new directory
     */
    fun createDirectory(path: String, name: String){
        val directory = File("$path/$name")
        if(!directory.exists()) directory.mkdir()
    }

    /**
     * Creates a new file with the specified [name] at the given [path]
     *
     * @param path The path where the new file will be created
     * @param name The name of the new file
     */
    fun createFile(path: String, name: String){
        val file = File("$path/$name")
        try { if(!file.exists()) file.createNewFile() }
        catch (e: Exception){ e.printStackTrace() }
    }

    /**
     * Renames a file or directory located at the specified [path] to the new [newName]
     *
     * @param path The path to the file or directory to be renamed
     * @param newName The new name for the file or directory
     */
    fun renameFileOrDirectory(path: String, newName: String){
        val source = File(path)
        val destination = File(source.parent, newName)

        try{ if(source.exists()) source.renameTo(destination) }
        catch (e: Exception){ e.printStackTrace() }
    }

    /**
     * Checks if a file exists at the specified path
     *
     * @param path The path of the file to check for existence
     * @return 'true' if the file exists, 'false' otherwise
     */
    fun existsFile(path: String): Boolean =
        Files.exists(Paths.get(path))

    /**
     * Deletes generated JFlex, Java, and class files associated with a specific class name
     *
     * @param className The name of the class for which generated files will be deleted
     */
    fun deleteGeneratedFiles(className: String){
        val jFlexFilePath = File("${localDirectory}/${className}${Constants.JFLEX_EXTENSION}")
        val javaFilePath = File("${javaFilesDirectory}/${className}${Constants.JAVA_EXTENSION}")
        val classFilePath = File("${classFilesDirectory}/${className}${Constants.JAVA_CLASS_EXTENSION}")
        deleteFileOrDirectory(jFlexFilePath)
        deleteFileOrDirectory(javaFilePath)
        deleteFileOrDirectory(classFilePath)
    }
}