package com.example.studyplay2.data.data_resource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.studyplay2.domain.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDataBase : RoomDatabase(){
    abstract val noteDao : NoteDao

    companion object{
        const val DATABASE_NAME = "note_db"
    }
}