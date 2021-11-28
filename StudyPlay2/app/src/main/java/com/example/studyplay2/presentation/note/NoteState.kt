package com.example.studyplay2.presentation.note

import com.example.studyplay2.domain.utils.NoteOrder
import com.example.studyplay2.domain.utils.OrderType
import org.w3c.dom.Node

data class NoteState (
    val notes: List<Node> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false

)