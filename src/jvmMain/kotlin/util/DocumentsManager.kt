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
}