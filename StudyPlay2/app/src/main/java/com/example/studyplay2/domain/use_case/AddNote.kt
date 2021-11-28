package com.example.studyplay2.domain.use_case

import com.example.studyplay2.domain.model.InvalidNoteException
import com.example.studyplay2.domain.model.Note
import com.example.studyplay2.domain.repository.NoteRepository

class AddNote (
    private val repository: NoteRepository
    ){

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if(note.title.isBlank()){
            throw InvalidNoteException("The title of Note is empty.")
        }
        if(note.content.isBlank()){
            throw InvalidNoteException("The content of Note is empty.")
        }
        repository.insertNote(note)
    }
}