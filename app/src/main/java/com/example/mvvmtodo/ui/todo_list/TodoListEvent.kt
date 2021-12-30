package com.example.mvvmtodo.ui.todo_list

import com.example.mvvmtodo.data.Todo

sealed class TodoListEvent{
    data class DeleteTodo(val todo: Todo): TodoListEvent()

    data class OnDoneChange(val todo: Todo, val isDone: Boolean): TodoListEvent()

    object OnUndoDeleteClick: TodoListEvent()

    data class OnTodoClick(val todo: Todo): TodoListEvent()

    object OnAddTodoClick: TodoListEvent()

}