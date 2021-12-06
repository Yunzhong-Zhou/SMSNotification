package com.smsnotification.app.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Mr.Z on 2021/12/6.
 * 拦截广播
 */
public class SmsReceiver extends BroadcastReceiver {
    private Context mContext;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext=context;
        Toast.makeText(context, "接收短信执行了.....", Toast.LENGTH_SHORT).show();
        Log.e("SMSReceiver", isOrderedBroadcast()+"");
        Log.e("SmsReceiver", "接收短信执行了......");
        String action = intent.getAction();
        if (SMS_RECEIVED_ACTION.equals(action) || SMS_DELIVER_ACTION.equals(action)) {
            Toast.makeText(context, "开始接收短信.....", Toast.LENGTH_SHORT).show();
            Log.e("SmsReceiver", "开始接收短信.....");

            /*Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                if (pdus != null && pdus.length > 0) {
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        byte[] pdu = (byte[]) pdus[i];
                        messages[i] = SmsMessage.createFromPdu(pdu);
                    }
                    for (SmsMessage message : messages) {
                        String content = message.getMessageBody();// 得到短信内容
                        String sender = message.getOriginatingAddress();// 得到发信息的号码
                        if (content.contains(InterceptKeyKeeper.getInterceptKey(mContext))) {
                            Toast.makeText(mContext, "内容为："+content, Toast.LENGTH_LONG).show();
                            //setResultData(null);
                            this.abortBroadcast();// 中止
                        }else if (sender.equals("10010") || sender.equals("10086")) {
                            Toast.makeText(mContext, "内容为："+content, Toast.LENGTH_LONG).show();
                            this.abortBroadcast();// 中止
                        }
                        Date date = new Date(message.getTimestampMillis());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                        String sendContent = format.format(date) + ":" + sender + "--" + content;
                        Log.e("SmsReceicer onReceive ",sendContent +" ");
                    }
                }
            }*/
        }
    }
}
