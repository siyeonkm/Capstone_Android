package com.example.capstoneblackbox;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static com.example.capstoneblackbox.RecordActivity.recordImpact;

class ImpactListener implements SensorEventListener {
    private long mShakeTime;
    private static final int SHAKE_SKIP_TIME = 500;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private int mShakeCount = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            float gravityX = axisX / SensorManager.GRAVITY_EARTH;
            float gravityY = axisX / SensorManager.GRAVITY_EARTH;
            float gravityZ = axisX / SensorManager.GRAVITY_EARTH;

            Float f = gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ;
            double squaredD = Math.sqrt(f.doubleValue());
            float gFore = (float) squaredD;

            if(gFore > SHAKE_THRESHOLD_GRAVITY){
                long currentTime = System.currentTimeMillis();
                if(mShakeTime + SHAKE_SKIP_TIME > currentTime)
                    return;
                mShakeTime = currentTime;
                mShakeCount++;
                Log.d("shake","shake 발생"+ mShakeCount);
                recordImpact();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}