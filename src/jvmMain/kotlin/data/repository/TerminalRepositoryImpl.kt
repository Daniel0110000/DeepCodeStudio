package data.repository

import data.local.databaseConnection
import data.local.tables.CommandHistoryTable
import domain.repository.TerminalRepository
import domain.utilies.CallHandler
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TerminalRepositoryImpl: TerminalRepository {

    init {
        // Execute the database connection
        databaseConnection()
        // Creating the table
        transaction { SchemaUtils.create(CommandHistoryTable) }
    }

    /**
     * Inserts the command into the command history in the database
     *
     * @param command Command to insert
     */
    override suspend fun addCommand(command: String) {
        CallHandler.callHandler {
            transaction {
                CommandHistoryTable.insert {
                    it[CommandHistoryTable.command] = command
                }
            }
        }
    }

    /**
     * Retrieves all the history of executed commands
     *
     * @return A list of strings containing the entire history of commands
     */
    override fun getAllCommandHistory(): List<String> = transaction {
        CommandHistoryTable
            .selectAll()
            .map { it[CommandHistoryTable.command] }
            .reversed()
    }
}