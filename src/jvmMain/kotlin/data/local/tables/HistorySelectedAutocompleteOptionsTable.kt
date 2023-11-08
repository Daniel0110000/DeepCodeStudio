package data.local.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * An object representing a database table for storing history of selected autocomplete options
 */
object HistorySelectedAutocompleteOptionsTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val uuid: Column<String> = varchar("uuid", 200)
    val asmFilePath: Column<String> = varchar("asmFilePath", 1000)
    val optionName: Column<String> = varchar("optionName", 100)
    val jsonPath: Column<String> = varchar("jsonPath", 1000)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}