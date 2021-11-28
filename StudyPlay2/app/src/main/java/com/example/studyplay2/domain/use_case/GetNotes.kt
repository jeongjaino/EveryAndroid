package com.example.studyplay2.domain.use_case

import com.example.studyplay2.domain.Note
import com.example.studyplay2.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotes (
        private val repository: NoteRepository
){
    operator fun invoke(): Flow<List<Note>> {

    }
}