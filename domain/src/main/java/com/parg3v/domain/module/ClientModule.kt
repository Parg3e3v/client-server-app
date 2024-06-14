package com.parg3v.domain.module

import com.parg3v.domain.use_cases.ValidateIpAddressUseCase
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
}