package com.dr10.database.data.mappers

import com.dr10.common.models.RegexRuleModel
import com.dr10.database.data.room.entities.RegexRulesEntity

fun RegexRuleModel.toEntity(): RegexRulesEntity = RegexRulesEntity(
    id = this.id,
    uniqueId = this.uniqueId,
    regexName = this.regexName,
    regexPattern = this.regexPattern
)