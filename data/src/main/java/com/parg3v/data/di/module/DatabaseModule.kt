package com.parg3v.data.di.module

import android.content.Context
import androidx.room.Room
import com.parg3v.data.local.GestureLogDao
import com.parg3v.data.local.GestureLogDatabase
import com.parg3v.data.repository.GestureLogRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
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
    fun provideGestureLogDao(database: GestureLogDatabase): GestureLogDao {
        return database.gestureLogDao()
    }

    @Provides
    @Singleton
    fun provideGestureLogRepository(dao: GestureLogDao): GestureLogRepositoryImpl {
        return GestureLogRepositoryImpl(dao)
    }
}