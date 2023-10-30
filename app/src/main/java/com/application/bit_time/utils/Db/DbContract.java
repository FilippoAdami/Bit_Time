package com.application.bit_time.utils.Db;

import android.provider.BaseColumns;
import android.util.Log;

public final class DbContract {


    private DbContract(){
        Log.i("DB_INFO","activityContract is on");
    }

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



    }


    public static class Tasks implements  BaseColumns
    {
        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_NAME_TASK_NAME ="name";
        public static final String COLUMN_NAME_TASK_DURATION = "duration";


    }


    public static class Userdata implements  BaseColumns
    {
        public static final String TABLE_NAME = "userdata";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_PIN = "pin";


    }





}
