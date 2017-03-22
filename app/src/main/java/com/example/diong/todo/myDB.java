package com.example.diong.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diong on 2016/12/4.
 */
 public class myDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "Todo_Storage";
    private static final String TABLE_NAME = "TodoItems";

    public myDB(Context context) {
        super(context,DB_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE if not exists "
                + TABLE_NAME
                +" (_id INTEGER PRIMARY KEY, content TEXT, year INTEGER,month INTEGER,day INTEGER,hour INTEGER, minute INTEGER,important INTEGER,finish INTEGER, alarmOp TEXT, beforeTime INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i, int il){
    }
    public void insertEntry(TodoItem i) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("content", i.getToDoContent());
        cv.put("year", i.getToDoYear());
        cv.put("month", i.getToDoMonth());
        cv.put("day", i.getToDoDay());
        cv.put("hour", i.getToDoHour());
        cv.put("minute", i.getToDoMinute());
        cv.put("important", i.getImportant());
        cv.put("finish",i.getFinish());
        cv.put("alarmOp", i.getToDoAlarmOP());
        cv.put("beforeTime", i.getBeforeTime());
        db.insert(TABLE_NAME,null,cv);
        db.close();
    }
    public void updateEntry(TodoItem i) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("content", i.getToDoContent());
        cv.put("year", i.getToDoYear());
        cv.put("month", i.getToDoMonth());
        cv.put("day", i.getToDoDay());
        cv.put("hour", i.getToDoHour());
        cv.put("minute", i.getToDoMinute());
        cv.put("important", i.getImportant());
        cv.put("finish",i.getFinish());
        cv.put("alarmOp", i.getToDoAlarmOP());
        cv.put("beforeTime", i.getBeforeTime());
        String whereClause = "content=?";
        String[] whereArgs={i.getToDoContent()};
        db.update(TABLE_NAME,cv,whereClause,whereArgs);
    }
    public void deleteEntry(String n) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "content=?";
        String[] whereArgs={n};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public TodoItem getEntry(String content) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish", "alarmOp", "beforeTime"},null,null,null,null,null);
        TodoItem temp;
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            String tempContent = cursor.getString(contentCol);
            if (TextUtils.equals(content,tempContent)) {
                int yearCol = cursor.getColumnIndex("year");
                int monthCol = cursor.getColumnIndex("month");
                int dayCol = cursor.getColumnIndex("day");
                int hourCol = cursor.getColumnIndex("hour");
                int minuteCol = cursor.getColumnIndex("minute");
                int importantCol = cursor.getColumnIndex("important");
                int finishCol = cursor.getColumnIndex("finish");
                int OpCol = cursor.getColumnIndex("alarmOp");
                int TimeCol = cursor.getColumnIndex("beforeTime");
                temp = new TodoItem(cursor.getString(contentCol),cursor.getInt(yearCol), cursor.getInt(monthCol),
                        cursor.getInt(dayCol),cursor.getInt(hourCol),cursor.getInt(minuteCol),cursor.getInt(importantCol),cursor.getInt(finishCol), cursor.getString(OpCol), cursor.getInt(TimeCol));
                return temp;
            }
        }
        return null;
    }

    public List<TodoItem> getAllItems() {
        List<TodoItem> bl = new ArrayList<TodoItem>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish", "alarmOp", "beforeTime"},null,null,null,null,null);
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            int yearCol = cursor.getColumnIndex("year");
            int monthCol = cursor.getColumnIndex("month");
            int dayCol = cursor.getColumnIndex("day");
            int hourCol = cursor.getColumnIndex("hour");
            int minuteCol = cursor.getColumnIndex("minute");
            int importantCol = cursor.getColumnIndex("important");
            int finishCol = cursor.getColumnIndex("finish");
            int OpCol = cursor.getColumnIndex("alarmOp");
            int TimeCol = cursor.getColumnIndex("beforeTime");
            TodoItem temp = new TodoItem(cursor.getString(contentCol),cursor.getInt(yearCol), cursor.getInt(monthCol),
                    cursor.getInt(dayCol),cursor.getInt(hourCol),cursor.getInt(minuteCol),
                    cursor.getInt(importantCol),cursor.getInt(finishCol), cursor.getString(OpCol), cursor.getInt(TimeCol));
            bl.add(temp);
        }
        return bl;
    }

    public int searchID(String content) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"_id","content","year","month","day","hour","minute","important","finish", "alarmOp", "beforeTime"},null,null,null,null,null);
        TodoItem temp;
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            String tempContent = cursor.getString(contentCol);
            if (TextUtils.equals(content,tempContent)) {
                int _idc = cursor.getColumnIndex("_id");
                int _id = cursor.getInt(_idc);
                return _id;
            }
        }
        return -1;
    }

    public void deleteFinish() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"_id","content","year","month","day","hour","minute","important","finish", "alarmOp", "beforeTime"},null,null,null,null,null);
        while (cursor.moveToNext()) {
            int finishCol = cursor.getColumnIndex("finish");
            int finished = cursor.getInt(finishCol);
            int contentCol = cursor.getColumnIndex("content");
            String tempContent = cursor.getString(contentCol);
            if (finished == 1) {
                deleteEntry(tempContent);
            }
        }
    }


    public List<TodoItem> getImportantList() {
        List<TodoItem> bl = new ArrayList<TodoItem>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish", "alarmOp", "beforeTime"},null,null,null,null,null);
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            int yearCol = cursor.getColumnIndex("year");
            int monthCol = cursor.getColumnIndex("month");
            int dayCol = cursor.getColumnIndex("day");
            int hourCol = cursor.getColumnIndex("hour");
            int minuteCol = cursor.getColumnIndex("minute");
            int importantCol = cursor.getColumnIndex("important");
            int finishCol = cursor.getColumnIndex("finish");
            int OpCol = cursor.getColumnIndex("alarmOp");
            int TimeCol = cursor.getColumnIndex("beforeTime");
            if (cursor.getInt(finishCol) != 1 && cursor.getInt(importantCol) == 1) {
                TodoItem temp = new TodoItem(cursor.getString(contentCol),cursor.getInt(yearCol), cursor.getInt(monthCol),
                        cursor.getInt(dayCol),cursor.getInt(hourCol),cursor.getInt(minuteCol),
                        cursor.getInt(importantCol),cursor.getInt(finishCol), cursor.getString(OpCol), cursor.getInt(TimeCol));
                bl.add(temp);
            }
        }
        return bl;
    }

    public List<TodoItem> getNotImportantList() {
        List<TodoItem> bl = new ArrayList<TodoItem>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish" , "alarmOp", "beforeTime"},null,null,null,null,null);
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            int yearCol = cursor.getColumnIndex("year");
            int monthCol = cursor.getColumnIndex("month");
            int dayCol = cursor.getColumnIndex("day");
            int hourCol = cursor.getColumnIndex("hour");
            int minuteCol = cursor.getColumnIndex("minute");
            int importantCol = cursor.getColumnIndex("important");
            int finishCol = cursor.getColumnIndex("finish");
            int OpCol = cursor.getColumnIndex("alarmOp");
            int TimeCol = cursor.getColumnIndex("beforeTime");
            if (cursor.getInt(finishCol) != 1 && cursor.getInt(importantCol) == 0) {
                TodoItem temp = new TodoItem(cursor.getString(contentCol),cursor.getInt(yearCol), cursor.getInt(monthCol),
                        cursor.getInt(dayCol),cursor.getInt(hourCol),cursor.getInt(minuteCol),
                        cursor.getInt(importantCol),cursor.getInt(finishCol), cursor.getString(OpCol), cursor.getInt(TimeCol));
                bl.add(temp);
            }
        }
        return bl;
    }

    public List<TodoItem> getFinishList() {
        List<TodoItem> bl = new ArrayList<TodoItem>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish" , "alarmOp", "beforeTime"},null,null,null,null,null);
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            int yearCol = cursor.getColumnIndex("year");
            int monthCol = cursor.getColumnIndex("month");
            int dayCol = cursor.getColumnIndex("day");
            int hourCol = cursor.getColumnIndex("hour");
            int minuteCol = cursor.getColumnIndex("minute");
            int importantCol = cursor.getColumnIndex("important");
            int finishCol = cursor.getColumnIndex("finish");
            int OpCol = cursor.getColumnIndex("alarmOp");
            int TimeCol = cursor.getColumnIndex("beforeTime");
            if (cursor.getInt(finishCol) == 1) {
                TodoItem temp = new TodoItem(cursor.getString(contentCol),cursor.getInt(yearCol), cursor.getInt(monthCol),
                        cursor.getInt(dayCol),cursor.getInt(hourCol),cursor.getInt(minuteCol),
                        cursor.getInt(importantCol),cursor.getInt(finishCol), cursor.getString(OpCol), cursor.getInt(TimeCol));
                bl.add(temp);
            }
        }
        return bl;
    }
}