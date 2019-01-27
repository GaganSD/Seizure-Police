package com.hackic.seizure;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.provider.Settings.Secure;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

  @Override
  public void onNewToken(String s) {
    super.onNewToken(s);
    String android_id = Secure.getString(getContentResolver(),
            Secure.ANDROID_ID);
    new HttpHandler(this).sendPost(s, android_id);
  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    Log.d("MessageReceived: ", remoteMessage.getNotification().getTitle());
    handleNow(remoteMessage);
  }

  private void handleNow(RemoteMessage remoteMessage) {
    Log.d("handlingMessage", "Short lived task is done.");
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "123")
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(remoteMessage.getNotification().getTitle())
            .setContentText(remoteMessage.getNotification().getBody())
            .setContentIntent(MainActivity.pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    NotificationManagerCompat manager = NotificationManagerCompat.from(this);
    manager.notify(123, mBuilder.build());
  }
}
