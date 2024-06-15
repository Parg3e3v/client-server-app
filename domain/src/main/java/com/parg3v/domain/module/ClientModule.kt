package com.parg3v.domain.module

import com.parg3v.domain.repository.ClientRepository
import com.parg3v.domain.use_cases.client.StartClientUseCase
import com.parg3v.domain.use_cases.client.StopClientUseCase
import com.parg3v.domain.use_cases.client.ValidateIpAddressUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {
    @Provides
    @Singleton
    fun provideValidateIpAddressUseCase(): ValidateIpAddressUseCase {
        return ValidateIpAddressUseCase()
    }

    @Provides
    @Singleton
    fun provideStartClientUseCase(repository: ClientRepository): StartClientUseCase {
        return StartClientUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideStopClientUseCase(repository: ClientRepository): StopClientUseCase {
        return StopClientUseCase(repository)
    }
}