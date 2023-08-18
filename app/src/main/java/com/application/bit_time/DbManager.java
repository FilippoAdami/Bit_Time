package com.application.bit_time;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbManager {

    private SQLiteDatabase db;

    public class DbHelper extends SQLiteOpenHelper
    {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Activities.db";

        private static final String SQL_CREATE_ACTIVITIES_TABLE = "create table " + DbContract.Activities.TABLE_NAME  + " (" +
                DbContract.Activities._ID + " integer primary key autoincrement,"  +
                DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME     + " text," +
                DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + " integer);";
                /*+ DbContract.Activities.COLUMN_NAME_TASK1 + "integer,"
                + DbContract.Activities.COLUMN_NAME_TASK2 + "integer,"
                + DbContract.Activities.COLUMN_NAME_TASK3 + "integer,"
                + DbContract.Activities.COLUMN_NAME_TASK4 + "integer,"
                + DbContract.Activities.COLUMN_NAME_TASK5 + "integer);";*/

        private static final String SQL_CREATE_TASKS_TABLE = "create table " + DbContract.Tasks.TABLE_NAME  + " (" +
                DbContract.Tasks._ID + " integer primary key autoincrement,"  +
                DbContract.Tasks.COLUMN_NAME_TASK_NAME + " text," +
                DbContract.Tasks.COLUMN_NAME_TASK_DURATION  + " text);";

        private static final String SQL_DELETE_ENTRIES =   "DROP TABLE IF EXISTS " + DbContract.Activities.TABLE_NAME;



        public DbHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TASKS_TABLE);
            db.execSQL(SQL_CREATE_ACTIVITIES_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }




    }


    public String getDbName()
    {
        return db.getPath();
    }


    public DbManager(Context context)
    {
        DbHelper dbHelper = new DbHelper(context);

        this.db = dbHelper.getWritableDatabase();
    }


    public void insertActivityRecord(String name, String duration)
    {
        String insertQuery = "insert into "+ DbContract.Activities.TABLE_NAME
                +" ("+ DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME+","
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION +") values("
                + "'"+name+"','"+duration+"');";

        db.execSQL(insertQuery);
    }

    public void insertTaskRecord(String name,String duration)
    {
        String insertQuery = "insert into "+ DbContract.Tasks.TABLE_NAME
                +" ("+ DbContract.Tasks.COLUMN_NAME_TASK_NAME+","
                + DbContract.Tasks.COLUMN_NAME_TASK_DURATION+") values("
                + "'"+name+"','"+duration+"');";

        db.execSQL(insertQuery);
    }

    public Cursor searchRecord(String name)
    {
        String searchQuery = "select "+ name +" from " + DbContract.Activities.TABLE_NAME;

        return db.rawQuery(searchQuery,null);
    }

    public Cursor selectAllActivities()
    {
        String searchQuery= "select * from "+ DbContract.Activities.TABLE_NAME;

        return db.rawQuery(searchQuery,null);
    }

    public Cursor selectAllTasks()
    {
        String searchQuery= "select * from "+ DbContract.Tasks.TABLE_NAME;

        return db.rawQuery(searchQuery,null);
    }


    public void deleteActivity(ActivityInfo item)
    {
        String deleteQuery = "delete from "+ DbContract.Activities.TABLE_NAME
                             + " where " + DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME + "='" +item.getName() +
                "' and "+ DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + "='"+ item.getTime()+"';";

        Log.i("QUERY",deleteQuery);
        db.execSQL(deleteQuery);
    }

    public void modifyTask(TaskItem modifiedItem)
    {
        String updateQuery =
                "update "+ DbContract.Tasks.TABLE_NAME +
                        " set "
                        + DbContract.Tasks.COLUMN_NAME_TASK_NAME + "='" + modifiedItem.getName() + "',"
                        + DbContract.Tasks.COLUMN_NAME_TASK_DURATION + "=" + modifiedItem.getDuration()
                        + " where " + DbContract.Tasks._ID + "=" + modifiedItem.getIdStr();

        Log.i("SQLMOD",updateQuery.toString());
        //db.execSQL(updateQuery);

    }


    public void closeDb()
    {
        db.close();

    }

}
