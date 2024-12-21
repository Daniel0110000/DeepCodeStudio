package com.dr10.database.data.repositories

import com.dr10.common.models.RegexRuleModel
import com.dr10.database.data.mappers.toEntity
import com.dr10.database.data.mappers.toModel
import com.dr10.database.data.room.Queries
import com.dr10.database.data.room.entities.RegexRulesEntity
import com.dr10.database.domain.repositories.RegexRulesRepository

class RegexRulesRepositoryImpl(
    private val queries: Queries
): RegexRulesRepository {

    override suspend fun insertRegexRule(regexRuleModel: RegexRuleModel) {
        queries.insertRegexRule(regexRuleModel.toEntity())
    }

    override suspend fun deleteRegexRule(model: RegexRuleModel) {
        queries.deleteRegexRule(model.toEntity())
    }

    override suspend fun getRegexRulesByUniqueId(uniqueId: String): List<RegexRuleModel> =
        queries.getRegexRulesByUniqueId(uniqueId).map(RegexRulesEntity::toModel)

}