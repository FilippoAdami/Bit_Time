package com.application.bit_time.utils.AlarmUtils;

public interface AlarmSchedulerInterface {
    void schedule(AlarmInfo info);
    void cancel(AlarmInfo info);

}
