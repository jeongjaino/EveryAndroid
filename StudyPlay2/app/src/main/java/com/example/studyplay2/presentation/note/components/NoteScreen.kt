package com.example.studyplay2.presentation.note.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.studyplay2.presentation.NotesViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
    ){
}