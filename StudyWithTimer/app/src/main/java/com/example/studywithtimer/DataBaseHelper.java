package com.example.studywithtimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.example.studywithtimer.dataclass.TimerItem;
import com.example.studywithtimer.dataclass.TodoItem;

import java.util.ArrayList;

class DataBaseHelper extends SQLiteOpenHelper{

    public static String DATABASE_NAME = "TimeDataBase";

    public static String TIMER_TABLE_NAME = "TimeTable";
    public static String TODO_TABLE_NAME = "TodoTable";

    SQLiteDatabase database;

    public DataBaseHelper(@NonNull Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DropTimeTable(db);
        DropTodoTable(db);
        createTimerTable(db);
        createTodoTable(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    public void createTimerTable(SQLiteDatabase db){
        db.execSQL("create table if not exists " + TIMER_TABLE_NAME + " ("
                + "id integer PRIMARY KEY autoincrement, "
                + "date text, "
                + "time text, "
                + "startTime text, "
                + "endTime text"
                + ")"
        );
    }
    public void timeTableInsertData(String date, String time, String startTime, String endTime) {
        database = this.getWritableDatabase();
        try {
            if (database != null) {
                String sql = "insert into TimeTable(date, time, startTime, endTime) values(?, ?, ?, ?)";
                Object[] params = {date, time, startTime, endTime};
                database.execSQL(sql, params);
                database.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<TimerItem> TimerSelectData() {
        ArrayList<TimerItem> timerItemArrayList = new ArrayList<TimerItem>();
        SQLiteDatabase rd = this.getReadableDatabase();
        try {
            String sql = "select date, time, startTime, endTime from " + TIMER_TABLE_NAME;
            Cursor cursor = rd.rawQuery(sql, null);
            timerItemArrayList = new ArrayList<TimerItem>();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String date = cursor.getString(0);
                String time = cursor.getString(1);
                String startTime = cursor.getString(2);
                String endTime = cursor.getString(3);
                TimerItem timerItem = new TimerItem(date, time, startTime, endTime);

                timerItemArrayList.add(timerItem);
            }
            cursor.close();
            rd.close();
            return timerItemArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return timerItemArrayList;
        }
    }
    public void TimerDeleteData(int position){
        int idIndex;
        database = this.getWritableDatabase();

        String sql1 = "select * from " + TIMER_TABLE_NAME;
        Cursor cursor = database.rawQuery(sql1, null);
        //adapter position 값으로 cursor를 이용해서 id값 찾기
        if(cursor != null && cursor.moveToPosition(position)){
            try{
                idIndex = cursor.getColumnIndex("id");
                int id = cursor.getInt(idIndex);
                String sql = "delete from TimeTable where id ="+ id;
                database.execSQL(sql);
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        database.close();
    }
    public void createTodoTable(SQLiteDatabase db){
        db.execSQL("create table if not exists " + TODO_TABLE_NAME + "("
                + " id integer PRIMARY KEY autoincrement, "
                + " todo text, "
                + " checked integer);"
        );
    }
    public void TodoInsertData(String todo, int checked) {
        database = this.getWritableDatabase();
        try {
            if (database != null) {
                String sql = "insert into TodoTable(todo, checked) values(?, ?)";
                Object[] params = {todo, checked};
                database.execSQL(sql, params);
                database.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<TodoItem> TodoSelectData() {
        ArrayList<TodoItem> todoArrayList = new ArrayList<TodoItem>();
        database = this.getReadableDatabase();
        try {
            String sql = "select todo, checked from " + TODO_TABLE_NAME;
            Cursor cursor = database.rawQuery(sql, null);
            todoArrayList = new ArrayList<TodoItem>();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String date = cursor.getString(0);
                int checked = cursor.getInt(1);
                TodoItem todoItem = new TodoItem(date, checked);
                todoArrayList.add(todoItem);
            }
            cursor.close();
            database.close();
            return todoArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return todoArrayList;
        }
    }
    public void TodoDeleteData(int position){
        database = this.getWritableDatabase();
        int indexId;
        String sql = "select * from " + TODO_TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);
        if(cursor != null && cursor.moveToPosition(position)) {
            try {
                indexId = cursor.getColumnIndex("id");
                int pos = cursor.getInt(indexId);
                String delete = "delete from TodoTable where id =" + pos;
                database.execSQL(delete);
                cursor.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        database.close();
    }
    public void TodoUpdateData(int position, String todo, int checked){
        database = this.getWritableDatabase();
        int indexId;
        ContentValues contentValues = new ContentValues();
        String sql = "select * from " + TODO_TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);
        if(cursor != null && cursor.moveToPosition(position)) {
            try{
                indexId = cursor.getColumnIndex("id");
                int pos = cursor.getInt(indexId);
                contentValues.put("id", pos);
                contentValues.put("todo", todo);
                contentValues.put("checked", checked);
                database.update(TODO_TABLE_NAME, contentValues,"id = ?"
                        ,new String[]{Integer.toString(pos)});
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        database.close();
    }
    public TodoItem todoLoadData(int position){
        TodoItem todoItem = new TodoItem(null, 0);
        database = this.getReadableDatabase();
        String sql = "select * from " + TODO_TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);
        if(cursor != null && cursor.moveToPosition(position)){
            try{
                int todo = cursor.getColumnIndex("todo");
                int check = cursor.getColumnIndex("checked");
                todoItem = new TodoItem(cursor.getString(todo), cursor.getInt(check));
                cursor.close();
                database.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return todoItem;
    }
    public void DropTimeTable(SQLiteDatabase db){
        String sql = "drop table if exists " + TIMER_TABLE_NAME;
        try{
            db.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void DropTodoTable(SQLiteDatabase db){
        String sql = "drop table if exists " + TODO_TABLE_NAME;
        try{
            db.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}