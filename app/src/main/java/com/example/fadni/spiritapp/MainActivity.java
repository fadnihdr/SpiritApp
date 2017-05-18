package com.example.fadni.spiritapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    public Vibrator vibrating;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float initialX, initialY, initialZ;
    private float vibrateThreshold = 0;
    private TextView currentX, currentY, currentZ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
            //accelarometer is set. does our device have accelerometer
            //we initialize our sensorManager in order to bind the sensor to listen to the accelerometer events.
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() /2;
        }else{
            //in case device doesnt have accelerometer
        }
        vibrating = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // clean current values
        ResetValues();
        // display the current x,y,z accelerometer values
        displayCurrentCoordinates();

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(initialX - event.values[0]);
        deltaY = Math.abs(initialY - event.values[1]);
        deltaZ = Math.abs(initialZ - event.values[2]);

        // device will calculate the slightest difference as well, so need to set the low threshold
        // if the change is below 2, it is just plain noise(not to be considered)
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
        if (deltaZ < 2)
            deltaZ = 0;

        //threshold can be set as max/2 or max/3
        if ((deltaZ > vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
            vibrating.vibrate(40);
        }
    }

    public void ResetValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentCoordinates() {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
