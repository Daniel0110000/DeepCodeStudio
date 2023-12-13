package domain.repositories

import domain.model.SyntaxHighlightConfigModel

interface SyntaxHighlightSettingsRepository {
    suspend fun createSyntaxHighlightConfig(model: SyntaxHighlightConfigModel)

    fun getAllSyntaxHighlightConfigs(): List<SyntaxHighlightConfigModel>

    fun getSyntaxHighlightConfig(uuid: String): SyntaxHighlightConfigModel

    suspend fun updateSyntaxHighlightConfig(model: SyntaxHighlightConfigModel)

    suspend fun deleteSyntaxHighlightConfig(uuid: String)

    // Change the name
    suspend fun updateSyntaxHighlightConfigJsonPath(jsonPath: String, uuid: String)
}