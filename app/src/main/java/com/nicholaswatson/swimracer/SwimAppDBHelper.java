package com.nicholaswatson.swimracer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DellUser on 4/9/2016.
 */

public class SwimAppDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "SwimAppDBHelper";
    public static final String DATABASE_NAME = "SwimAppDBHelper.db";
    private static final int DATABASE_VERSION = 1;
    public static final String SWIMTIME_TABLE_NAME = "swimTime";
    public static final String SWIMTIME_COLUMN_ID = "_id";
    public static final String SWIMTIME_COLUMN_STROKE = "stroke";
    public static final String SWIMTIME_COLUMN_TIME = "time";
    public static final String SWIMTIME_COLUMN_YARDS = "yards";

    public SwimAppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);



    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SWIMTIME_TABLE_NAME + "(" +
                        SWIMTIME_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        SWIMTIME_COLUMN_STROKE + " TEXT, " +
                        SWIMTIME_COLUMN_TIME + " TEXT, " +
                        SWIMTIME_COLUMN_YARDS + " INTEGER)"
        );
    }

    public boolean newSwimTime(String stroke, String time, int yards) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SWIMTIME_COLUMN_STROKE, stroke);
        contentValues.put(SWIMTIME_COLUMN_TIME, time);
        contentValues.put(SWIMTIME_COLUMN_YARDS, yards);
        db.insert(SWIMTIME_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean changeSwimTime(Integer id, String stroke, String time, int yards) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SWIMTIME_COLUMN_STROKE, stroke);
        contentValues.put(SWIMTIME_COLUMN_TIME, time);
        contentValues.put(SWIMTIME_COLUMN_YARDS, yards);
        db.update(SWIMTIME_TABLE_NAME, contentValues, SWIMTIME_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }
    public Cursor getSwimTime(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + SWIMTIME_TABLE_NAME + " WHERE " +
                SWIMTIME_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
        return res;
    }
    public Cursor getAllSwimTimes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + SWIMTIME_TABLE_NAME, null );
        return res;
    }
    public Integer deleteSwimTime(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SWIMTIME_TABLE_NAME,
                SWIMTIME_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //If dataHelper is greater than database, call onUpgrade
        db.execSQL("DROP TABLE IF EXISTS " + SWIMTIME_TABLE_NAME);
        onCreate(db);
    }


}
