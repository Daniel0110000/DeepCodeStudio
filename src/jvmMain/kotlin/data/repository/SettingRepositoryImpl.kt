package data.repository

import data.local.databaseConnection
import data.local.tables.AutocompleteTable
import domain.model.AutocompleteOptionModel
import domain.repository.SettingRepository
import domain.util.CallHandler
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class SettingRepositoryImpl: SettingRepository {

    init {
        // Execute the database connection
        databaseConnection()
        // Creating the table [AutocompleteTable]
        transaction { SchemaUtils.create(AutocompleteTable) }
    }

    /**
     * Inserts a new option for autocomplete into the database
     *
     * @param model The data to be inserted into the database
     */
    override suspend fun addAutocompleteOption(model: AutocompleteOptionModel) {
        CallHandler.callHandler {
            transaction {
                AutocompleteTable.insert {
                    it[optionName] = model.optionName
                    it[jsonPath] = model.jsonPath
                }
            }
        }
    }

    /**
     * Deletes an autocomplete option specified by the [model] parameter
     *
     * @param model The autocomplete option to delete
     */
    override suspend fun deleteAutocompleteOption(model: AutocompleteOptionModel) {
        CallHandler.callHandler {
            transaction {
                AutocompleteTable.deleteWhere {
                    (id eq model.id) and (optionName eq model.optionName) and (jsonPath eq model.jsonPath)
                }
            }
        }
    }

    /**
     * Updates the JSOn path pf an autocomplete option specified by the [model] parameter
     *
     * @param jsonPath The new JSOn path to set for the autocomplete option
     * @param model The autocomplete option to delete
     */
    override suspend fun updateAutocompleteOptionJsonPath(jsonPath: String, model: AutocompleteOptionModel) {
        CallHandler.callHandler {
            transaction {
                AutocompleteTable.update({ (AutocompleteTable.id eq model.id) and (AutocompleteTable.optionName eq model.optionName) }){
                    it[AutocompleteTable.jsonPath] = jsonPath
                }
            }
        }
    }

    /**
     * Retrieves all autocomplete options from the database and returns them as a list of [AutocompleteOptionModel]
     *
     * @return A list of AutocompleteOptionModel containing all autocomplete options
     */
    override fun getAllAutocompleteOptions(): List<AutocompleteOptionModel> = transaction {
        AutocompleteTable.selectAll().map {
            AutocompleteOptionModel(it[AutocompleteTable.id], it[AutocompleteTable.optionName], it[AutocompleteTable.jsonPath])
        }
    }
}