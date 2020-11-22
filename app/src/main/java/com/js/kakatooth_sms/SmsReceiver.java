package com.js.kakatooth_sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Build.VERSION;
import android.net.Uri;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class SmsReceiver extends BroadcastReceiver {
    private  static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG="SmsReceiver";
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm", Locale.getDefault());



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), SMS_RECEIVED)){
            Log.d(TAG,"onReceiver() 호출");

            Bundle bundle = intent.getExtras();
            assert bundle != null;
            SmsMessage[] messages =pareseSmsMessage(bundle);

            if(messages.length>0){
                String sender = messages[0].getOriginatingAddress();
                String contents = messages[0].getMessageBody().toString();

                Date receivedDate = new Date(messages[0].getTimestampMillis());

                Log.d(TAG,"Sender :"+sender);
                Log.d(TAG, "contents :"+contents);
                Log.d(TAG,"receivedDate:"+receivedDate);

                sendToAcitivity(context,sender,contents,receivedDate);
            }
        }

    }

    private void sendToAcitivity(Context context,String sender, String contents,Date receivedDate){
        Intent intent=new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sender",sender);
        intent.putExtra("contents",contents);
        intent.putExtra("receivedDate", format.format(receivedDate));
        intent.putExtra("class","SMS");
        context.startActivity(intent);

    }

    private SmsMessage[]pareseSmsMessage(Bundle bundle){
        Object[] objs = (Object[])bundle.get("pdus");
        assert objs != null;
        SmsMessage[] messages=new SmsMessage[objs.length];

        for(int i =0;i<objs.length;i++){
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                String format = bundle.getString("format");
                messages[i] =SmsMessage.createFromPdu((byte[])objs[i],format);
            }
            else{
                messages[i]=SmsMessage.createFromPdu((byte[])objs[i]);
            }
        }
        return messages;
    }
}
