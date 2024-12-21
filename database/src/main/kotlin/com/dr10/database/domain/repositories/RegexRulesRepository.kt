package com.dr10.database.domain.repositories

import com.dr10.common.models.RegexRuleModel

interface RegexRulesRepository {

    suspend fun insertRegexRule(regexRuleModel: RegexRuleModel)

    suspend fun deleteRegexRule(model: RegexRuleModel)

    suspend fun getRegexRulesByUniqueId(uniqueId: String): List<RegexRuleModel>

}