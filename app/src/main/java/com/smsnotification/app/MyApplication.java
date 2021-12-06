package com.smsnotification.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.smsnotification.app.music.MyService;
import com.smsnotification.app.sms.SmsService;
import com.xdandroid.hellodaemon.DaemonEnv;

/**
 * Created by zyz on 2018/1/18.
 */

public class MyApplication extends Application {
    // 上下文菜单
    private static Context mContext;

    private static MyApplication myApplication;

    public static MyApplication getInstance() {
        return myApplication;
    }

    private Handler handler;

    @Override
    public final void onCreate() {
        super.onCreate();
        mContext = this;
        myApplication = this;

        //保活 需要在 Application 的 onCreate() 中调用一次 DaemonEnv.initialize()
//        DaemonEnv.initialize(this, MyService.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        DaemonEnv.initialize(this, MyService.class, 30 * 1000);
        DaemonEnv.startServiceMayBind(MyService.class);

        DaemonEnv.initialize(this, SmsService.class, 30 * 1000);
        DaemonEnv.startServiceMayBind(SmsService.class);

    }

    public static Context getContext() {
        return mContext;
    }

}
