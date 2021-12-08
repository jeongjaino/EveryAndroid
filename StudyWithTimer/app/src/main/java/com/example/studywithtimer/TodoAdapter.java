package com.example.studywithtimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class TodoAdapter extends BaseAdapter {


    Context mContext = null;
    LayoutInflater layoutInflater = null;
    ArrayList<TodoItem> todoItemArrayList = new ArrayList<TodoItem>();

    public TodoAdapter(Context context, ArrayList<TodoItem> todoItemArrayList){
        this.mContext = context;
        this.todoItemArrayList = todoItemArrayList;
        layoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return todoItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View item_view = layoutInflater.inflate(R.layout.todo_list_item, null);

        TextView text = (TextView) item_view.findViewById(R.id.item_todo_text);
        CheckBox check = (CheckBox) item_view.findViewById(R.id.checkBox);

        if(todoItemArrayList.get(position).getChecked() == 1){
            check.setChecked(true);
        }
        else{
            check.setChecked(false);
        }

        text.setText(todoItemArrayList.get(position).getText());

        return item_view;
    }
}
