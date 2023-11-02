package data.local.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * An object representing a database table for storing syntax highlight configurations
 */
object SyntaxHighlightTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val optionName: Column<String> = varchar("optionName", 100)
    val jsonPath: Column<String> = varchar("jsonPath", 1000)
    val keywordColor: Column<String> = varchar("keywordColor", 10)
    val variableColor: Column<String> = varchar("variableColor", 10)
    val numberColor: Column<String> = varchar("numberColor", 10)
    val sectionColor: Column<String> = varchar("sectionColor", 10)
    val commentColor: Column<String> = varchar("commentColor", 10)
    val stringColor: Column<String> = varchar("stringColor", 10)
    val labelColor: Column<String> = varchar("labelColor", 10)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}