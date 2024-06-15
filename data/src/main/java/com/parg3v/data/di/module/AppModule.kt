package com.parg3v.data.di.module

import android.content.Context
import androidx.room.Room
import com.parg3v.data.gesture.GestureHandler
import com.parg3v.data.local.GestureLogDao
import com.parg3v.data.local.GestureLogDatabase
import com.parg3v.data.repository.ClientRepositoryImpl
import com.parg3v.data.repository.DataStoreRepositoryImpl
import com.parg3v.domain.repository.ClientRepository
import com.parg3v.domain.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): GestureLogDatabase {
        return Room.databaseBuilder(
            app,
            GestureLogDatabase::class.java,
            "gesture_log_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGestureLogDao(database: GestureLogDatabase): GestureLogDao {
        return database.gestureLogDao()
    }

    @Provides
    @Singleton
    fun provideClientRepository(
        @ApplicationContext app: Context,
        gestureHandler: GestureHandler,
        gestureLogDao: GestureLogDao
    ): ClientRepository = ClientRepositoryImpl(app, gestureLogDao, gestureHandler)


    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepository = DataStoreRepositoryImpl(app)
}