package com.example.cleanarcitecturenote.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelabs.state.todo.*
import com.codelabs.state.util.generateRandomTodoItem
import kotlin.random.Random

@Composable
fun TodoScreen(
   items: List<TodoItem>,
   currentlyEditing : TodoItem?,
   onAddItem : (TodoItem) -> Unit,
   onRemoveItem : (TodoItem) -> Unit,
   onStartEdit : (TodoItem) -> Unit,
   onEditItemChange : (TodoItem) -> Unit,
   onEditDone: () -> Unit
){
    Column{
        val enableTopSection = currentlyEditing == null
        TodoItemInputBackground(elevate = enableTopSection) {
            if(enableTopSection){
                TodoItemEntryInput(onAddItem)
            } else{
                Text(
                    "수정 화면입니당. ",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp)
    ){
        items(items){ todo ->
            if(currentlyEditing?.id == todo.id){
                TodoItemInlineEditor(
                    item = currentlyEditing,
                    onEditItemChange = onEditItemChange,
                    onEditDone = { onEditDone },
                    onRemoveItem = {onRemoveItem(todo)}
                )
            } else{
                TodoRow(
                    todo,
                    { onStartEdit(it)},
                    Modifier.fillParentMaxWidth()
                )
            }
        }
    }
}
@Composable
fun TodoRow(todo: TodoItem,
            onItemClicked : (TodoItem) -> Unit,
            modifier : Modifier =Modifier,
            iconAlpha : Float = remember(todo.id) { randomTint()}){
    Row(
        modifier = modifier
            .clickable { onItemClicked(todo) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(todo.task)
        Icon(
            imageVector = todo.icon.imageVector,
            tint = LocalContentColor.current.copy(alpha = iconAlpha),
            contentDescription = stringResource(id = todo.icon.contentDescription)
        )
    }
}

private fun randomTint(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}
@Composable
fun TodoItemEntryInput(onItemComplete: (TodoItem) ->Unit){
    val (text, setText) = remember { mutableStateOf("")}
    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.Default)}
    val iconsVisible = text.isNotBlank()
    val submit = {
        onItemComplete(TodoItem(text, icon)) //callback 호출
        setIcon(TodoIcon.Default) //입력하고 icon 제자리
        setText("") // 텍스트 지우기
    }
    TodoItemInput(
        text = text,
        onTextChange = setText,
        icon = icon,
        onIconChange = setIcon,
        submit = submit,
        iconsVisible = iconsVisible
    )
}
@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) ->Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    submit: () -> Unit,
    iconsVisible: Boolean
){
    Column{
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ){
            TodoInputText(
                text,
                onTextChange,
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                submit
            ) //keyboard에 enter = onImeAction

            TodoEditButton(
                onClick = submit,
                text = "ADD",
                modifier = Modifier.align(Alignment.CenterVertically),
                enabled = text.isNotBlank()
            )
        }
        if(iconsVisible){
            AnimatedIconRow(icon, onIconChange, Modifier.padding(top = 8.dp) )
        }else{
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: (TodoItem) -> Unit
) = TodoItemInput(
    text = item.task,
    onTextChange = { onEditItemChange(item.copy(task = it))},
    icon = item.icon,
    onIconChange = { onEditItemChange(item.copy(icon = it))},
    submit = onEditDone,
    iconsVisible = true
)
/*
@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    TodoScreen(items, {}, {})
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todo = remember { generateRandomTodoItem() }
    TodoRow(todo = todo, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}*/