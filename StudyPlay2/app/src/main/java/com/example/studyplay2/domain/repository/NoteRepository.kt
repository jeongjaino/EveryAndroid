package com.example.studyplay2.domain.repository

import com.example.studyplay2.domain.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNote(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}