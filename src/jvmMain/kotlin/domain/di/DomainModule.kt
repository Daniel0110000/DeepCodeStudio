package domain.di

import data.repository.SettingRepositoryImpl
import domain.repository.SettingRepository
import org.koin.dsl.module

/**
 * This is a Koin module for configuring and defining dependencies related to the domain layer
 */
val domainModule = module {
    single<SettingRepository> { SettingRepositoryImpl() }
}