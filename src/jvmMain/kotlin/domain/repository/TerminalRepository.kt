package domain.repository

interface TerminalRepository {
    suspend fun addCommand(command: String)

    fun getAllCommandHistory(): List<String>
}