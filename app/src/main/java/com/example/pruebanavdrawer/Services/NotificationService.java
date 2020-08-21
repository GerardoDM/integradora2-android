package com.example.pruebanavdrawer.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pruebanavdrawer.R;

public abstract class NotificationService extends Service {

    private static  final String  CHANNEL_ID = "NOTIFICATION";
    private static  final int NOTIFICATION_ID = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID);
            builder.setContentTitle("Notificacion Puerta")
                    .setContentText("Se abrió la compuerta de croquetas")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSmallIcon(R.drawable.dog);


            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());



            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence name = "Notification";
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager)  getActivity().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);
            }



        return super.onStartCommand(intent, flags, startId);
    }

    protected abstract Context getActivity();
}
