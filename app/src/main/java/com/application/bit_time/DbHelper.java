package com.application.bit_time;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper
{


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Activities.db";

    private static final String SQL_CREATE_ENTRIES = "create table " + DbContract.Activities.TABLE_NAME  + " (" +
                    DbContract.Activities._ID + " integer primary key,"  +
                    DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME     + " text," +
                    DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + " text);";

    private static final String SQL_DELETE_ENTRIES =   "DROP TABLE IF EXISTS " + DbContract.Activities.TABLE_NAME;







    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("DB_INFO","DbHelper created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
        Log.i("DB_INFO","Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

        Log.i("DB_INFO","Database discarted then recreated");


    }



    public void insertRecord(String name, String duration)
    {
        String insertQuery = "insert into "+ DbContract.Activities.TABLE_NAME
                +"("+ DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME+","
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION +") values"
                + "'"+name+"','"+duration+"');";



    }

}

