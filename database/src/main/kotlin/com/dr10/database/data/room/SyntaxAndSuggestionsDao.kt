package com.dr10.database.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SyntaxAndSuggestionsDao {

    @Upsert
    suspend fun insert(syntaxAndSuggestions: SyntaxAndSuggestionsEntity)

    @Query("DELETE FROM syntax_and_suggestions WHERE uniqueId = :uniqueId")
    suspend fun delete(uniqueId: String)

    @Query("SELECT * FROM syntax_and_suggestions")
    fun getAll(): Flow<List<SyntaxAndSuggestionsEntity>>

}