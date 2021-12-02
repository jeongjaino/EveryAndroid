package com.example.studyplay2.presentation.note.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studyplay2.domain.utils.NoteOrder
import com.example.studyplay2.domain.utils.OrderType

@Composable
fun OrderSection (
    modifier : Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange : (NoteOrder) ->Unit
){
    Column(
        modifier = modifier
    ){
        Row(
            modifier= Modifier.fillMaxWidth()
        ){
            DefaultRadioButton(
                text = "Title",
                selected = noteOrder is NoteOrder.Title ,
                OnSelect = { onOrderChange(
                NoteOrder.Title(noteOrder.orderType)
            )}
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Title",
                selected = noteOrder is NoteOrder.Date ,
                OnSelect = { onOrderChange(
                    NoteOrder.Title(noteOrder.orderType)
                )}
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                DefaultRadioButton(
                    text = "Ascending",
                    selected = noteOrder.orderType is OrderType.Ascending ,
                    OnSelect = {
                        onOrderChange(noteOrder.copy(OrderType.Ascending))
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                        text = "Title",
                selected = noteOrder.orderType is OrderType.Descending ,
                OnSelect = {
                    onOrderChange(noteOrder.copy(OrderType.Ascending))
                }
                )
            }
        }
    }
}