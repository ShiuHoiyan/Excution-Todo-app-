package com.example.diong.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Created by Administrator on 2016/12/23 0023.
 */
public class bzDB extends SQLiteOpenHelper{
    private static final String DB_NAME = "BZ_Storage";
    private static final String TABLE_NAME = "Beizhu";

    public bzDB(Context context) {
        super(context,DB_NAME, null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE if not exists "
                + TABLE_NAME
                +" (_id INTEGER PRIMARY KEY, content TEXT, infor TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertBZ(String content, String what) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        cv.put("infor", what);
        db.insert(TABLE_NAME,null,cv);
        db.close();
    }

    public void updateBZ(String content, String what) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        cv.put("infor", what);
        String whereClause = "content=?";
        String[] whereArgs={content};
        db.update(TABLE_NAME,cv,whereClause,whereArgs);
    }

    public void deleteBZ(String content) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "content=?";
        String[] whereArgs={content};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public String getBZ(String content) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"content","infor"},null,null,null,null,null);
        TodoItem temp;
        while (cursor.moveToNext()) {
            int contentCol = cursor.getColumnIndex("content");
            String tempContent = cursor.getString(contentCol);
            if (TextUtils.equals(content,tempContent)) {
                int ifCol = cursor.getColumnIndex("infor");
                return cursor.getString(ifCol);
            }
        }
        return "";
    }
}
