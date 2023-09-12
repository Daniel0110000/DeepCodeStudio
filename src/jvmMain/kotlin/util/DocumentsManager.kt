package util

import java.io.File

object DocumentsManager {
    /**
     * Retrieve the path of the user's home directory
     *
     * @return The path of the user's home directory
     */
    fun getUserHome(): String = System.getProperty("user.home")

    /**
     * Creates the default projects directory in the user's home directory if it doesn't exist
     */
    fun createDefaultProjectsDirectory(){
        val projectsDirectory = File("${getUserHome()}/${Constants.DEFAULT_PROJECTS_DIRECTORY_NAME}")
        if(!projectsDirectory.exists()){
            kotlin.runCatching { projectsDirectory.mkdir() }
                .onFailure { exception -> println("An error occurred while creating the projects directory: ${exception.message}") }
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
}