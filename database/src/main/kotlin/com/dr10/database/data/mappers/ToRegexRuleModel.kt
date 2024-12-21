package com.dr10.database.data.mappers

import com.dr10.common.models.RegexRuleModel
import com.dr10.database.data.room.entities.RegexRulesEntity

fun RegexRulesEntity.toModel(): RegexRuleModel = RegexRuleModel(
    id = this.id,
    uniqueId = this.uniqueId,
    regexName = this.regexName,
    regexPattern = this.regexPattern
)