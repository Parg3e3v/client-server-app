package com.parg3v.domain.module

import com.parg3v.domain.repository.DataStoreRepository
import com.parg3v.domain.repository.GestureLogRepository
import com.parg3v.domain.repository.ServerRepository
import com.parg3v.domain.use_cases.client.GetIpClientAppUseCase
import com.parg3v.domain.use_cases.client.GetPortClientAppUseCase
import com.parg3v.domain.use_cases.client.SaveIpClientAppUseCase
import com.parg3v.domain.use_cases.client.SavePortClientAppUseCase
import com.parg3v.domain.use_cases.server.GetLogsFormDBUseCase
import com.parg3v.domain.use_cases.common.ProvideServerIpUseCase
import com.parg3v.domain.use_cases.server.GetPortServerAppUseCase
import com.parg3v.domain.use_cases.server.SavePortServerAppUseCase
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

    @Provides
    @Singleton
    fun provideGetPortServerAppUseCase(repository: DataStoreRepository): GetPortServerAppUseCase {
        return GetPortServerAppUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSavePortServerAppUseCase(repository: DataStoreRepository): SavePortServerAppUseCase {
        return SavePortServerAppUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPortClientAppUseCase(repository: DataStoreRepository): GetPortClientAppUseCase {
        return GetPortClientAppUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSavePortClientAppUseCase(repository: DataStoreRepository): SavePortClientAppUseCase {
        return SavePortClientAppUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetIpClientAppUseCase(repository: DataStoreRepository): GetIpClientAppUseCase {
        return GetIpClientAppUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveIpClientAppUseCase(repository: DataStoreRepository): SaveIpClientAppUseCase {
        return SaveIpClientAppUseCase(repository)
    }

}