package com.example.mvvmtodo.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val title: String,
    val descrption: String?,
    val isDone: Boolean,
    //자동 id 생성하고 증가
    @PrimaryKey
    var id: Int? = null
)
