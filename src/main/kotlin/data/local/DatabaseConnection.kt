package data.local

import org.jetbrains.exposed.sql.Database

/**
 * Creating a connection to the database.
 */
fun databaseConnection(){
    Database.connect(
        "jdbc:sqlite:src/main/kotlin/data/local/database/Settings.db",
        "org.sqlite.JDBC"
    )
}