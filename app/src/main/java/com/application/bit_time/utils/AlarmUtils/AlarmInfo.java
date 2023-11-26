package com.application.bit_time.utils.AlarmUtils;

import android.util.Log;

import java.time.LocalDateTime;

public class AlarmInfo {



   DateTimeUtil dateTimeUtil;

    public void setDateTimeUtil(DateTimeUtil dateTimeUtil)
    {
        this.dateTimeUtil= new DateTimeUtil(dateTimeUtil);
    }

    public long getAlarmTimeLong()
    {
        long res = 0;


        return res;

    }

    public AlarmInfo()
    {
        this.dateTimeUtil = null;
    }


}
