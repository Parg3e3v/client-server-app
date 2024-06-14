package com.parg3v.data.di.module

import com.parg3v.data.repository.ServerRepositoryImpl
import com.parg3v.domain.repository.ServerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModules {
    @Binds
    @Singleton
    abstract fun bindServerRepository(repository: ServerRepositoryImpl): ServerRepository
}