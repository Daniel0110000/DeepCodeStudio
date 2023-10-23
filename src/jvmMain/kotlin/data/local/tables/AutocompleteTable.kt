package data.local.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * An object representing a database table for storing autocomplete options
 */
object AutocompleteTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val optionName: Column<String> = varchar("optionName", 100)
    val jsonPath: Column<String> = varchar("jsonPath", 1000)

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "ID")
}