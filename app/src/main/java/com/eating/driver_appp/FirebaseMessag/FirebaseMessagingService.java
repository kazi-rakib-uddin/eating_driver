package com.eating.driver_appp.FirebaseMessag;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.eating.driver_appp.MainActivity;
import com.eating.driver_appp.R;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    NotificationManager notificationManager;
    MediaPlayer mp;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String img_url = remoteMessage.getData().get("img_url");
        showNotification(remoteMessage.getData().get("message"));
        //showNotification(remoteMessage.getData().get("message"));
    }
    private void showNotification(String titel) {
        Uri sound = Uri.parse("https://notificationsounds.com/notification-sounds/deduction-588");
        final long[] VIBRATE_PATTERN = {0, 400, 600, 800, 1000};
        final int NOTIFICATION_COLOR = getResources().getColor(R.color.colorPrimary);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        }else {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this,"MyNotification")
                .setAutoCancel(true)
                .setContentTitle(titel)
                .setColor(NOTIFICATION_COLOR)
                //.setContentText(message)
                .setSmallIcon(R.drawable.logo)
                .setSound(sound)
                .setLights(Notification.DEFAULT_LIGHTS, 1000, 1000)
                .setVibrate(VIBRATE_PATTERN)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);


       /* ImageRequest imageRequest = new ImageRequest(img_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0,builder.build());
            }
        }, 0, 0, null,Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        MySingleton.getmInstance(this).addToRequestQue(imageRequest);*/



        MediaPlayer mediaPlayer=MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        //MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.notification23sec);
        mediaPlayer.start();

        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(this);
        managerCompat.notify(0,builder.build());
    }

}
