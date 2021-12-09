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

    SQLiteDatabase database;

    public DataBaseHelper(@NonNull Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTimerTable(db);
        createTodoTable(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    public void createTimerTable(SQLiteDatabase db){
        db.execSQL("create table if not exists " + TIMER_TABLE_NAME + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " date text, "
                + " time text, "
                + " startTime text, "
                + " endTime text);"
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
                Log.d("tag","success");
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.d("tag","false");
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
        database = this.getWritableDatabase();
        database.delete(TIMER_TABLE_NAME,"_id = ?", new String[]{Integer.toString(position)});
        database.close();
    }
    public void createTodoTable(SQLiteDatabase db){
        db.execSQL("create table if not exists " + TODO_TABLE_NAME + "("
                + " _id integer PRIMARY KEY autoincrement, "
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
            String sql = "select todo, Checked from " + TODO_TABLE_NAME;
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
        database.delete(TODO_TABLE_NAME,"_id = ?", new String[]{Integer.toString(position)});
        database.close();
    }
}