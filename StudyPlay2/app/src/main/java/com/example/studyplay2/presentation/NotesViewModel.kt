package com.example.studyplay2.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyplay2.domain.model.Note
import com.example.studyplay2.domain.use_case.NoteUseCases
import com.example.studyplay2.domain.utils.NoteOrder
import com.example.studyplay2.domain.utils.OrderType
import com.example.studyplay2.presentation.note.NoteEvent
import com.example.studyplay2.presentation.note.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NoteState())
    val state : State<NoteState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNoteJob: Job? =  null

    init{
        getNotes(NoteOrder.Date(OrderType.Descending))
    }
    fun onEvent(event: NoteEvent){
        when(event){
            is NoteEvent.Order ->{
                if(state.value.noteOrder::class == event.noteOrder::class &&
                        state.value.noteOrder.orderType == event.noteOrder.orderType){
                    return
                }
            }
            is NoteEvent.DeleteNote ->{
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note // delete후 save
                }
            }
            is NoteEvent.RestoreNote ->{
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NoteEvent.ToggleOrderSection->{
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }
    private fun getNotes(noteOrder: NoteOrder){
        getNoteJob?.cancel()
        getNoteJob = noteUseCases.getNotes(noteOrder)
            .onEach{ notes->
                _state.value =state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}