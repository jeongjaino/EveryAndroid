package com.example.studywithtimer;

public class TodoItem {

    String text;
    int checked; //true = 1, false = 0

    public TodoItem(String text, int checked){
        this.text = text;
        this.checked = checked;
    }
    public String getText(){
        return text;
    }
    public int getChecked(){
        return checked;
    }
}
