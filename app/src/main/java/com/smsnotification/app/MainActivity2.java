package com.smsnotification.app;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;

import com.cretin.tools.fanpermission.FanPermissionListener;
import com.cretin.tools.fanpermission.FanPermissionUtils;
import com.smsnotification.app.music.IMusicPlay;
import com.smsnotification.app.music.MyService;
import com.smsnotification.app.sms.SmsInfo;
import com.smsnotification.app.sms.SmsReceiver;
import com.smsnotification.app.sms.SmsService;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    // 音乐文件到路径
//    private final String MUSIC_PATH = Environment.getExternalStorageDirectory() + "/cjyy.mp3";
    private final String MUSIC_PATH = "http://music.163.com/song/media/outer/url?id=39224325.mp3";

    SmsReceiver smsReceiver;

    public static IMusicPlay iMusicPlay;
    private ServiceConnection connection = new ServiceConnection() {
        /**
         * 连接到服务
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMusicPlay = (IMusicPlay) service;
        }

        /**
         * 断开连接
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    //创建震动服务对象
//    private Vibrator mVibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FanPermissionUtils.with(MainActivity2.this)
                //添加所有你需要申请的权限
//                .addPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)//写入
//                .addPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)//读取
//                .addPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)//定位
//                .addPermissions(Manifest.permission.ACCESS_FINE_LOCATION)//定位
//                .addPermissions(Manifest.permission.CALL_PHONE)//拨打电话
//                .addPermissions(Manifest.permission.READ_PHONE_STATE)//读取手机状态
//                .addPermissions(Manifest.permission.ACCESS_WIFI_STATE)//访问WiFi状态
//                .addPermissions(Manifest.permission.CAMERA)//相机
//                .addPermissions(Manifest.permission.SEND_SMS)//发送短信
                .addPermissions(Manifest.permission.RECEIVE_WAP_PUSH)//读取短信
                .addPermissions(Manifest.permission.RECEIVE_MMS)//读取短信
                .addPermissions(Manifest.permission.READ_SMS)//读取短信
                .addPermissions(Manifest.permission.RECEIVE_SMS)//接收短信
                .addPermissions(Manifest.permission.FOREGROUND_SERVICE)//前台服务
                .addPermissions(Manifest.permission.VIBRATE)//震动
//                .addPermissions(Manifest.permission.REQUEST_INSTALL_PACKAGES)//安装apk
                //添加权限申请回调监听 如果申请失败 会返回已申请成功的权限列表，用户拒绝的权限列表和用户点击了不再提醒的永久拒绝的权限列表
                .setPermissionsCheckListener(new FanPermissionListener() {
                    @Override
                    public void permissionRequestSuccess() {
                        smsReceiver = new SmsReceiver();

                        Intent intentService = new Intent(MainActivity2.this, SmsService.class);
                        startService(intentService);

                        startService(new Intent(MainActivity2.this, MyService.class));
                    }


                    @Override
                    public void permissionRequestFail(String[] grantedPermissions, String[] deniedPermissions, String[] forceDeniedPermissions) {
                        //当有权限没有被授权就会回调这里
                        //会返回已申请成功的权限列表（grantedPermissions）
                        //用户拒绝的权限列表（deniedPermissions）
                        //用户点击了不再提醒的永久拒绝的权限列表（forceDeniedPermissions）
                    }
                })
                //生成配置
                .createConfig()
                //配置是否强制用户授权才可以使用，当设置为true的时候，如果用户拒绝授权，会一直弹出授权框让用户授权
                .setForceAllPermissionsGranted(true)
                //配置当用户点击了不再提示的时候，会弹窗指引用户去设置页面授权，这个参数是弹窗里面的提示内容
                .setForceDeniedPermissionTips("请前往设置->应用->【" + FanPermissionUtils.getAppName(MainActivity2.this) + "】->权限中打开相关权限，否则功能无法正常运行！")
                //构建配置并生效
                .buildConfig()
                //开始授权
                .startCheckPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!KeepLiveUtils.isIgnoringBatteryOptimizations(MainActivity2.this)) {
                KeepLiveUtils.requestIgnoreBatteryOptimizations(MainActivity2.this);
            }

        }
        findViewById(R.id.button1).setOnClickListener(v -> {
            //取消震动
//            mVibrator.cancel();
            VibratorUtils.cancelVibrator(this);
//            //关闭音乐
            iMusicPlay.stopPlay();
        });

        /*findViewById(R.id.button2).setOnClickListener(v->{
            //手动设置白名单
            KeepLiveUtils.goKeepLiveSetting(MainActivity.this);
        });*/
        findViewById(R.id.button3).setOnClickListener(v -> {
            //跳转应用权限管理
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri1 = Uri.fromParts("package", MainActivity2.this.getPackageName(), null);
            intent.setData(uri1);
            startActivity(intent);

        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        // 绑定音乐服务
        bindService(new Intent(MainActivity2.this, MyService.class), connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑服务：注意bindService后 必须要解绑服务，否则会报 连接资源异常
        if (null != connection) {
            unbindService(connection);
        }
    }

    /**
     * 用户按返回键，系统会调用到此方法
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        // 既然是后台播放，就是要把当前Activity切换到后台
        moveTaskToBack(true);
        /*// 判断是否在播放，如果在播放中，才告知用户 弹出对话框
        if (iMusicPlay == null) {
            return true;
        }
        MediaPlayer mediaPlayer = iMusicPlay.getMediaPlayer();
        if(mediaPlayer.isPlaying()) {

            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    // 既然是后台播放，就是要把当前Activity切换到后台
                    moveTaskToBack(true);
                    break;
            }
        }*/
        return true;
    }

    public static class SmsHandler extends Handler {
        private Context mContext;

        public SmsHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            SmsInfo smsInfo = (SmsInfo) msg.obj;
//            Toast.makeText(mContext, "来短信了", Toast.LENGTH_SHORT).show();

            // 启用震动
            VibratorUtils.startVibrator(mContext, new long[]{1000, 1000}, 0);
            //播放音乐
            iMusicPlay.playMusic("http://music.163.com/song/media/outer/url?id=39224325.mp3");


            if (smsInfo.action == 1) {
                ContentValues values = new ContentValues();
                values.put("read", "1");
                mContext.getContentResolver().update(
                        Uri.parse("content://sms/inbox"), values, "thread_id=?",
                        new String[]{smsInfo.thread_id});
            } else if (smsInfo.action == 2) {
                Uri mUri = Uri.parse("content://sms/");
                mContext.getContentResolver().delete(mUri, "_id=?",
                        new String[]{smsInfo._id});
            }
        }
    }
    /*@Override
    protected  void onDestroy(){
        unregisterReceiver(receiver);//解绑广播接收器
    }*/
}