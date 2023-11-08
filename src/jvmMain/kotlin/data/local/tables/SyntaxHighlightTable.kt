package data.local.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * An object representing a database table for storing syntax highlight configurations
 */
object SyntaxHighlightTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val uuid: Column<String> = varchar("uuid", 200)
    val optionName: Column<String> = varchar("optionName", 100)
    val jsonPath: Column<String> = varchar("jsonPath", 1000)
    val simpleColor: Column<String> = varchar("simpleColor", 10)
    val instructionColor: Column<String> = varchar("instructionColor", 10)
    val variableColor: Column<String> = varchar("variableColor", 10)
    val constantColor: Column<String> = varchar("constantColor", 10)
    val numberColor: Column<String> = varchar("numberColor", 10)
    val segmentColor: Column<String> = varchar("segmentColor", 10)
    val systemCallColor: Column<String> = varchar("systemCallColor", 10)
    val registerColor: Column<String> = varchar("registerColor", 10)
    val arithmeticInstructionColor: Column<String> = varchar("arithmeticInstructionColor", 10)
    val logicalInstructionColor: Column<String> = varchar("logicalInstructionColor", 10)
    val conditionColor: Column<String> = varchar("conditionColor", 10)
    val loopColor: Column<String> = varchar("loopColor", 10)
    val memoryManagementColor: Column<String> = varchar("memoryManagement", 10)
    val commentColor: Column<String> = varchar("commentColor", 10)
    val stringColor: Column<String> = varchar("stringColor", 10)
    val labelColor: Column<String> = varchar("labelColor", 10)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}