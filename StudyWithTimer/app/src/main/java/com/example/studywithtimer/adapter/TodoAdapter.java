package com.example.studywithtimer.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.studywithtimer.R;
import com.example.studywithtimer.dataclass.TodoItem;

import java.util.ArrayList;

public class TodoAdapter extends BaseAdapter implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private OnItemClickListener onItemClickListener;
    private OnItemClickListener onCheckedChangeListener;

    Context mContext = null;
    LayoutInflater layoutInflater = null;
    ArrayList<TodoItem> todoItemArrayList = new ArrayList<TodoItem>();

    public TodoAdapter(Context context, ArrayList<TodoItem> todoItemArrayList, OnItemClickListener clickListener){
        this.mContext = context;
        this.todoItemArrayList = todoItemArrayList;
        onItemClickListener = clickListener;
        onCheckedChangeListener = clickListener;
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
        CardView cardView = (CardView) item_view.findViewById(R.id.cardView);

        if(todoItemArrayList.get(position).getChecked() == 1){
            check.setChecked(true);
        }
        else{
            check.setChecked(false);
        }
        text.setText(todoItemArrayList.get(position).getText());

        ContentValues contentValues = new ContentValues();
        contentValues.put("0", position);
        contentValues.put("1", todoItemArrayList.get(position).getText());
        contentValues.put("2", todoItemArrayList.get(position).getChecked());

        check.setTag(contentValues);
        check.setOnCheckedChangeListener(this);
      //  cardView.setTag(position);
        //cardView.setOnClickListener(this);

        return item_view;
    }
    public interface OnItemClickListener{
        public void onCardViewClickListener(int position);
        public void onCheckBoxClickListener(int position, String text, int checked);
    }
    @Override
    public void onClick(View view) {
        if(this.onItemClickListener != null){
            onItemClickListener.onCardViewClickListener((int) view.getTag());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean b) {
        ContentValues contentValues = (ContentValues) view.getTag();
        int position = (int) contentValues.get("0");
        String text = (String) contentValues.get("1");
        int checked = (int) contentValues.get("2");
        if(this.onCheckedChangeListener != null){
            onCheckedChangeListener.onCheckBoxClickListener(position, text, checked);
        }
    }
}
