package com.smsnotification.app;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Vibrator;

/**
 * Created by Mr.Z on 2021/12/5.
 */
public class VibratorUtils {
    /**
     * 开始震动
     * @param context
     * @param pattern 震动规则
     * @param repeat 循环次数
     */
    public static void startVibrator(Context context, long[] pattern, int repeat) {
        Vibrator vibrator = (Vibrator) MyApplication.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();
        vibrator.vibrate(pattern, repeat);
        AudioAttributes audioAttributes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM) //key USAGE_NOTIFICATION_RINGTONE  USAGE_NOTIFICATION
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();
            vibrator.vibrate(pattern, 1, audioAttributes);
        }else {
            vibrator.vibrate(pattern, 1);
        }
    }

    /**
     * 关闭震动
     *
     * @param context
     */
    public static void cancelVibrator(Context context) {
        Vibrator vibrator = (Vibrator) MyApplication.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();
    }
}
