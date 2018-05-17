package com.example.panda.assignment3.Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class Backgroundservice extends Service implements SensorEventListener{

    // Inspiration from https://github.com/bagilevi/android-pedometer
    public static final String BROADCAST_BACKGROUND_SERVICE_RESULT = "com.example.panda.Assignment3_ITSMAP.Backgroundservice_RESULT";
    private final IBinder backGroundBinder = new BackGroundBind();

    SensorManager sensorManager;
    Sensor stepCounterSensor;
    Sensor stepDetectorSensor;
    Intent backgroundintent;
    NotificationManager notificationManager;

    int currentStepsDetected;

    int stepCounter;
    int newStepCounter;

    boolean serviceStopped;

    private final Handler handler = new Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepCounterSensor, 0);
        sensorManager.registerListener(this, stepDetectorSensor, 0);

        //currentStepCount = 0;
        currentStepsDetected = 0;
        stepCounter = 0;
        newStepCounter = 0;

        serviceStopped = false;


        handler.removeCallbacks(updateBroadcastData);

        handler.post(updateBroadcastData);

        return START_STICKY;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        backgroundintent = new Intent(BROADCAST_BACKGROUND_SERVICE_RESULT);

    }

    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!serviceStopped) {
                broadcastSensorValue();

                handler.postDelayed(this, 5000);
            }
        }
    };
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int countSteps = (int) event.values[0];



            if (stepCounter == 0) {
                stepCounter = (int) event.values[0];
            }
            newStepCounter = countSteps - stepCounter;
        }


        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            int detectSteps = (int) event.values[0];
            currentStepsDetected += detectSteps;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void broadcastSensorValue() {
        Log.d("test", "Data to Activity");

        backgroundintent.putExtra("Counted_Step_Int", newStepCounter);
        backgroundintent.putExtra("Counted_Step", String.valueOf(newStepCounter));

        backgroundintent.putExtra("Detected_Step_Int", currentStepsDetected);
        backgroundintent.putExtra("Detected_Step", String.valueOf(currentStepsDetected));

        sendBroadcast(backgroundintent);
    }
    public class BackGroundBind extends Binder {
        public Backgroundservice getService() {

            return Backgroundservice.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Service", "Stop");

        serviceStopped = true;

    }


}
