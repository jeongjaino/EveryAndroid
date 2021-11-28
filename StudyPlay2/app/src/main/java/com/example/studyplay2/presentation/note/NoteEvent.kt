package com.example.studyplay2.presentation.note

import com.example.studyplay2.domain.model.Note
import com.example.studyplay2.domain.utils.NoteOrder

sealed class NoteEvent {

    data class Order(val noteOrder : NoteOrder): NoteEvent()
    data class DeleteNote(val note: Note): NoteEvent()
    object RestoreNote: NoteEvent()
    object ToggleOrderSection: NoteEvent()
}