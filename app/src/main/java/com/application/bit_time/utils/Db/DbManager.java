package com.application.bit_time.utils.Db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private SQLiteDatabase db;

    public static class DbHelper extends SQLiteOpenHelper
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
                DbContract.Userdata.COLUMN_NAME_PASSWORD + " text," +
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

    public void insertActivityRecord(ActivityItem activity)
    {
        TaskItem[] tasks = activity.getSubtasks();

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
                + activity.getName()+ "'," + totalTime + "," +tasksStr +");";


        Log.i("insert act str",insertQuery);
        db.execSQL(insertQuery);
    }

    public void insertTaskRecord(TaskItem task)
    {
        String insertQuery = "insert into "+ DbContract.Tasks.TABLE_NAME
                +" ("+ DbContract.Tasks.COLUMN_NAME_TASK_NAME+","
                + DbContract.Tasks.COLUMN_NAME_TASK_DURATION+") values("
                + "'"+task.getName()+"','"+task.getDuration()+"');";

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


    public ActivityItem searchActivityItem(ActivityInfo activityInfo)
    {
        String searchQuery =
                        "select * from "+DbContract.Activities.TABLE_NAME
                        + " where " + DbContract.Activities._ID + "="+ activityInfo.getIdInt();

        Log.i("searchqueryz",searchQuery);
        Cursor c = db.rawQuery(searchQuery,null);
        Log.i("searchqueryz",Integer.toString(c.getCount()));
        List<TaskItem> taskItemList= this.retrieveSubtasks(c);

        TaskItem[] taskItemArr = new TaskItem[taskItemList.size()];

        for(int i =0; i< taskItemList.size();i++)
        {
                taskItemArr[i] = new TaskItem(taskItemList.get(i));
        }

        ActivityItem item = new ActivityItem(activityInfo,taskItemArr);

        return item;
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

    public void modifyActivity(ActivityInfo info,int[] subtasksId) {

        String query =
                "UPDATE "+ DbContract.Activities.TABLE_NAME
                + " SET "
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME + "='"+ info.getName() + "',"
                + DbContract.Activities.COLUMN_NAME_TASK1 + "=" + Integer.toString(subtasksId[0]) +","
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + "=" + Integer.toString(info.getTimeInt()) +","
                + DbContract.Activities.COLUMN_NAME_TASK2 + "=" + Integer.toString(subtasksId[1]) +","
                + DbContract.Activities.COLUMN_NAME_TASK3 + "=" + Integer.toString(subtasksId[2]) +","
                + DbContract.Activities.COLUMN_NAME_TASK4 + "=" + Integer.toString(subtasksId[3]) +","
                + DbContract.Activities.COLUMN_NAME_TASK5 + "=" + Integer.toString(subtasksId[4])
                + " WHERE "+ DbContract.Activities._ID + "=" +Integer.toString(info.getIdInt());

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

        Log.i("SQLMOD",updateQuery);
        db.execSQL(updateQuery);

        String scanQuery =
                "select * from "+DbContract.Activities.TABLE_NAME
                + " where ";

        for(int i =1 ;i<=DbContract.Activities.DIM_MAX;i++)
        {
            String condition = DbContract.Activities.TABLE_NAME +".task"+Integer.toString(i) + "="+ modifiedItem.getIdStr();
            //Log.i("condition",condition);
            if(i<DbContract.Activities.DIM_MAX)
            {
                condition=condition.concat(" OR ");
            }
            scanQuery = scanQuery.concat(condition);
        }

        Log.i("scan query",scanQuery);

        Cursor scanCursor = db.rawQuery(scanQuery,null);
        scanCursor.moveToFirst();

        do {
            int currActivity = scanCursor.getInt(0);
            int newDuration = 0;
            for (int i = 0; i < DbContract.Activities.DIM_MAX; i++) {
                int currentElem = scanCursor.getInt(3 + i);
                Log.i("scanCursor currElem", Integer.toString(currentElem));
                if (currentElem > 0) {
                    TaskItem currTask = this.searchTask(currentElem);
                    newDuration += currTask.getDurationInt();
                }
            }
            Log.i("new duration", Integer.toString(newDuration));

            updateQuery = "update " + DbContract.Activities.TABLE_NAME + " set "
                    + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + "=" + Integer.toString(newDuration)
                    + " where " + DbContract.Activities._ID + "=" + Integer.toString(currActivity);
            ;

            Log.i("updateQueryAct", updateQuery);

            db.execSQL(updateQuery);
        }while(scanCursor.moveToNext());
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


    public boolean isUserRegistered() {
        // Check if there's at least one user in the database
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.Userdata.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        cursor.close();
        return userExists;
    }
    public Cursor searchUser(String email) {
        String searchQuery = "select * from "+DbContract.Userdata.TABLE_NAME+ " where "+
                DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        return db.rawQuery(searchQuery,null);
    }
    public void registerUser(String email,String password) {
        String insertQuery = "insert into "+ DbContract.Userdata.TABLE_NAME
                +" ("+ DbContract.Userdata.COLUMN_NAME_EMAIL+","
                + DbContract.Userdata.COLUMN_NAME_PASSWORD+") values("
                + "'"+email+"','"+password+"');";

        db.execSQL(insertQuery);
    }
    public boolean checkUser(Cursor cursor ,String password) {
        cursor.moveToFirst();
        String dbPassword = cursor.getString(3);
        if(dbPassword.equals(password))
            return true;
        else
            return false;
    }
    public String getUserEmail() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.Userdata.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        if(userExists)
            return cursor.getString(2);
        else
            return null;
    }
    public String getUserPin() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.Userdata.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        if(userExists){
            String pin = cursor.getString(4);
            //if the pin is saved return it, if it is null return string 0000
            if(pin != null)
                return pin;
            else
                return "0000";}
        else //return string 0000 if no user is registered
            return "0000";
    }
    public String getUsername() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.Userdata.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        if(userExists)
            return cursor.getString(1);
        else
            return null;
    }
    public void updateEmail(String email,String newEmail) {
        String updateQuery =
                "update "+ DbContract.Userdata.TABLE_NAME +
                        " set "
                        + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + newEmail + "'"
                        + " where " + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        Log.i("SQLMOD",updateQuery.toString());
        db.execSQL(updateQuery);
    }
    public void updateUsername(String email,String username) {
        String updateQuery =
                "update "+ DbContract.Userdata.TABLE_NAME +
                        " set "
                        + DbContract.Userdata.COLUMN_NAME_USERNAME + "='" + username + "'"
                        + " where " + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        Log.i("SQLMOD",updateQuery.toString());
        db.execSQL(updateQuery);
    }
    public void updatePin(String email,String pin) {
        String updateQuery =
                "update "+ DbContract.Userdata.TABLE_NAME +
                        " set "
                        + DbContract.Userdata.COLUMN_NAME_PIN + "='" + pin + "'"
                        + " where " + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        Log.i("SQLMOD",updateQuery.toString());
        db.execSQL(updateQuery);
    }
    public void updatePassword(String email,String password) {
        String updateQuery =
                "update "+ DbContract.Userdata.TABLE_NAME +
                        " set "
                        + DbContract.Userdata.COLUMN_NAME_PASSWORD + "='" + password + "'"
                        + " where " + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        Log.i("SQLMOD",updateQuery.toString());
        db.execSQL(updateQuery);
    }

    public void deleteUser(String email) {
        String deleteQuery =
                "delete from "+ DbContract.Userdata.TABLE_NAME + " where "
                        + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        //Log.i("DB delTask",deleteQuery.toString());
        db.execSQL(deleteQuery);
    }

    public void closeDb()
    {
        db.close();

    }

}
