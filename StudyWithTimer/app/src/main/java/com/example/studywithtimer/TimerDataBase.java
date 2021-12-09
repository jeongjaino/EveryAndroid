package com.example.studywithtimer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

class DataBaseHelper extends SQLiteOpenHelper{

    public static String DATABASE_NAME = "TimeDataBase";

    public static String TIMER_TABLE_NAME = "TimeTable";
    public static String TODO_TABLE_NAME = "TodoTable";

    public DataBaseHelper(@NonNull Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTimerTable();
        createTodoTable();
    }
    public void onOpen(SQLiteDatabase db){
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    public void createTimerTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("create table if not exists " + TIMER_TABLE_NAME + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " date text, "
                + " time text, "
                + " startTime text, "
                + " endTime text);"
        );
    }
    public void timeTableInsertData(String date, String time, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null) {
                String sql = "insert into TimeTable(date, time, startTime, endTime) values(?, ?, ?, ?)";
                Object[] params = {date, time, startTime, endTime};
                db.execSQL(sql, params);
                Log.d("tag","success");
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.d("tag","false");
        }
    }
    public ArrayList<TimerItem> TimerSelectData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TimerItem> timerItemArrayList;
        try {
            String sql = "select date, time, startTime, endTime from " + TIMER_TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null);
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
            return timerItemArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int TimerDeleteData(int position){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TIMER_TABLE_NAME,"_id = ?", new String[]{Integer.toString(position)});
    }
    public void createTodoTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("create table if not exists " + TODO_TABLE_NAME + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " todo text, "
                + " checked integer);"
        );
    }
    public void TodoInsertData(String todo, int checked) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            if (db != null) {
                String sql = "insert into TodoTable(todo, checked) values(?, ?)";
                Object[] params = {todo, checked};
                db.execSQL(sql, params);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<TodoItem> TodoSelectData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TodoItem> todoArrayList;
        try {
            String sql = "select todo, Checked from " + TODO_TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null);
            todoArrayList = new ArrayList<TodoItem>();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String date = cursor.getString(0);
                int checked = cursor.getInt(1);
                TodoItem todoItem = new TodoItem(date, checked);

                todoArrayList.add(todoItem);
            }
            cursor.close();
            return todoArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int TodoDeleteData(int position){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TODO_TABLE_NAME,"_id = ?", new String[]{Integer.toString(position)});
    }
}