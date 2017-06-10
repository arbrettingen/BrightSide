package com.soapbox.brightside;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Alex on 6/10/2017.
 */

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent mIntent = new Intent(context, MainMenuActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);

        Notification mNotify = new Notification.Builder(context)
                .setContentTitle("BrightSide")
                .setContentText(intent.getStringExtra("Message Text"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(mPendingIntent)
                .setSound(sound)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mNotify);


    }
}
