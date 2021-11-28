package com.example.studyplay2.data.data_resource

import androidx.room.*
import com.example.studyplay2.domain.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {


    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}