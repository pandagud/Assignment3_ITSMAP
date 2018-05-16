package com.example.panda.assignment3.Services;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepCounter implements SensorEventListener{
    SensorManager sensorManager;

    private void startStepCounter(Context c)
    {
        sensorManager = (SensorManager) c.getSystemService(c.SENSOR_SERVICE);
        Sensor countersensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
