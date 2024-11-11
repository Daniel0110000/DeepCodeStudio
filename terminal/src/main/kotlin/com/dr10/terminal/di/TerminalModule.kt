package com.dr10.terminal.di

import com.dr10.terminal.ui.viewModel.TerminalViewModel
import org.koin.dsl.module

/**
 * Define the [terminalModule] for dependency injection
 */
val terminalModule = module {
    single { TerminalViewModel() }
}