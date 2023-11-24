package data.local.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * Object representing a database table for storing command history
 */
object CommandHistoryTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val command: Column<String> = varchar("command", 1000)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}