package com.dr10.database.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    "color_schemes",
    foreignKeys = [
        ForeignKey(
            entity = SyntaxAndSuggestionsEntity::class,
            parentColumns = ["unique_id"],
            childColumns = ["unique_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("unique_id")]
)
data class ColorSchemesEntity(
    @PrimaryKey @ColumnInfo("unique_id") val uniqueId: String,
    @ColumnInfo("simple_color") val simpleColor: String,
    @ColumnInfo("comment_color") val commentColor: String,
    @ColumnInfo("reserved_word_color") val reservedWordColor: String,
    @ColumnInfo("reserved_word_2_color") val reservedWord2Color: String,
    @ColumnInfo("hexadecimal_color") val hexadecimalColor: String,
    @ColumnInfo("number_color") val numberColor: String,
    @ColumnInfo("function_color") val functionColor: String,
    @ColumnInfo("string_color") val stringColor: String,
    @ColumnInfo("data_type_color") val dataTypeColor: String,
    @ColumnInfo("variable_color") val variableColor: String,
    @ColumnInfo("operator_color") val operatorColor: String,
    @ColumnInfo("processor_color") val processorColor: String
)
