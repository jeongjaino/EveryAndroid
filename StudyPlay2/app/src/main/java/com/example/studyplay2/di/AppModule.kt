package com.example.studyplay2.di

import android.app.Application
import androidx.room.Room
import com.example.studyplay2.data.data_resource.NoteDataBase
import com.example.studyplay2.data.repository.NoteRepositoryImpl
import com.example.studyplay2.domain.repository.NoteRepository
import com.example.studyplay2.domain.use_case.DeleteNotes
import com.example.studyplay2.domain.use_case.GetNotes
import com.example.studyplay2.domain.use_case.NoteUseCases
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
    fun provideNoteDataBase(app: Application): NoteDataBase{
        return Room.databaseBuilder(
            app,
            NoteDataBase::class.java,
            NoteDataBase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDataBase): NoteRepository{
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases{
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNotes(repository)
        )
    }
}