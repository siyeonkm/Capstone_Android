package com.example.capstoneblackbox;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import static com.example.capstoneblackbox.RecordActivity.impact;
import static io.realm.Realm.getApplicationContext;

class ImpactListener implements SensorEventListener {
    private long mShakeTime;
    private static final int SHAKE_SKIP_TIME = 500;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.6F;
    private int mShakeCount = 0;
     Context context = getApplicationContext();

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
                Toast.makeText(context, "충격이 발생했습니다", Toast.LENGTH_SHORT).show();
                //Log.d("shake","shake 발생"+ mShakeCount);
                impact = 1;
                //recordImpact();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}