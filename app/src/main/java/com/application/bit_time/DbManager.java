package com.application.bit_time;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private SQLiteDatabase db;




    public class DbHelper extends SQLiteOpenHelper
    {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Activities.db";

        private static final String SQL_CREATE_ACTIVITIES_TABLE = "create table " + DbContract.Activities.TABLE_NAME  + " (" +
                DbContract.Activities._ID + " integer primary key autoincrement,"  +
                DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME     + " text," +
                DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + " integer,"
                + DbContract.Activities.COLUMN_NAME_TASK1 + " integer,"
                + DbContract.Activities.COLUMN_NAME_TASK2 + " integer,"
                + DbContract.Activities.COLUMN_NAME_TASK3 + " integer,"
                + DbContract.Activities.COLUMN_NAME_TASK4 + " integer,"
                + DbContract.Activities.COLUMN_NAME_TASK5 + " integer);";

        private static final String SQL_CREATE_TASKS_TABLE = "create table " + DbContract.Tasks.TABLE_NAME  + " (" +
                DbContract.Tasks._ID + " integer primary key autoincrement,"  +
                DbContract.Tasks.COLUMN_NAME_TASK_NAME + " text," +
                DbContract.Tasks.COLUMN_NAME_TASK_DURATION  + " text);";

        private static final String SQL_DELETE_ENTRIES =   "DROP TABLE IF EXISTS " + DbContract.Activities.TABLE_NAME;

        private static final String SQL_CREATE_USERDATA_TABLE = "create table " + DbContract.Userdata.TABLE_NAME +" (" +
                DbContract.Userdata._ID + " integer primary key autoincrement," +
                DbContract.Userdata.COLUMN_NAME_USERNAME + " text," +
                DbContract.Userdata.COLUMN_NAME_EMAIL +" text," +
                DbContract.Userdata.COLUMN_NAME_PIN + " integer)";

        public DbHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TASKS_TABLE);
            db.execSQL(SQL_CREATE_ACTIVITIES_TABLE);
            db.execSQL(SQL_CREATE_USERDATA_TABLE);

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


    public Cursor searchActivityById(int idInt) {

        String query = "SELECT * FROM "+ DbContract.Activities.TABLE_NAME + " WHERE " +DbContract.Activities._ID + "=" + Integer.toString(idInt);

        //Log.i("searchQuery",query);
        return db.rawQuery(query,null);

    }

    public void insertActivityRecord(String name,TaskItem[] tasks)
    {

        int totalTime = 0;

        for(TaskItem ti : tasks)
        {
            totalTime = totalTime + ti.getDurationInt();
        }

        Log.i("totalTime",Integer.toString(totalTime));


        String tasksStr = "";


        for(int i = 0; i < DbContract.Activities.DIM_MAX ; i++)
        {
            Log.i("querystr",tasks[i].getIdStr()+",");
            if(i<DbContract.Activities.DIM_MAX-1)
                tasksStr = tasksStr.concat(tasks[i].getIdStr()+",");
            else
                tasksStr = tasksStr.concat(tasks[i].getIdStr());

        }

        String insertQuery = "insert into "+ DbContract.Activities.TABLE_NAME
                +" ("+ DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME+","
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + ","
                + DbContract.Activities.COLUMN_NAME_TASK1 + ","
                + DbContract.Activities.COLUMN_NAME_TASK2 + ","
                + DbContract.Activities.COLUMN_NAME_TASK3 + ","
                + DbContract.Activities.COLUMN_NAME_TASK4 + ","
                + DbContract.Activities.COLUMN_NAME_TASK5 + ") values ('"
                + name + "'," + totalTime + "," +tasksStr +");";


        Log.i("insert act str",insertQuery);
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

    public TaskItem searchTask(int id)
    {
        String searchQuery = "select * from "+DbContract.Tasks.TABLE_NAME+ " where "+
                DbContract.Tasks._ID + "=" + id;

        Cursor c = db.rawQuery(searchQuery,null);
        c.moveToFirst();
        return new TaskItem(c.getInt(0),c.getString(1),c.getInt(2));

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

    public void modifyActivity(int Id,String Name,int duration,int[] subtasksId) {

        String query =
                "UPDATE "+ DbContract.Activities.TABLE_NAME
                + " SET "
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME + "='"+ Name + "',"
                + DbContract.Activities.COLUMN_NAME_TASK1 + "=" + Integer.toString(subtasksId[0]) +","
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + "=" + Integer.toString(duration) +","
                + DbContract.Activities.COLUMN_NAME_TASK2 + "=" + Integer.toString(subtasksId[1]) +","
                + DbContract.Activities.COLUMN_NAME_TASK3 + "=" + Integer.toString(subtasksId[2]) +","
                + DbContract.Activities.COLUMN_NAME_TASK4 + "=" + Integer.toString(subtasksId[3]) +","
                + DbContract.Activities.COLUMN_NAME_TASK5 + "=" + Integer.toString(subtasksId[4])
                + " WHERE "+ DbContract.Activities._ID + "=" +Integer.toString(Id);

        Log.i("updatequery",query);


        db.execSQL(query);

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
        db.execSQL(updateQuery);

    }

    public void deleteTask(TaskItem task)
    {
        String deleteQuery =
                "delete from "+ DbContract.Tasks.TABLE_NAME + " where "
                + DbContract.Tasks._ID + "=" + task.getIdStr();

        //Log.i("DB delTask",deleteQuery.toString());
        db.execSQL(deleteQuery);

    }

    public List<TaskItem> retrieveSubtasks(Cursor activityCursor)
    {
        activityCursor.moveToFirst();
        List<TaskItem> subtasks = new ArrayList<>();

        for(int i =0 ; i< DbContract.Activities.DIM_MAX; i++)
        {
            int subtaskId = activityCursor.getInt(3+i);
            Log.i("testdbm",Integer.toString(subtaskId));
            if(subtaskId != -1) {
                TaskItem subtask = searchTask(subtaskId);
                Log.i("retrieved subtask", subtask.getName());
                subtasks.add(subtask);
            }
        }

        return subtasks;


    }

    public void closeDb()
    {
        db.close();

    }

}
