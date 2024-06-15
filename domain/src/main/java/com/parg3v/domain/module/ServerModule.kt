package com.parg3v.domain.module

import com.parg3v.domain.repository.GestureLogRepository
import com.parg3v.domain.repository.ServerRepository
import com.parg3v.domain.use_cases.server.GetLogsFormDBUseCase
import com.parg3v.domain.use_cases.common.ProvideServerIpUseCase
import com.parg3v.domain.use_cases.server.StartServerUseCase
import com.parg3v.domain.use_cases.server.StopServerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServerModule {
    @Provides
    @Singleton
    fun provideStartServerUseCase(serverRepository: ServerRepository): StartServerUseCase {
        return StartServerUseCase(serverRepository)
    }

    @Provides
    @Singleton
    fun provideStopServerUseCase(serverRepository: ServerRepository): StopServerUseCase {
        return StopServerUseCase(serverRepository)
    }

    @Provides
    @Singleton
    fun provideServerIpUseCase(): ProvideServerIpUseCase {
        return ProvideServerIpUseCase()
    }

    @Provides
    @Singleton
    fun provideGetLogsFromDBUseCase(repository: GestureLogRepository): GetLogsFormDBUseCase {
        return GetLogsFormDBUseCase(repository)
    }

}