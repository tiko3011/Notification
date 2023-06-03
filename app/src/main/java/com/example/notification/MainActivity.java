package com.example.notification;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnNotification;
    public static final String CHANNEL_ID = "DigiTIme_ID";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            // Request permission
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE);
        }


        btnNotification = findViewById(R.id.btn_sent_notification);
        btnNotification.setOnClickListener(v -> {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);

            builder.setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("This is Title")
                    .setContentText("You got Notification!")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("data", "Some data");

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    0, intent, PendingIntent.FLAG_MUTABLE);

            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(CHANNEL_ID);

            if (notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;

                notificationChannel = new NotificationChannel(CHANNEL_ID, "Description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationManager.notify(0, builder.build());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            // Check if notification permission was granted or not
            if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                Log.i("PermissionInfo", "Notifications ENABLED");
            } else {
                Log.i("PermissionInfo", "Notifications DISABLED");
            }
        }
    }

}