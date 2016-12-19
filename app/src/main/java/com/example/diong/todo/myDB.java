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
                +" (_id INTEGER PRIMARY KEY, content TEXT, year INTEGER,month INTEGER,day INTEGER,hour INTEGER, minute INTEGER,important INTEGER,finish INTEGER)";
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
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish"},null,null,null,null,null);
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
                temp = new TodoItem(cursor.getString(contentCol),cursor.getInt(yearCol), cursor.getInt(monthCol),
                        cursor.getInt(dayCol),cursor.getInt(hourCol),cursor.getInt(minuteCol),cursor.getInt(importantCol),cursor.getInt(finishCol));
                return temp;
            }
        }
        return null;
    }

    public List<TodoItem> getAllItems() {
        List<TodoItem> bl = new ArrayList<TodoItem>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish"},null,null,null,null,null);
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            int yearCol = cursor.getColumnIndex("year");
            int monthCol = cursor.getColumnIndex("month");
            int dayCol = cursor.getColumnIndex("day");
            int hourCol = cursor.getColumnIndex("hour");
            int minuteCol = cursor.getColumnIndex("minute");
            int importantCol = cursor.getColumnIndex("important");
            int finishCol = cursor.getColumnIndex("finish");
            TodoItem temp = new TodoItem(cursor.getString(contentCol),cursor.getInt(yearCol), cursor.getInt(monthCol),
                    cursor.getInt(dayCol),cursor.getInt(hourCol),cursor.getInt(minuteCol),cursor.getInt(importantCol),cursor.getInt(finishCol));
            bl.add(temp);
        }
        return bl;
    }

    public int searchID(String content) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"_id","content","year","month","day","hour","minute","important","finish"},null,null,null,null,null);
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

  /*  public TodoItem getTodoItemByID(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"_id", "content","year","month","day","hour","minute","important"},null,null,null,null,null);
        TodoItem temp;
        while (cursor.moveToNext()) {
            int idCol = cursor.getColumnIndex("_id");
            int tempContent = cursor.getInt(idCol);
            if (tempContent == id) {
                int contentCol = cursor.getColumnIndex("content");
                int yearCol = cursor.getColumnIndex("year");
                int monthCol = cursor.getColumnIndex("month");
                int dayCol = cursor.getColumnIndex("day");
                int hourCol = cursor.getColumnIndex("hour");
                int minuteCol = cursor.getColumnIndex("minute");
                int importantCol = cursor.getColumnIndex("important");
                temp = new TodoItem(cursor.getString(contentCol),cursor.getInt(yearCol), cursor.getInt(monthCol),
                        cursor.getInt(dayCol),cursor.getInt(hourCol),cursor.getInt(minuteCol),cursor.getInt(importantCol));
                return temp;
            }
        }
        return null;
    }*/


    public List<TodoItem> getImportantList() {
        List<TodoItem> bl = new ArrayList<TodoItem>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish"},null,null,null,null,null);
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            int yearCol = cursor.getColumnIndex("year");
            int monthCol = cursor.getColumnIndex("month");
            int dayCol = cursor.getColumnIndex("day");
            int hourCol = cursor.getColumnIndex("hour");
            int minuteCol = cursor.getColumnIndex("minute");
            int importantCol = cursor.getColumnIndex("important");
            int finishCol = cursor.getColumnIndex("finish");
            if (cursor.getInt(finishCol) != 1 && cursor.getInt(importantCol) == 1) {
                TodoItem temp = new TodoItem(cursor.getString(contentCol), cursor.getInt(yearCol), cursor.getInt(monthCol),
                        cursor.getInt(dayCol), cursor.getInt(hourCol), cursor.getInt(minuteCol), cursor.getInt(importantCol), cursor.getInt(finishCol));
                bl.add(temp);
            }
        }
        return bl;
    }

    public List<TodoItem> getNotImportantList() {
        List<TodoItem> bl = new ArrayList<TodoItem>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish"},null,null,null,null,null);
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            int yearCol = cursor.getColumnIndex("year");
            int monthCol = cursor.getColumnIndex("month");
            int dayCol = cursor.getColumnIndex("day");
            int hourCol = cursor.getColumnIndex("hour");
            int minuteCol = cursor.getColumnIndex("minute");
            int importantCol = cursor.getColumnIndex("important");
            int finishCol = cursor.getColumnIndex("finish");
            if (cursor.getInt(finishCol) != 1 && cursor.getInt(importantCol) == 0) {
                TodoItem temp = new TodoItem(cursor.getString(contentCol), cursor.getInt(yearCol), cursor.getInt(monthCol),
                        cursor.getInt(dayCol), cursor.getInt(hourCol), cursor.getInt(minuteCol), cursor.getInt(importantCol), cursor.getInt(finishCol));
                bl.add(temp);
            }
        }
        return bl;
    }

    public List<TodoItem> getFinishList() {
        List<TodoItem> bl = new ArrayList<TodoItem>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","year","month","day","hour","minute","important","finish"},null,null,null,null,null);
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            int yearCol = cursor.getColumnIndex("year");
            int monthCol = cursor.getColumnIndex("month");
            int dayCol = cursor.getColumnIndex("day");
            int hourCol = cursor.getColumnIndex("hour");
            int minuteCol = cursor.getColumnIndex("minute");
            int importantCol = cursor.getColumnIndex("important");
            int finishCol = cursor.getColumnIndex("finish");
            if (cursor.getInt(finishCol) == 1) {
                TodoItem temp = new TodoItem(cursor.getString(contentCol), cursor.getInt(yearCol), cursor.getInt(monthCol),
                        cursor.getInt(dayCol), cursor.getInt(hourCol), cursor.getInt(minuteCol), cursor.getInt(importantCol), cursor.getInt(finishCol));
                bl.add(temp);
            }
        }
        return bl;
    }
}