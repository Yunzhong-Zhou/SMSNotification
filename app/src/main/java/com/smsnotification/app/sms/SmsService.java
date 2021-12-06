package com.smsnotification.app.sms;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import com.smsnotification.app.MainActivity2;
import com.xdandroid.hellodaemon.AbsWorkService;

import androidx.annotation.Nullable;

/**
 * Created by Mr.Z on 2021/12/6.
 *  开启一个服务开监听数据库
 */
public class SmsService extends AbsWorkService {

    private SmsObserver mObserver;

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return null;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {

    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {

    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "SmsService 服务器启动了....", Toast.LENGTH_SHORT).show();
        // 在这里启动
        ContentResolver resolver = getContentResolver();
        mObserver = new SmsObserver(resolver, new MainActivity2.SmsHandler(this));
        resolver.registerContentObserver(Uri.parse("content://sms"), true,mObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(mObserver);
//        Process.killProcess(Process.myPid());
    }
}
