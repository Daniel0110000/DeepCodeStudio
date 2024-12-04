package com.dr10.database.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Upsert
import com.dr10.database.data.room.entities.ColorSchemesEntity
import com.dr10.database.data.room.entities.SelectedConfigHistoryEntity
import com.dr10.database.data.room.entities.SyntaxAndSuggestionsEntity
import com.dr10.database.data.room.relations.ColorSchemeRelation
import com.dr10.database.data.room.relations.SelectedConfigRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface Queries {

    @Upsert
    suspend fun insertSyntaxAndSuggestion(syntaxAndSuggestionsEntity: SyntaxAndSuggestionsEntity)

    @Upsert
    suspend fun insertColorScheme(colorSchemesEntity: ColorSchemesEntity)

    @Upsert
    suspend fun insertSelectedConfigHistory(selectedConfigHistoryEntity: SelectedConfigHistoryEntity)

    @Delete
    suspend fun deleteSyntaxAndSuggestion(syntaxAndSuggestionsEntity: SyntaxAndSuggestionsEntity)

    @Query("SELECT * FROM syntax_and_suggestions")
    fun getAllSyntaxAndSuggestions(): Flow<List<SyntaxAndSuggestionsEntity>>

    @Query("SELECT * FROM syntax_and_suggestions")
    suspend fun getAllSyntaxAndSuggestionsAsList(): List<SyntaxAndSuggestionsEntity>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("""
        SELECT cs.*, sas.* FROM color_schemes cs
        INNER JOIN syntax_and_suggestions sas ON cs.unique_id = sas.unique_id
    """)
    fun getAllColorSchemes(): Flow<List<ColorSchemeRelation>>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("""
        SELECT cs.*, sas.* FROM color_schemes cs
        INNER JOIN syntax_and_suggestions sas ON cs.unique_id = sas.unique_id
    """)
    suspend fun getAllColorSchemesAsList(): List<ColorSchemeRelation>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("""
        SELECT s.*, h.*, c.* FROM syntax_and_suggestions s
        INNER JOIN selected_configs_history h ON s.unique_id = h.unique_id
        INNER JOIN color_schemes c ON s.unique_id = c.unique_id
        WHERE h.asm_file_path = :asmFilePath LIMIT 1
    """)
    suspend fun getSelectedConfig(asmFilePath: String): SelectedConfigRelation?

    @Transaction
    @Query("""
        UPDATE color_schemes
        SET
            simple_color = CASE WHEN :simpleColor IS NOT NULL THEN :simpleColor ELSE simple_color END,
            comment_color = CASE WHEN :commentColor IS NOT NULL THEN :commentColor ELSE comment_color END,
            reserved_word_color = CASE WHEN :reservedWordColor IS NOT NULL THEN :reservedWordColor ELSE reserved_word_color END,
            reserved_word_2_color = CASE WHEN :reservedWord2Color IS NOT NULL THEN :reservedWord2Color ELSE reserved_word_2_color END,
            hexadecimal_color = CASE WHEN :hexadecimalColor IS NOT NULL THEN :hexadecimalColor ELSE hexadecimal_color END,
            number_color = CASE WHEN :numberColor IS NOT NULL THEN :numberColor ELSE number_color END,
            function_color = CASE WHEN :functionColor IS NOT NULL THEN :functionColor ELSE function_color END,
            string_color = CASE WHEN :stringColor IS NOT NULL THEN :stringColor ELSE string_color END,
            data_type_color = CASE WHEN :dataTypeColor IS NOT NULL THEN :dataTypeColor ELSE data_type_color END,
            variable_color = CASE WHEN :variableColor IS NOT NULL THEN :variableColor ELSE variable_color END,
            operator_color = CASE WHEN :operatorColor IS NOT NULL THEN :operatorColor ELSE operator_color END,
            processor_color = CASE WHEN :processorColor IS NOT NULL THEN :processorColor ELSE processor_color END
        WHERE unique_id = :uniqueId
    """)
    suspend fun updateColor(
        uniqueId: String,
        simpleColor: String? = null,
        commentColor: String? = null,
        reservedWordColor: String? = null,
        reservedWord2Color: String? = null,
        hexadecimalColor: String? = null,
        numberColor: String? = null,
        functionColor: String? = null,
        stringColor: String? = null,
        dataTypeColor: String? = null,
        variableColor: String? = null,
        operatorColor: String? = null,
        processorColor: String? = null
    )

    @Query("UPDATE selected_configs_history SET unique_id = :uniqueId WHERE asm_file_path = :asmFilePath")
    suspend fun updateSelectedConfig(uniqueId: String, asmFilePath: String)

    @Query("DELETE FROM selected_configs_history WHERE asm_file_path = :asmFilePath")
    suspend fun deleteSelectedConfig(asmFilePath: String)

}