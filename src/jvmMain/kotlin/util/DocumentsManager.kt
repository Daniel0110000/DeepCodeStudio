package util

object DocumentsManager {
    /**
     * Retrieve the path of the user's home directory
     *
     * @return The path of the user's home directory
     */
    fun getUserHome(): String = System.getProperty("user.home")
}