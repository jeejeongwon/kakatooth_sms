package com.js.kakatooth_sms;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class MyNotificationListener extends NotificationListenerService {
public final static String TAG = "MyNotificationListener";
    Context context;
    @Override
    public  void onCreate(){
        super.onCreate();
        context = getApplicationContext();

        NLServiceReceiver nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("command").equals("clearall")) {
                Toast.makeText(context, "clearall", Toast.LENGTH_SHORT).show();
                MyNotificationListener.this.cancelAllNotifications();

            } else if (intent.getStringExtra("command").equals("list")) {
                Toast.makeText(context, "list", Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent("NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event", "=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : MyNotificationListener.this.getActiveNotifications()) {
                    Intent i2 = new Intent("NOTIFICATION_LISTENER_EXAMPLE");
                    i2.putExtra("notification_event", i + " " + sbn.getPackageName() + "\n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new Intent("NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event", "===== Notification List ====");
                sendBroadcast(i3);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        super.onNotificationPosted(sbn);

        if(sbn == null) return;
        String packName = sbn.getPackageName();

        Notification mNotification = sbn.getNotification();
        Bundle extras = mNotification.extras;
        Intent intent= new Intent("android.service.notification.NotificationListenerService");

        if (packName.equalsIgnoreCase("com.kakao.talk")) {
            String head = extras.getString(Notification.EXTRA_TITLE);

            String body = extras.getString(Notification.EXTRA_TEXT);
            if (!(head == null || body == null )) {

                intent.putExtra("head", head);
                intent.putExtra("body", body);

                sendBroadcast(intent);

            }

        }



    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("MyNotificationListener", "onNotificationRemoved()");
    }

    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

}
