package com.application.bit_time.utils.Db;

import org.mindrot.jbcrypt.BCrypt;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.application.bit_time.R;
import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.AlarmUtils.AlarmInfo;
import com.application.bit_time.utils.PlanningInfo;
import com.application.bit_time.utils.ReportData;
import com.application.bit_time.utils.TaskItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DbManager {

    private static SQLiteDatabase db = null;
    private Resources res;

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
                + DbContract.Activities.COLUMN_NAME_TASK5 + " integer,"
                + DbContract.Activities.COLUMN_NAME_IS_PLANNED + " integer," // 0 will be false and 1 true
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_IMG + " text)";

        private static final String SQL_CREATE_TASKS_TABLE = "create table " + DbContract.Tasks.TABLE_NAME  + " (" +
                DbContract.Tasks._ID + " integer primary key autoincrement,"  +
                DbContract.Tasks.COLUMN_NAME_TASK_NAME + " text," +
                DbContract.Tasks.COLUMN_NAME_TASK_DURATION  + " text," +
                DbContract.Tasks.COLUMN_NAME_IMG + " text)";

        private static final String SQL_DELETE_ENTRIES =   "DROP TABLE IF EXISTS " + DbContract.Activities.TABLE_NAME;

        private static final String SQL_CREATE_USERDATA_TABLE = "create table " + DbContract.Userdata.TABLE_NAME +" (" +
                DbContract.Userdata._ID + " integer primary key autoincrement," +
                DbContract.Userdata.COLUMN_NAME_USERNAME + " text," +
                DbContract.Userdata.COLUMN_NAME_EMAIL +" text," +
                DbContract.Userdata.COLUMN_NAME_PASSWORD + " text," +
                DbContract.Userdata.COLUMN_NAME_PIN + " integer," +
                DbContract.Userdata.COLUMN_NAME_SALT + " text)";

        public static final String SQL_CREATE_REPORT_DATA_TABLE = "create table "+ DbContract.reportData.TABLE_NAME + " (" +
                DbContract.reportData._ID + " integer primary key autoincrement," +
                DbContract.reportData.COLUMN_NAME_METADATA + " text,"+
                DbContract.reportData.COLUMN_NAME_ENDSTATUS + " text,"+
                DbContract.reportData.COLUMN_NAME_LASTED_TIME + " integer)";

        private static final String SQL_CREATE_GAMIFICATION_SETTINGS_TABLE = "create table " + DbContract.gamificationSettings.TABLE_NAME +" (" +
                DbContract.gamificationSettings._ID + " integer primary key autoincrement," +
                DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION + " integer," +
                DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TYPE + " integer," +
                DbContract.gamificationSettings.COLUMN_NAME_POSITIVE_ICON + " text," +
                DbContract.gamificationSettings.COLUMN_NAME_NEGATIVE_ICON + " text," +
                DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_1 + " integer," +
                DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_2 + " integer," +
                DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_3 + " integer," +
                DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_4 + " integer," +
                DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_5 + " integer," +
                DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_6 + " integer)";

        private static final String SQL_CREATE_APP_SETTINGS_TABLE = "create table " + DbContract.appSettings.TABLE_NAME +" (" +
                DbContract.appSettings._ID + " integer primary key autoincrement," +
                DbContract.appSettings.COLUMN_NAME_THEME + " text," +
                DbContract.appSettings.COLUMN_NAME_BACKGROUND + " text," +
                DbContract.appSettings.COLUMN_NAME_VOLUME + " integer," +
                DbContract.appSettings.COLUMN_NAME_RINGTONE + " text," +
                DbContract.appSettings.COLUMN_NAME_NOTIFICATION_SOUNDS + " integer," +
                DbContract.appSettings.COLUMN_NAME_NOTIFICATIONS + " integer," +
                DbContract.appSettings.COLUMN_NAME_SOUNDS + " integer," +
                DbContract.appSettings.COLUMN_NAME_FOCUS + " integer," +
                DbContract.appSettings.COLUMN_NAME_HOME_TYPE + " integer," +
                DbContract.appSettings.COLUMN_NAME_BACKGROUND_NAME + " text," +
                DbContract.appSettings.COLUMN_NAME_RINGTONE_NAME + " text," +
                DbContract.appSettings.COLUMN_NAME_NOTIFICATIONS_NAME + " text)";


        public static final String SQL_CREATE_ACTIVITY_SCHEDULE_TABLE =
                "create table "+DbContract.ActivitySchedule.TABLE_NAME +"(" +
                        DbContract.ActivitySchedule._ID +" integer primary key autoincrement," +
                        DbContract.ActivitySchedule.COLUMN_NAME_ACTIVITY_ID + " integer," +
                        DbContract.ActivitySchedule.COLUMN_NAME_YEAR + " integer," +
                        DbContract.ActivitySchedule.COLUMN_NAME_MONTH + " integer," +
                        DbContract.ActivitySchedule.COLUMN_NAME_DAY + " integer," +
                        DbContract.ActivitySchedule.COLUMN_NAME_HOUR +" integer," +
                        DbContract.ActivitySchedule.COLUMN_NAME_MINUTES + " integer," +
                        DbContract.ActivitySchedule.COLUMN_NAME_FREQUENCY + " text);";


        public DbHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }



        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TASKS_TABLE);
            db.execSQL(SQL_CREATE_ACTIVITIES_TABLE);
            db.execSQL(SQL_CREATE_USERDATA_TABLE);
            db.execSQL(SQL_CREATE_APP_SETTINGS_TABLE);
            db.execSQL(SQL_CREATE_GAMIFICATION_SETTINGS_TABLE);
            db.execSQL(SQL_CREATE_ACTIVITY_SCHEDULE_TABLE);
            db.execSQL(SQL_CREATE_REPORT_DATA_TABLE);
        }

        /*public String getDbName()
        {
            return db.getPath();
        }*/

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }


    public DbManager(Context context)
    {
        DbHelper dbHelper = new DbHelper(context);
        res = context.getResources();
        db = dbHelper.getWritableDatabase();
    }


    public Cursor searchActivityById(int idInt) {

        String query = "SELECT * FROM "+ DbContract.Activities.TABLE_NAME + " WHERE " +DbContract.Activities._ID + "=" + idInt;

        //Log.i("searchQuery",query);
        return db.rawQuery(query,null);

    }

    public int insertActivityRecord(ActivityItem activity)
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
        String imagePath = activity.getInfo().getImage();
        String insertQuery = "insert into "+ DbContract.Activities.TABLE_NAME
                +" ("+ DbContract.Activities.COLUMN_NAME_ACTIVITY_NAME+","
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + ","
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_IMG + ","
                + DbContract.Activities.COLUMN_NAME_TASK1 + ","
                + DbContract.Activities.COLUMN_NAME_TASK2 + ","
                + DbContract.Activities.COLUMN_NAME_TASK3 + ","
                + DbContract.Activities.COLUMN_NAME_TASK4 + ","
                + DbContract.Activities.COLUMN_NAME_TASK5 + ") values ('"
                + activity.getName()+ "'," + totalTime + ",'" + imagePath + "'," +tasksStr +");";


        Log.i("insert act str",insertQuery);
        db.execSQL(insertQuery);


        Cursor c = selectLatestActivity();
        int latestActId = -1;

        if (c.getCount() > 0) {
            c.moveToFirst();
            latestActId = c.getInt(0);
        }
        //return latestActId;
        return latestActId;
    }


    public List<Integer> insertMultipleActivitySchedule(ActivityItem activity)
    {
        List<Integer> ids = new ArrayList<>();
        int latestActId = activity.getInfo().getIdInt();
        if(activity.isPlanned()) {



            for(PlanningInfo pi : activity.getPlans())
            {
                Log.i("pi to be added",pi.toString());
                ids.add(insertActivitySchedule(latestActId,pi.getInfo().getInfoGC(),pi.getInfo().getFreq()));

            }



        }

        return ids;
    }

    public Cursor selectLatestActivity()
    {


        String queryMaxStr = "select max(" +DbContract.Activities._ID +") from "+ DbContract.Activities.TABLE_NAME;

        Cursor  c = db.rawQuery(queryMaxStr,null);


        int latestActivityId=-1;

        if(c.getCount()==1)
        {
            c.moveToFirst();
            latestActivityId = c.getInt(0);
            //Log.i("count","max actId is "+latestActivityId);
            String queryStr = "select * from "+ DbContract.Activities.TABLE_NAME +
                    " where "+ DbContract.Activities._ID +"="+ latestActivityId;

            c= db.rawQuery(queryStr,null);

            if(c.getCount()>0)
            {
                c.moveToFirst();
                Log.i("latest act inserted has",c.getColumnCount() +" columns");
            }
        }
        return c;
    }

    public void insertTaskRecord(TaskItem task)
    {
        String insertQuery = "insert into "+ DbContract.Tasks.TABLE_NAME
                +" ("+ DbContract.Tasks.COLUMN_NAME_TASK_NAME+","
                + DbContract.Tasks.COLUMN_NAME_TASK_DURATION+","
                + DbContract.Tasks.COLUMN_NAME_IMG+") values("
                + "'"+task.getName()+"','"+task.getDuration()+"','"+task.getImg()+"');";

        db.execSQL(insertQuery);
    }

    public TaskItem searchTask(int id)
    {
        String searchQuery = "select * from "+DbContract.Tasks.TABLE_NAME+ " where "+
                DbContract.Tasks._ID + "=" + id;

        Cursor c = db.rawQuery(searchQuery,null);

        if(c.getCount()>0) {
            c.moveToFirst();
            TaskItem item = new TaskItem(c.getInt(0), c.getString(1), c.getInt(2), c.getString(3));
            c.close();
            return item;
        }

        c.close();
        return new TaskItem();


    }

    /*public Cursor searchRecord(String name)
    {
        String searchQuery = "select "+ name +" from " + DbContract.Activities.TABLE_NAME;

        return db.rawQuery(searchQuery,null);
    }*/

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

        //ActivityItem item = new ActivityItem(activityInfo,taskItemArr);

        //return item;
        //activityInfo.updateImgStr(c.getString(9));

        return new ActivityItem(activityInfo,taskItemArr);
    }
    public Cursor selectAllActivities()
    {
        String searchQuery= "select * from "+ DbContract.Activities.TABLE_NAME;
        Log.i("selectAllActivities query",searchQuery);
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
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + "=" + info.getTimeInt() +","
                + DbContract.Activities.COLUMN_NAME_ACTIVITY_IMG + "='" + info.getImage() + "',"
                + DbContract.Activities.COLUMN_NAME_TASK1 + "=" + subtasksId[0] +","
                + DbContract.Activities.COLUMN_NAME_TASK2 + "=" + subtasksId[1] +","
                + DbContract.Activities.COLUMN_NAME_TASK3 + "=" + subtasksId[2] +","
                + DbContract.Activities.COLUMN_NAME_TASK4 + "=" + subtasksId[3] +","
                + DbContract.Activities.COLUMN_NAME_TASK5 + "=" + subtasksId[4]
                + " WHERE "+ DbContract.Activities._ID + "=" + info.getIdInt();

        Log.i("updatequery",query);


        db.execSQL(query);

    }

    public List<Integer> modifyActivity(ActivityItem item,int[] subtasksId)
    {
        modifyActivity(item.getInfo(),subtasksId);
        List<Integer> ids = new ArrayList<>();
        ids.add(item.getInfo().getIdInt());
        ids.addAll(insertMultipleActivitySchedule(item));

        /*if(item.isPlanned())
        {
            for(PlanningInfo pi : item.getPlans())
            {
                Log.i("modActLog",pi.toString());
                insertActivitySchedule(item.getInfo().getIdInt(),pi.getInfo().getInfoGC(),pi.getInfo().getFreq());
            }
        }*/


        return ids;

    }

    public void deletePlanById(int planId)
    {
        String str = "delete from "+ DbContract.ActivitySchedule.TABLE_NAME +
                " where "+ DbContract.ActivitySchedule._ID + "="+ planId;

        Log.i("deletePlan str",str);

        db.execSQL(str);
    }

    public void deleteAllPlansByActivityId(int activityId)
    {


        String str= "delete from "+DbContract.ActivitySchedule.TABLE_NAME +
                " where " + DbContract.ActivitySchedule.COLUMN_NAME_ACTIVITY_ID + "=" + activityId;

        Log.i("deletePlans str",str);

        db.execSQL(str);

    }

    public void deleteActivity(ActivityInfo item)
    {
        deleteAllPlansByActivityId(item.getIdInt());

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
                        + DbContract.Tasks.COLUMN_NAME_TASK_DURATION + "='" + modifiedItem.getDuration() + "',"
                        + DbContract.Tasks.COLUMN_NAME_IMG + "='" + modifiedItem.getImg() + "'"
                        + " where " + DbContract.Tasks._ID + "=" + modifiedItem.getIdStr();

        Log.i("SQLMOD",updateQuery);
        db.execSQL(updateQuery);

        String scanQuery = scanQueryBuilder(modifiedItem);

        Log.i("scan query",scanQuery);

        Cursor scanCursor = db.rawQuery(scanQuery,null);
        scanCursor.moveToFirst();

        if(scanCursor.getCount()>0) {
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
                        + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + "=" + newDuration
                        + " where " + DbContract.Activities._ID + "=" + currActivity;


                Log.i("updateQueryAct", updateQuery);

                db.execSQL(updateQuery);
            } while (scanCursor.moveToNext());
        }

        scanCursor.close();
    }

    public void deleteTask(TaskItem task) {
        String deleteQuery =
                "DELETE FROM " + DbContract.Tasks.TABLE_NAME + " WHERE "
                        + DbContract.Tasks._ID + "=" + task.getIdStr();

        db.execSQL(deleteQuery);

        String scanQuery = scanQueryBuilder(task);

        Cursor scanCursor = db.rawQuery(scanQuery, null);

        Log.i("scanCursor info", "dim: " + scanCursor.getCount());

        if (scanCursor.getCount() > 0) {
            scanCursor.moveToFirst();
            do {
                int[] currSubtasks = new int[DbContract.Activities.DIM_MAX];
                int pos = 0;
                int newDuration = 0;

                for (int i = 0; i < DbContract.Activities.DIM_MAX; i++) {
                    int currElem = scanCursor.getInt(3 + i);
                    if (currElem != task.getID()) {
                        currSubtasks[pos] = currElem;
                        pos++;

                        if (currElem > 0) {
                            TaskItem currItem = searchTask(currElem);
                            newDuration += currItem.getDurationInt();
                        }
                    }
                }

                if (pos > 0) {
                    currSubtasks[DbContract.Activities.DIM_MAX - 1] = -1;

                    String updateQuery = "update " + DbContract.Activities.TABLE_NAME + " set "
                            + DbContract.Activities.COLUMN_NAME_ACTIVITY_DURATION + "=" + newDuration + ",";

                    for (int i = 1; i <= DbContract.Activities.DIM_MAX; i++) {
                        String partial = " task" + i + "=" + currSubtasks[i - 1];

                        if (i < DbContract.Activities.DIM_MAX) {
                            partial = partial.concat(",");
                        }
                        updateQuery = updateQuery.concat(partial);
                    }

                    updateQuery = updateQuery.concat(" WHERE " + DbContract.Activities._ID + "=" + scanCursor.getInt(0));

                    Log.i("updateQuery2", updateQuery);
                    db.execSQL(updateQuery);
                } else {
                    Log.i("dimtestlog", "left subtasks are zero");
                }
            } while (scanCursor.moveToNext());
        }

        scanCursor.close();
    }


    public List<TaskItem> retrieveSubtasks(Cursor activityCursor)
    {
        List<TaskItem> subtasks = new ArrayList<>();

        if(activityCursor.getCount()>0)
        {
            activityCursor.moveToFirst();

            for (int i = 0; i < DbContract.Activities.DIM_MAX; i++) {
                int subtaskId = activityCursor.getInt(3 + i);
                Log.i("testdbm", Integer.toString(subtaskId));
                if (subtaskId != -1) {
                    TaskItem subtask = searchTask(subtaskId);
                    Log.i("retrieved subtask img", subtask.getImg());
                    subtasks.add(subtask);
                }
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
    public void registerUser(String email, String password) {
        // Generate a random salt
        String salt = BCrypt.gensalt();

        // Hash the password with the salt
        String hashedPassword = BCrypt.hashpw(password, salt);
        Log.i("DB_INFO", "Hashed password: " + hashedPassword);

        String insertQuery = "INSERT INTO " + DbContract.Userdata.TABLE_NAME +
                " (" + DbContract.Userdata.COLUMN_NAME_EMAIL + ", " +
                DbContract.Userdata.COLUMN_NAME_PASSWORD + ", " +
                DbContract.Userdata.COLUMN_NAME_SALT + ") VALUES (?, ?, ?)";

        // Use parameterized query to avoid SQL injection
        db.execSQL(insertQuery, new String[]{email, hashedPassword, salt});
    }
    public boolean checkUser(Cursor cursor, String password) {
        int columnIndex = cursor.getColumnIndex(DbContract.Userdata.COLUMN_NAME_PASSWORD);
        int saltIndex = cursor.getColumnIndex(DbContract.Userdata.COLUMN_NAME_SALT);

        if (columnIndex == -1 || saltIndex == -1) {
            Log.e("DB_ERROR", "Column index is -1 for password or salt column");
            cursor.close();
            return false;
        }
        cursor.moveToFirst();
        String dbPasswordHash = cursor.getString(columnIndex);
        String salt = cursor.getString(saltIndex);

        // Hash the input password with the retrieved salt
        String hashedPassword = BCrypt.hashpw(password, salt);
        Log.i("DB_INFO", "Hashed password: " + hashedPassword);
        cursor.close();
        // Compare the hashed input password with the saved hash
        return hashedPassword.equals(dbPasswordHash);
    }

    public String getUserEmail() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.Userdata.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        if(userExists) {
            String email = cursor.getString(2);
            cursor.close();
            return email;
        } else {
            cursor.close();
            return null;
        }
    }
    public String getUserPin() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.Userdata.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        if(userExists){
            String pin = cursor.getString(4);
            cursor.close();
            //if the pin is saved return it, if it is null return string 0000
            if(pin != null)
                if(pin.equals("0"))
                    return "0000";
                else
                    return pin;
            else
                return "0000";}
        else //return string 0000 if no user is registered
        {
            cursor.close();
            return "0000";
        }
    }
    public String getUsername() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.Userdata.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        if(userExists){
            String username = cursor.getString(1);
            cursor.close();
            return username;
        }
        else {
            cursor.close();
            return null;
        }
    }
    public void updateEmail(String email,String newEmail) {
        String updateQuery =
                "update "+ DbContract.Userdata.TABLE_NAME +
                        " set "
                        + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + newEmail + "'"
                        + " where " + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        Log.i("SQLMOD", updateQuery);
        db.execSQL(updateQuery);
    }
    public void updateUsername(String email,String username) {
        String updateQuery =
                "update "+ DbContract.Userdata.TABLE_NAME +
                        " set "
                        + DbContract.Userdata.COLUMN_NAME_USERNAME + "='" + username + "'"
                        + " where " + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        Log.i("SQLMOD", updateQuery);
        db.execSQL(updateQuery);
    }
    public void updatePin(String pin) {
        String updateQuery =
                "update " + DbContract.Userdata.TABLE_NAME +
                        " set " + DbContract.Userdata.COLUMN_NAME_PIN + "='" + pin + "'";

        Log.i("SQLMOD", updateQuery);
        db.execSQL(updateQuery);
    }
    public void updatePassword(String email, String password) {
        // Generate a salt
        String salt = BCrypt.gensalt();

        // Hash the password with the generated salt
        String hashedPassword = BCrypt.hashpw(password, salt);

        String updateQuery =
                "UPDATE " + DbContract.Userdata.TABLE_NAME +
                        " SET "
                        + DbContract.Userdata.COLUMN_NAME_PASSWORD + "='" + hashedPassword + "', "
                        + DbContract.Userdata.COLUMN_NAME_SALT + "='" + salt + "'"
                        + " WHERE " + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        Log.i("SQLMOD", updateQuery);
        db.execSQL(updateQuery);
    }
    /*public void deleteUser(String email) {
        String deleteQuery =
                "delete from "+ DbContract.Userdata.TABLE_NAME + " where "
                        + DbContract.Userdata.COLUMN_NAME_EMAIL + "='" + email + "'";

        //Log.i("DB delTask",deleteQuery.toString());
        db.execSQL(deleteQuery);
    }*/

    public void changeTheme(String theme) {
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_THEME, theme);

        // Check if a row exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            // If at least one row exists, update the value
            int rowsAffected = db.update(
                    DbContract.appSettings.TABLE_NAME,
                    values,
                    null,
                    null
            );

            if (rowsAffected > 0) {
                Log.i("DB_UPDATE", "Theme updated successfully: " + theme);
            } else {
                Log.e("DB_ERROR", "Failed to update theme");
            }
        } else {
            // If no row exists, create a new row with default values
            long newRowId = db.insert(DbContract.appSettings.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Log.i("DB_INSERT", "New row inserted with theme: " + theme);
            } else {
                Log.e("DB_ERROR", "Failed to insert new row");
            }
        }
        cursor.close();
    }
    public void changeBackground(String background) {
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_BACKGROUND, background);

        // Check if a row exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            // If at least one row exists, update the value
            int rowsAffected = db.update(
                    DbContract.appSettings.TABLE_NAME,
                    values,
                    null,
                    null
            );

            if (rowsAffected > 0) {
                Log.i("DB_UPDATE", "Background updated successfully: " + background);
            } else {
                Log.e("DB_ERROR", "Failed to update background");
            }
        } else {
            // If no row exists, create a new row with default values
            long newRowId = db.insert(DbContract.appSettings.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Log.i("DB_INSERT", "New row inserted with background: " + background);
            } else {
                Log.e("DB_ERROR", "Failed to insert new row");
            }
        }

        cursor.close();  // Close the cursor to avoid potential memory leaks
    }
    public void changeBackgroundName(String name){
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_BACKGROUND_NAME, name);

        // Check if a row exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            // If at least one row exists, update the value
            int rowsAffected = db.update(
                    DbContract.appSettings.TABLE_NAME,
                    values,
                    null,
                    null
            );

            if (rowsAffected > 0) {
                Log.i("DB_UPDATE", "Background updated successfully: " + name);
            } else {
                Log.e("DB_ERROR", "Failed to update background");
            }
        } else {
            // If no row exists, create a new row with default values
            long newRowId = db.insert(DbContract.appSettings.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Log.i("DB_INSERT", "New row inserted with background: " + name);
            } else {
                Log.e("DB_ERROR", "Failed to insert new row");
            }
        }

        cursor.close();  // Close the cursor to avoid potential memory leaks
    }
    public void changeVolume(int volume) {
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_VOLUME, volume);

        int rowsAffected = db.update(DbContract.appSettings.TABLE_NAME, values, null, null);

        if (rowsAffected == 0) {
            // No row was updated, so insert a new row
            db.insert(DbContract.appSettings.TABLE_NAME, null, values);
        }

        Log.i("SQLMOD", "changeFocus: Rows affected - " + rowsAffected);
    }
    public void changeRingtone(String ringtone) {
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_RINGTONE, ringtone);

        // Check if a row exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            // If at least one row exists, update the value
            int rowsAffected = db.update(
                    DbContract.appSettings.TABLE_NAME,
                    values,
                    null,
                    null
            );

            if (rowsAffected > 0) {
                Log.i("DB_UPDATE", "Ringtone updated successfully: " + ringtone);
            } else {
                Log.e("DB_ERROR", "Failed to update ringtone");
            }
        } else {
            // If no row exists, create a new row with default values
            long newRowId = db.insert(DbContract.appSettings.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Log.i("DB_INSERT", "New row inserted with ringtone: " + ringtone);
            } else {
                Log.e("DB_ERROR", "Failed to insert new row");
            }
        }

        cursor.close();  // Close the cursor to avoid potential memory leaks
    }
    public void changeNotification(String notification) {
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_NOTIFICATION_SOUNDS, notification);

        // Check if a row exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            // If at least one row exists, update the value
            int rowsAffected = db.update(
                    DbContract.appSettings.TABLE_NAME,
                    values,
                    null,
                    null
            );

            if (rowsAffected > 0) {
                Log.i("DB_UPDATE", "Notification updated successfully: " + notification);
            } else {
                Log.e("DB_ERROR", "Failed to update theme");
            }
        } else {
            // If no row exists, create a new row with default values
            long newRowId = db.insert(DbContract.appSettings.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Log.i("DB_INSERT", "New row inserted with theme: " + notification);
            } else {
                Log.e("DB_ERROR", "Failed to insert new row");
            }
        }

        cursor.close();  // Close the cursor to avoid potential memory leaks
    }
    public void changeNotifications(boolean notifications) {
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_NOTIFICATIONS, notifications ? 1 : 0);

        int rowsAffected = db.update(DbContract.appSettings.TABLE_NAME, values, null, null);

        if (rowsAffected == 0) {
            // No row was updated, so insert a new row
            db.insert(DbContract.appSettings.TABLE_NAME, null, values);
        }

        Log.i("SQLMOD", "changeNotifications: Rows affected - " + rowsAffected);
    }
    public void changeNotificationsName(String name){
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_NOTIFICATIONS_NAME, name);

        // Check if a row exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            // If at least one row exists, update the value
            int rowsAffected = db.update(
                    DbContract.appSettings.TABLE_NAME,
                    values,
                    null,
                    null
            );

            if (rowsAffected > 0) {
                Log.i("DB_UPDATE", "Background updated successfully: " + name);
            } else {
                Log.e("DB_ERROR", "Failed to update background");
            }
        } else {
            // If no row exists, create a new row with default values
            long newRowId = db.insert(DbContract.appSettings.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Log.i("DB_INSERT", "New row inserted with background: " + name);
            } else {
                Log.e("DB_ERROR", "Failed to insert new row");
            }
        }

        cursor.close();  // Close the cursor to avoid potential memory leaks
    }
    public void changeSounds(boolean sounds) {
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_SOUNDS, sounds ? 1 : 0);

        int rowsAffected = db.update(DbContract.appSettings.TABLE_NAME, values, null, null);

        if (rowsAffected == 0) {
            // No row was updated, so insert a new row
            db.insert(DbContract.appSettings.TABLE_NAME, null, values);
        }

        Log.i("SQLMOD", "changeSounds: Rows affected - " + rowsAffected);
    }
    public void changeRingtoneName(String name){
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_RINGTONE_NAME, name);

        // Check if a row exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            // If at least one row exists, update the value
            int rowsAffected = db.update(
                    DbContract.appSettings.TABLE_NAME,
                    values,
                    null,
                    null
            );

            if (rowsAffected > 0) {
                Log.i("DB_UPDATE", "Background updated successfully: " + name);
            } else {
                Log.e("DB_ERROR", "Failed to update background");
            }
        } else {
            // If no row exists, create a new row with default values
            long newRowId = db.insert(DbContract.appSettings.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Log.i("DB_INSERT", "New row inserted with background: " + name);
            } else {
                Log.e("DB_ERROR", "Failed to insert new row");
            }
        }

        cursor.close();  // Close the cursor to avoid potential memory leaks
    }
    public void changeFocus(boolean focus) {
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_FOCUS, focus ? 1 : 0);

        int rowsAffected = db.update(DbContract.appSettings.TABLE_NAME, values, null, null);

        if (rowsAffected == 0) {
            // No row was updated, so insert a new row
            db.insert(DbContract.appSettings.TABLE_NAME, null, values);
        }

        Log.i("SQLMOD", "changeFocus: Rows affected - " + rowsAffected);
    }
    public void changeHomeType(boolean homeType) {
        ContentValues values = new ContentValues();
        values.put(DbContract.appSettings.COLUMN_NAME_HOME_TYPE, homeType ? 1 : 0);

        int rowsAffected = db.update(DbContract.appSettings.TABLE_NAME, values, null, null);

        if (rowsAffected == 0) {
            // No row was updated, so insert a new row
            db.insert(DbContract.appSettings.TABLE_NAME, null, values);
        }

        Log.i("SQLMOD", "changeHomeType: Rows affected - " + rowsAffected);
    }

    public static String getTheme() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DbContract.appSettings.COLUMN_NAME_THEME);

            if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                String theme = cursor.getString(columnIndex);
                cursor.close();  // Close the cursor to avoid potential memory leaks
                return theme;
            } else {
                Log.e("DB_ERROR", "Column index is -1 for theme column");
                cursor.close();
                return "PastelTheme";
            }
        } else {
            Log.i("DB_INFO", "No rows found in the database, returning default theme");
            cursor.close();
            return "PastelTheme";
        }
    }
    public String getBackground() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DbContract.appSettings.COLUMN_NAME_BACKGROUND);

            if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                String theme = cursor.getString(columnIndex);
                cursor.close();  // Close the cursor to avoid potential memory leaks
                return theme;
            } else {
                Log.e("DB_ERROR", "Column index is -1 for background column");
                cursor.close();
                return "no background";
            }
        } else {
            Log.i("DB_INFO", "No rows found in the database, returning default theme");
            cursor.close();
            return "no background";
        }
    }
    public String getBackgroundName(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DbContract.appSettings.COLUMN_NAME_BACKGROUND_NAME);

            if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                String theme = cursor.getString(columnIndex);
                cursor.close();  // Close the cursor to avoid potential memory leaks
                return theme;
            } else {
                Log.e("DB_ERROR", "Column index is -1 for background column");
                cursor.close();
                return "nessuno sfondo";
            }
        } else {
            Log.i("DB_INFO", "No rows found in the database, returning default name");
            cursor.close();
            return "nessuno sfondo";
        }
    }
    public int getVolume() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        try {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(DbContract.appSettings.COLUMN_NAME_VOLUME);
                int volume = cursor.getInt(columnIndex);
                cursor.close();
                return volume;
            } else {
                cursor.close();
                return 5;
            }
        } catch (IllegalArgumentException e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            cursor.close();
            return 5;
        } finally {
            cursor.close();  // Close the cursor to avoid potential memory leaks
        }
    }
    public String getRingtone() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DbContract.appSettings.COLUMN_NAME_RINGTONE);

            if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                String theme = cursor.getString(columnIndex);
                cursor.close();  // Close the cursor to avoid potential memory leaks
                return theme;
            } else {
                Log.e("DB_ERROR", "Column index is -1 for theme column");
                cursor.close();
                return "default ringtone";
            }
        } else {
            Log.i("DB_INFO", "No rows found in the database, returning default theme");
            cursor.close();
            return "default ringtone";
        }
    }
    public String getNotification() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DbContract.appSettings.COLUMN_NAME_NOTIFICATION_SOUNDS);

            if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                String theme = cursor.getString(columnIndex);
                cursor.close();  // Close the cursor to avoid potential memory leaks
                return theme;
            } else {
                Log.e("DB_ERROR", "Column index is -1 for theme column");
                cursor.close();
                return "default notification";
            }
        } else {
            Log.i("DB_INFO", "No rows found in the database, returning default theme");
            cursor.close();
            return "default notification";
        }
    }
    public boolean getNotifications() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        try {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(DbContract.appSettings.COLUMN_NAME_NOTIFICATIONS);
                int notifications = cursor.getInt(columnIndex);
                cursor.close();
                return notifications == 1;
            } else {
                cursor.close();
                return false;
            }
        } catch (IllegalArgumentException e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            cursor.close();
            return false;
        } finally {
            cursor.close();  // Close the cursor to avoid potential memory leaks
        }
    }
    public String getNotificationName(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        try {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(DbContract.appSettings.COLUMN_NAME_NOTIFICATIONS_NAME);
                String notifications = cursor.getString(columnIndex);
                cursor.close();
                return notifications;
            } else {
                cursor.close();
                return "nessun suono di notifica";
            }
        } catch (IllegalArgumentException e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            cursor.close();
            return "nessun suono di notifica";
        } finally {
            cursor.close();  // Close the cursor to avoid potential memory leaks
        }

    }
    public boolean getSounds() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        try {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(DbContract.appSettings.COLUMN_NAME_SOUNDS);
                int sounds = cursor.getInt(columnIndex);
                cursor.close();
                return sounds == 1;
            } else {
                cursor.close();
                return false;
            }
        } catch (IllegalArgumentException e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            cursor.close();
            return false;
        } finally {
            cursor.close();  // Close the cursor to avoid potential memory leaks
        }
    }
    public String getRingtoneName(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        try {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(DbContract.appSettings.COLUMN_NAME_RINGTONE_NAME);
                String sounds = cursor.getString(columnIndex);
                cursor.close();
                return sounds;
            } else {
                cursor.close();
                return "nessuna suoneria";
            }
        } catch (IllegalArgumentException e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            cursor.close();
            return "nessuna suoneria";
        } finally {
            cursor.close();  // Close the cursor to avoid potential memory leaks
        }
    }
    public boolean getFocus() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);

        try {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(DbContract.appSettings.COLUMN_NAME_FOCUS);
                int focus = cursor.getInt(columnIndex);
                cursor.close();
                return focus == 1;
            } else {
                cursor.close();
                return false;
            }
        } catch (IllegalArgumentException e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            cursor.close();
            return false;
        } finally {
            cursor.close();  // Close the cursor to avoid potential memory leaks
        }
    }
    public boolean getHomeType() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.appSettings.TABLE_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(DbContract.appSettings.COLUMN_NAME_HOME_TYPE);
                int homeType = cursor.getInt(columnIndex);
                cursor.close();
                return homeType == 1;
            } else {
                cursor.close();
                return false;
            }
        } catch (IllegalArgumentException e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            cursor.close();
            return false;
        } finally {
            cursor.close();  // Close the cursor to avoid potential memory leaks
        }
    }


    public void changeGamification(boolean gamification) {
        ContentValues values = new ContentValues();
        values.put(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION, gamification ? 1 : 0);

        int rowsAffected = db.update(
                DbContract.gamificationSettings.TABLE_NAME,
                values,
                null,
                null
        );

        if (rowsAffected == 0) {
            // No rows were updated, insert a new row
            db.insert(DbContract.gamificationSettings.TABLE_NAME, null, values);
        }

        Log.i("SQLMOD", "Gamification updated: " + gamification);
    }
    public void changeGamificationType(boolean gamificationType) {
        int gamificationTypeValue = gamificationType ? 1 : 0;

        // Check if a row exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.gamificationSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            // If at least one row exists, update the value
            String updateQuery =
                    "update " + DbContract.gamificationSettings.TABLE_NAME +
                            " set " + DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TYPE + "=" + gamificationTypeValue;
            db.execSQL(updateQuery);
            Log.i("SQLMOD", updateQuery);
        } else {
            // If no row exists, create a new row with default values
            ContentValues values = new ContentValues();
            values.put(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TYPE, gamificationTypeValue);
            db.insert(DbContract.gamificationSettings.TABLE_NAME, null, values);

            Log.i("SQLMOD", "New row inserted with gamificationType: " + gamificationTypeValue);
        }

        cursor.close();  // Close the cursor to avoid potential memory leaks
    }
    public void changePositiveIcon(String positiveIcon) {
        String updateQuery =
                "update "+ DbContract.gamificationSettings.TABLE_NAME +
                        " set "
                        + DbContract.gamificationSettings.COLUMN_NAME_POSITIVE_ICON + "='" + positiveIcon + "'";
        db.execSQL(updateQuery);
        Log.i("SQLMOD", updateQuery);
    }
    public void changeNegativeIcon(String negativeIcon) {
        String updateQuery =
                "update "+ DbContract.gamificationSettings.TABLE_NAME +
                        " set "
                        + DbContract.gamificationSettings.COLUMN_NAME_NEGATIVE_ICON + "='" + negativeIcon + "'";
        db.execSQL(updateQuery);
        Log.i("SQLMOD", updateQuery);
    }
    public void changeGamificationPoints(int gamificationPoints1, int gamificationPoints2, int gamificationPoints3, int gamificationPoints4, int gamificationPoints5, int gamificationPoints6) {
        ContentValues values = new ContentValues();
        values.put(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_1, gamificationPoints1);
        values.put(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_2, gamificationPoints2);
        values.put(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_3, gamificationPoints3);
        values.put(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_4, gamificationPoints4);
        values.put(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_5, gamificationPoints5);
        values.put(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TIME_POINTS_6, gamificationPoints6);

        db.update(DbContract.gamificationSettings.TABLE_NAME, values, null, null);

        Log.i("SQLMOD", "Updated gamificationPoints in the database");
    }

    public boolean getGamification() {
        String[] projection = {
                DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION
        };

        Cursor cursor = db.query(
                DbContract.gamificationSettings.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        boolean userExists = cursor.moveToFirst();
        boolean gamificationValue = true;

        if (userExists && !cursor.isNull(cursor.getColumnIndexOrThrow(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION))) {
            gamificationValue = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION)) == 1;
        }

        cursor.close();
        return gamificationValue;
    }
    public boolean getGamificationType() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.gamificationSettings.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        boolean gamificationType = false;

        if (userExists) {
            int columnIndex = cursor.getColumnIndex(DbContract.gamificationSettings.COLUMN_NAME_GAMIFICATION_TYPE);

            if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                gamificationType = cursor.getInt(columnIndex) == 1;
                Log.i("getGamificationType", "Read gamificationType from database: " + gamificationType);
            } else {
                // Handle the case where the column index is not found
                // You might want to log an error or set a default value
                Log.e("getGamificationType", "Column index not found for gamificationType");
            }
        } else {
            Log.i("getGamificationType", "No rows found, returning default value");
        }

        cursor.close();  // Close the cursor to avoid potential memory leaks
        return gamificationType;
    }
    public int[] getGamificationPoints() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.gamificationSettings.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        int[] gamificationPoints = new int[6];
        gamificationPoints[0] = 10;
        gamificationPoints[1] = 50;
        gamificationPoints[2] = 100;
        gamificationPoints[3] = 10;
        gamificationPoints[4] = -10;
        gamificationPoints[5] = -50;

        if (userExists ) {
            for (int i = 0; i < 6; i++) {
                String columnName = "gamificationTimePoints" + (i + 1);
                int columnIndex = cursor.getColumnIndex(columnName);
                if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                    gamificationPoints[i] = cursor.getInt(columnIndex);
                } else {
                    Log.e("getGamificationPoints", "Column index not found or null value for gamificationPoints[" + i + "]");
                }
            }
            Log.i("getGamificationPoints", "Read gamificationPoints from database");
        } else {
            // If no row exists, set default values
            Log.i("getGamificationPoints", "No rows found, returning default values");
        }

        cursor.close();  // Close the cursor to avoid potential memory leaks
        return gamificationPoints;
    }
    public String getPositiveIconStr() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.gamificationSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DbContract.gamificationSettings.COLUMN_NAME_POSITIVE_ICON);

            if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                String positiveIcon = cursor.getString(columnIndex);
                cursor.close();
                return positiveIcon;
            } else {
                Log.e("PositiveIcon", "Column not found or null" + DbContract.gamificationSettings.COLUMN_NAME_POSITIVE_ICON);
            }
        } else {
            Log.e("PositiveIcon", "No data found in the cursor.");
        }
        cursor.close();
        return res.getText(R.string.happyDogIcon).toString(); // Default value
    }
    public String getNegativeIconStr() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbContract.gamificationSettings.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DbContract.gamificationSettings.COLUMN_NAME_NEGATIVE_ICON);

            if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                String negativeIcon = cursor.getString(columnIndex);
                cursor.close();
                return negativeIcon;
            } else {
                Log.e("NegativeIcon", "Column not found: " + DbContract.gamificationSettings.COLUMN_NAME_NEGATIVE_ICON);
            }
        } else {
            Log.e("NegativeIcon", "No data found in the cursor.");
        }
        cursor.close();
        return res.getText(R.string.sadDogIcon).toString(); // Default value
    }


    public Drawable getNegativeIcon()
    {
        String negativeIconStr = getNegativeIconStr();

        if(negativeIconStr.equals("sad_cat"))
        {
            return res.getDrawable(R.drawable.sad_cat,null);
        }else
        {
            return res.getDrawable(R.drawable.sad_dog,null);
        }

    }

    public Drawable getPositiveIcon()
    {
        String positiveIconStr = getPositiveIconStr();

        if(positiveIconStr.equals("happy_cat"))
        {
            return res.getDrawable(R.drawable.happy_cat,null);
        }
        else {
            return res.getDrawable(R.drawable.happy_dog,null);
        }

    }


    public void closeDb() {
        db.close();

    }
    private String scanQueryBuilder(TaskItem itemToLookFor) {
        String scanQuery =
                "select * from "+DbContract.Activities.TABLE_NAME
                        + " where ";

        for(int i =1 ;i<=DbContract.Activities.DIM_MAX;i++)
        {
            String condition = DbContract.Activities.TABLE_NAME +".task"+ i + "="+ itemToLookFor.getIdStr();
            //Log.i("condition",condition);
            if(i<DbContract.Activities.DIM_MAX)
            {
                condition=condition.concat(" OR ");
            }
            scanQuery = scanQuery.concat(condition);
        }

        return scanQuery;

    }


    public int insertActivitySchedule(int activityId, GregorianCalendar infoCalendarFormat, AlarmInfo.Frequency frequency)
    {
        String queryStr = "insert into "+DbContract.ActivitySchedule.TABLE_NAME + " ("+
                DbContract.ActivitySchedule.COLUMN_NAME_ACTIVITY_ID + "," +
                DbContract.ActivitySchedule.COLUMN_NAME_YEAR +"," +
                DbContract.ActivitySchedule.COLUMN_NAME_MONTH +","+
                DbContract.ActivitySchedule.COLUMN_NAME_DAY +","+
                DbContract.ActivitySchedule.COLUMN_NAME_HOUR +"," +
                DbContract.ActivitySchedule.COLUMN_NAME_MINUTES + "," +
                DbContract.ActivitySchedule.COLUMN_NAME_FREQUENCY+") values ( "+
                activityId +"," +
                infoCalendarFormat.get(Calendar.YEAR) + "," +
                infoCalendarFormat.get(Calendar.MONTH) + "," +
                infoCalendarFormat.get(Calendar.DAY_OF_MONTH) + "," +
                infoCalendarFormat.get(Calendar.HOUR_OF_DAY) + "," +
                infoCalendarFormat.get(Calendar.MINUTE) + ",'" +
                frequency.toString() +"');";

        Log.i("queryStr",queryStr);
        db.execSQL(queryStr);

        queryStr = "select MAX(" + DbContract.ActivitySchedule._ID +") from "+ DbContract.ActivitySchedule.TABLE_NAME ;

        Log.i("queryStr",queryStr);
        Cursor c = db.rawQuery(queryStr,null);
        int max = -1;
        if(c.getCount()>0)
        {
            c.moveToFirst();
            Log.i("match found","max is "+c.getInt(0));
            max = c.getInt(0);
        }
        else
        {
            Log.i("match NOT found","i don't know the max yet");
        }

        c.close();
        return max;

    }

    public void deleteActivitySchedule(int scheduleId)
    {
        String queryStr =
                "delete from "+DbContract.ActivitySchedule.TABLE_NAME +
                " where " +DbContract.ActivitySchedule._ID + "=" +scheduleId;

        db.execSQL(queryStr);
    }

    /*public Cursor selectAllActivitySchedule()
    {
        String queryStr = "select * from "+ DbContract.ActivitySchedule.TABLE_NAME;

        return db.rawQuery(queryStr,null);
    }*/

    public Cursor getActivityScheduleInfo(int activityId)
    {
        String queryStr = "select * from "+DbContract.ActivitySchedule.TABLE_NAME +
                " where " + DbContract.ActivitySchedule.COLUMN_NAME_ACTIVITY_ID + "=" + activityId;

        Log.i("selectQuery",queryStr);
        //Cursor c = db.rawQuery(queryStr,null);

        //return c;

        return db.rawQuery(queryStr,null);
    }

    public AlarmInfo selectScheduleById(int scheduleId)
    {

        AlarmInfo res;
        String queryStr = "select * from "+DbContract.ActivitySchedule.TABLE_NAME +
            " where " + DbContract.ActivitySchedule._ID +"="+scheduleId;
        Log.i("queryStr sched",queryStr);
        Cursor c = db.rawQuery(queryStr,null);

        if(c.getCount()>0) {
            c.moveToFirst();
            res = new AlarmInfo(c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6), AlarmInfo.Frequency.valueOf(c.getString(7)));
            Log.i("res", res.toString());

        }
        else
        {
            Log.i("something","went wrong here");
            res=null;
        }
        c.close();

        return res;

    }

    /*public GregorianCalendar getActivityScheduleInfoGC(int activityId,int scheduleId) {
        GregorianCalendar calendar;
        boolean notfound = true;

        Cursor c = getActivityScheduleInfo(activityId);

        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
                if (c.getInt(0) == scheduleId) {
                    notfound = false;
                }
            } while (c.moveToNext() && notfound);


            calendar = new GregorianCalendar(c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6));


        } else
            calendar = new GregorianCalendar();


        return calendar;
    }*/


    /*public void selectAndPrintAllReportData()
    {
        String queryStr = "select * from "+DbContract.reportData.TABLE_NAME;

        Cursor c = db.rawQuery(queryStr,null);


        if(c.getCount()>0)
        {
            c.moveToFirst();

            do{
                Log.i("RDretr",c.getString(0) +","+ c.getString(1) +","+c.getString(2)+","+c.getInt(3));
            }while(c.moveToNext());

        }else
            Log.i("RDretr","currently emptyRD");

        //return c;
    }*/

    public void insertFullReportData(int actId,List<ReportData> reportDataList)
    {
        int pos=0;
        for(ReportData RD : reportDataList)
        {
            insertReportData(RD,pos,actId);
            pos++;
        }


    }
    public void insertReportData(ReportData RD,int pos,int actId)
    {
        String metadataStr = actId+"-"+pos+"-"+RD.getMetadata().getSubtaskInfotoStr();

        String queryStr = "insert into "+DbContract.reportData.TABLE_NAME +" (" +
                DbContract.reportData.COLUMN_NAME_METADATA + ","+
                DbContract.reportData.COLUMN_NAME_ENDSTATUS + "," +
                DbContract.reportData.COLUMN_NAME_LASTED_TIME + ") values ('"+
                metadataStr + "','"+RD.endStatus.toString() + "'," + RD.lastedTime + ");";

        Log.i("queryStr",queryStr);

        db.execSQL(queryStr);
    }

    public Cursor retrieveReportDataByActId(int actId)
    {
        String patternToSearchFor = "'"+actId+"-%'";
        String queryStr =
                "select * from "+DbContract.reportData.TABLE_NAME +
                " where "+DbContract.reportData.COLUMN_NAME_METADATA + " like " +  patternToSearchFor ;

        Log.i("retrQuery",queryStr);
        return db.rawQuery(queryStr,null);
    }



    public void insertPremadeTasks()
    {
        String insertionQueryBase = "insert into " + DbContract.Tasks.TABLE_NAME + "("
                + DbContract.Tasks.COLUMN_NAME_TASK_NAME +","
                + DbContract.Tasks.COLUMN_NAME_TASK_DURATION +","
                + DbContract.Tasks.COLUMN_NAME_IMG + ") values ";

        String space = ",";

        String firstPremadeTaskQuery = "('lavare i denti',15,'empty')";
        String secondPremadeTaskQuery = "('fare colazione',15,'empty')";
        String thirdPremadeTaskQuery = "('fare lo zaino',10,'empty')";
        String fourthPremadeTaskQuery = "('fare la doccia',20,'empty')";
        String fifthPremadeTaskQuery = "('fare merenda',20,'empty')";

        String finalQuery = insertionQueryBase +
                firstPremadeTaskQuery + space +
                secondPremadeTaskQuery + space +
                thirdPremadeTaskQuery + space +
                fourthPremadeTaskQuery + space +
                fifthPremadeTaskQuery;

        Log.i("insertion query",finalQuery);
        db.execSQL(finalQuery);
    }

}
