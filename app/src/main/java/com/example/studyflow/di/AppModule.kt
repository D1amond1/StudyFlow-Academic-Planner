package com.example.studyflow.di

import android.app.Application
import androidx.room.Room
import com.example.studyflow.data.local.AppDatabase
import com.example.studyflow.data.local.dao.EventDao
import com.example.studyflow.data.local.dao.NoteDao
import com.example.studyflow.data.repository.StudyFlowRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "studyflow_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: AppDatabase): NoteDao {
        return db.noteDao()
    }

    @Provides
    @Singleton
    fun provideEventDao(db: AppDatabase): EventDao {
        return db.eventDao()
    }

    @Provides
    @Singleton
    fun provideStudyFlowRepository(noteDao: NoteDao, eventDao: EventDao): StudyFlowRepository {
        return StudyFlowRepository(noteDao, eventDao)
    }
}