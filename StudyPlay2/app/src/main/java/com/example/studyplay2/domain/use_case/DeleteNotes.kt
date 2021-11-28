package com.example.studyplay2.domain.use_case

import com.example.studyplay2.domain.Note
import com.example.studyplay2.domain.repository.NoteRepository

class DeleteNotes (
    private val noteRepository: NoteRepository
    ){
    suspend operator fun invoke(note: Note){
        noteRepository.deleteNote(note)
    }
}