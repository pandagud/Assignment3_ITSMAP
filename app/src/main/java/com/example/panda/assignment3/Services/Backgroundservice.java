package com.example.panda.assignment3.Services;

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

import com.example.panda.assignment3.DataBases.Database;
import com.example.panda.assignment3.Globals.Global;

public class Backgroundservice extends Service implements SensorEventListener{

    // Inspiration from https://github.com/bagilevi/android-pedometer
    public static final String BROADCAST_BACKGROUND_SERVICE_RESULT = "com.example.panda.Assignment3_ITSMAP.Backgroundservice_RESULT";
    private final IBinder backGroundBinder = new BackGroundBind();

    SensorManager sManager;
    Sensor sStepCounter;
    Sensor sStepDetector;
    Intent bgIntent;

    int curStepDetector;

    int curStepCounter;
    int newStepCounter;

    boolean serviceStopped;

    private final Handler bghandler = new Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Inspiration from https://github.com/bagilevi/android-pedometer
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sStepCounter = sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sStepDetector = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sManager.registerListener(this, sStepCounter, 0);
        sManager.registerListener(this, sStepDetector, 0);
        // Inspiration from https://github.com/bagilevi/android-pedometer
        initializeValues();

        bghandler.removeCallbacks(BroadCastData);

        bghandler.post(BroadCastData);

        return START_STICKY;
    }
    public void initializeValues()
    {
        serviceStopped = false;

    }
    @Override
    public void onCreate() {
        super.onCreate();
        bgIntent = new Intent(BROADCAST_BACKGROUND_SERVICE_RESULT);

    }

    private Runnable BroadCastData = new Runnable() {
        public void run() {
            if (!serviceStopped) {
                broadcastStepCounterValue();

                bghandler.postDelayed(this, Global.TIMEOUT_FORBACKGROUNDSERVICE);
            }
        }
    };
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int countSteps = (int) event.values[0];



            if (curStepCounter == 0) {
                curStepCounter = (int) event.values[0];
            }
            newStepCounter = countSteps - curStepCounter;
        }


        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            int detectSteps = (int) event.values[0];
            curStepDetector += detectSteps;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void broadcastStepCounterValue() {
        Log.d(Global.BACKGROUNDSERVICE, "Broadcasting step counter values");
        // Inspiration from https://github.com/bagilevi/android-pedometer
        bgIntent.putExtra(Global.COUNTEDSTEPSINT, newStepCounter);
        bgIntent.putExtra(Global.COUNTEDSTEPS, String.valueOf(newStepCounter));

        bgIntent.putExtra(Global.DETECTEDSTEPSINT, curStepDetector);
        bgIntent.putExtra(Global.DETECTEDSTEPS, String.valueOf(curStepDetector));
        // Inspiration from https://github.com/bagilevi/android-pedometer
        sendBroadcast(bgIntent);
    }
    public class BackGroundBind extends Binder {
        public Backgroundservice getService() {

            return Backgroundservice.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return backGroundBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(Global.BACKGROUNDSERVICE, "Stop");


    }


}