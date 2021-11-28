package com.example.studyplay2.domain.use_case

import com.example.studyplay2.domain.Note
import com.example.studyplay2.domain.repository.NoteRepository
import com.example.studyplay2.domain.utils.NoteOrder
import com.example.studyplay2.domain.utils.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes (
        private val repository: NoteRepository
){
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNote().map{ notes->
            when(noteOrder.orderType){
                is OrderType.Descending ->{
                    when(noteOrder){
                        is NoteOrder.Date -> notes.sortedBy { it.timeStamp }
                        is NoteOrder.Title ->  notes.sortedBy { it.title.lowercase() }
                    }
                }
                is OrderType.Ascending ->{
                    when(noteOrder){
                        is NoteOrder.Date -> notes.sortedBy { it.timeStamp }
                        is NoteOrder.Title ->  notes.sortedBy { it.title.lowercase() }
                    }
                }
            }
        }
    }
}