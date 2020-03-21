package com.example.yourmission;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_ROWID   = "_id";
    static final String KEY_NAME    = "name";
    static final String KEY_DESC    = "description";
    static final String KEY_DATE    = "deadline";
    static final String TAG         = "DB Adapter";
    static final String DB_NAME     = "MyDB";
    static final String DB_TABLE    = "task";
    static final int    DB_VERSION  = 1;
    static final String DB_CREATE   = "create table "
            + DB_TABLE + "("
            + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null, "
            + KEY_DESC + " text not null, "
            + KEY_DATE + " text not null);";
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DB_CREATE);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to"
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS task");
            onCreate(db);
        }
    }

    // Open the database
    public DBAdapter open(){
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // Close the database
    public void close(){
        DBHelper.close();
    }

    // Insert a task
    public long insertTask(String nameTask, String descTask, String deadlineTask){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, nameTask);
        initialValues.put(KEY_DESC, descTask);
        initialValues.put(KEY_DATE, deadlineTask);
        return db.insert(DB_TABLE, null, initialValues);
    }

}