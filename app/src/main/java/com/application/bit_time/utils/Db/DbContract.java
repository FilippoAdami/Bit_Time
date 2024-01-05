package com.application.bit_time.utils.Db;

import android.provider.BaseColumns;
import android.util.Log;

public final class DbContract {


    private DbContract(){
        Log.i("DB_INFO","activityContract is on");
    }

    public static int timescores = 6;
    public static class Activities implements BaseColumns
    {
        public static final String TABLE_NAME = "activities";
        public static final String COLUMN_NAME_ACTIVITY_NAME = "name";
        public static final String COLUMN_NAME_ACTIVITY_DURATION = "duration";
        public static final int DIM_MAX = 5;

        public static final String COLUMN_NAME_TASK1  = "task1";
        public static final String COLUMN_NAME_TASK2  = "task2";
        public static final String COLUMN_NAME_TASK3  = "task3";
        public static final String COLUMN_NAME_TASK4  = "task4";
        public static final String COLUMN_NAME_TASK5  = "task5";
        public static final String COLUMN_NAME_IS_PLANNED = "isPlanned";



    }

    public static class Tasks implements  BaseColumns
    {
        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_NAME_TASK_NAME ="name";
        public static final String COLUMN_NAME_TASK_DURATION = "duration";


    }

    public static class Userdata implements  BaseColumns{
        public static final String TABLE_NAME = "userdata";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_PIN = "pin";
        public static final String COLUMN_NAME_SALT = "salt";
    }


    public static class ActivitySchedule implements BaseColumns
    {
        public static final String TABLE_NAME = "activitySchedule";
        public static final String COLUMN_NAME_ACTIVITY_ID = "activityId";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_MONTH = "month";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_HOUR ="hour";
        public static final String COLUMN_NAME_MINUTES = "minutes";
        public static final String COLUMN_NAME_FREQUENCY = "frequency";

    }

    public static class appSettings implements BaseColumns{
        public static final String TABLE_NAME = "appSettings";
        public static final String COLUMN_NAME_THEME = "theme";
        public static final String COLUMN_NAME_BACKGROUND = "background";
        public static final String COLUMN_NAME_VOLUME = "volume";
        public static final String COLUMN_NAME_RINGTONE = "ringtone";
        public static final String COLUMN_NAME_NOTIFICATION_SOUNDS = "notification_sounds";
        public static final String COLUMN_NAME_NOTIFICATIONS = "notifications";
        public static final String COLUMN_NAME_SOUNDS = "sounds";
        public static final String COLUMN_NAME_FOCUS = "focus";
        public static final String COLUMN_NAME_HOME_TYPE = "home_type";
    }

    public static class gamificationSettings implements BaseColumns{
        public static final String TABLE_NAME = "gamificationSettings";
        public static final String COLUMN_NAME_GAMIFICATION = "gamification";
        public static final String COLUMN_NAME_GAMIFICATION_TYPE = "gamificationType";
        public static final String COLUMN_NAME_POSITIVE_ICON = "positiveIcon";
        public static final String COLUMN_NAME_NEGATIVE_ICON = "negativeIcon";
        public static final String COLUMN_NAME_GAMIFICATION_TIME_POINTS_1 = "gamificationTimePoints1";
        public static final String COLUMN_NAME_GAMIFICATION_TIME_POINTS_2 = "gamificationTimePoints2";
        public static final String COLUMN_NAME_GAMIFICATION_TIME_POINTS_3 = "gamificationTimePoints3";
        public static final String COLUMN_NAME_GAMIFICATION_TIME_POINTS_4 = "gamificationTimePoints4";
        public static final String COLUMN_NAME_GAMIFICATION_TIME_POINTS_5 = "gamificationTimePoints5";
        public static final String COLUMN_NAME_GAMIFICATION_TIME_POINTS_6 = "gamificationTimePoints6";
    }

    public static class reportData implements BaseColumns
    {
        public static final String TABLE_NAME = "reportData";
        public static final String COLUMN_NAME_METADATA = "metadata";
        public static final String COLUMN_NAME_LASTED_TIME = "lastedtime";
        public static final String COLUMN_NAME_ENDSTATUS = "endstatus";
    }
}
