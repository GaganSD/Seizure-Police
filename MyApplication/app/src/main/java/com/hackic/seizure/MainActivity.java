package com.hackic.seizure;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Timer;
import java.util.TimerTask;

import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener{

    private final Runnable updater =  new Runnable() {
        int startingTime = 15;
        @Override
        public void run() {

            if (startingTime <= 0) {
                MakePhoneCall();
            }
            else {
                TextView t = (TextView) findViewById(R.id.timer);
                startingTime -= 1;
                t.setText("" + startingTime);
            }

        }
    };

    private SensorManager accSensorManager;
    private LocationManager locationManager;
    private Sensor accSensor;

    private int windowSize = 5000;
    public Queue<Double> accBufferx = new LinkedList<>();
    public Queue<Double> accBuffery = new LinkedList<>();
    public Queue<Double> accBufferz = new LinkedList<>();

    public static Double latitude, longitude;

    private final Handler handler = new Handler();

    public static PendingIntent pendingIntent = null;
    private int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePhoneCall();
            }
        });

                Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        for (int i = 1; i < 20; i++) {
            handler.postDelayed(updater,i*1000);
        }

        init();

        accSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Button buttonNoPermission = (Button) findViewById(R.id.buttonNoPermission);
//            buttonNoPermission.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                    System.exit(0);
//                }
//            });
//            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        accSensor = accSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        gyrSensor = gyrSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        accSensorManager.registerListener(this, accSensor, 10000);
//        gyrSensorManager.registerListener(this, gyrSensor, 10000);
    }

    private void init(){
      Intent intent = new Intent(this, MainActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      this.pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
      createNotificationChannel();
    }

    private void createNotificationChannel() {
      CharSequence name = "seizure";
      String description = "no description";
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel("123", name, importance);
      channel.setDescription(description);
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }

    public void MakePhoneCall(){
        Intent callIntent =new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:000"));
        startActivity(callIntent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        accBufferx.add((double) event.values[0]);
        if(accBufferx.size() > windowSize){
            accBufferx.remove();
        }

        accBuffery.add((double) event.values[1]);
        if(accBufferx.size() > windowSize){
            accBufferx.remove();
        }

        accBufferz.add((double) event.values[2]);
        if(accBufferx.size() > windowSize){
            accBufferx.remove();
        }

        Log.w("accBufferx", String.valueOf(accBufferx));
        Log.w("accBuffery", String.valueOf(accBuffery));
        Log.w("accBufferz", String.valueOf(accBufferz));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.w("latitude:", String.valueOf(latitude));
        Log.w("longitude:", String.valueOf(longitude));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
