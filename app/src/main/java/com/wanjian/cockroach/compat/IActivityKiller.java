package com.wanjian.cockroach.compat;

import android.os.Message;

/**
 * Created by generallizhong on 2019/11/26.
 */

public interface IActivityKiller {

    void finishLaunchActivity(Message message);

    void finishResumeActivity(Message message);

    void finishPauseActivity(Message message);

    void finishStopActivity(Message message);


}
