package com.codinghuseyn.exchangemaven.domain.domain_module

import com.codinghuseyn.exchangemaven.data.repository.ExchangeRepositoryImpl
import com.codinghuseyn.exchangemaven.domain.repository.ExchangeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    @Singleton
    abstract fun bindsExchangeRepository(exchangeRepositoryImpl: ExchangeRepositoryImpl): ExchangeRepository
}