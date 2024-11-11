package com.dr10.terminal.model

import com.dr10.common.utilities.DocumentsManager
import com.dr10.terminal.utils.ShellType

data class ShellInfo(
    val terminalID: String = "",
    val name: String,
    val type: ShellType,
    val command: List<String>,
    val environment: Map<String, String> = System.getenv(),
    val path: String = DocumentsManager.getUserHome()
)