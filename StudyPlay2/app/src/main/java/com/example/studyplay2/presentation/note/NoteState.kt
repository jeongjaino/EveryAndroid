package com.example.studyplay2.presentation.note

import com.example.studyplay2.domain.model.Note
import com.example.studyplay2.domain.utils.NoteOrder
import com.example.studyplay2.domain.utils.OrderType

data class NoteState (
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false

)