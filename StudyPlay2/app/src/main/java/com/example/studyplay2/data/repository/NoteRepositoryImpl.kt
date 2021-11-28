package com.example.studyplay2.data.repository

import com.example.studyplay2.data.data_resource.NoteDao
import com.example.studyplay2.domain.Note
import com.example.studyplay2.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl (
    private val dao: NoteDao
): NoteRepository{
    override fun getNote(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        return dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        return dao.deleteNote(note)
    }
}