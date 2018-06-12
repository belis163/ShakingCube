package com.example.belis.kostka;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    TextView mTextViewX;
    TextView mTextViewY;
    TextView mTextViewZ;
    private CheckBox mCheckBox;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float lastX;
    private float lastY;
    private float lastZ;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewX = findViewById(R.id.viewX);
        mTextViewY = findViewById(R.id.viewY);
        mTextViewZ = findViewById(R.id.viewZ);

        mCheckBox = findViewById(R.id.shakeActivate);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mCheckBox.isChecked()){
                    mCheckBox.setText("Disabled");
                }
                else {
                    mCheckBox.setText("Enabled");
                }
            }
        });

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        }
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastUpdate) > 100) {
            long diffTime = (currentTime - lastUpdate);
            lastUpdate = currentTime;

            float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 100000;


            if((currentTime - diffTime) == 2000){
                mCheckBox.setChecked(true);
            }

            if (speed > SHAKE_THRESHOLD && !mCheckBox.isChecked()) {
                changeDice();
            }

            lastX = x;
            lastY = y;
            lastZ = z;

            mTextViewX.setText((String.valueOf(lastX)));
            mTextViewY.setText((String.valueOf(lastY)));
            mTextViewZ.setText((String.valueOf(lastZ)));
        }

    }

    private void changeDice() {
        ImageView imageView = (ImageView) findViewById(R.id.imageViewDice);
        imageView.setImageResource(R.drawable.six);

        Animation rotationDice = AnimationUtils.loadAnimation(this, R.anim.move_down_ball_first);
        Random random = new Random();

        int numberOfDice = random.nextInt(6) + 1;

        if (numberOfDice == 1) {
            imageView.setImageResource(R.drawable.one);
            imageView.clearAnimation();
            imageView.startAnimation(rotationDice);
        } else if (numberOfDice == 2) {
            imageView.setImageResource(R.drawable.two);
            imageView.clearAnimation();
            imageView.startAnimation(rotationDice);
        } else if (numberOfDice == 4) {
            imageView.setImageResource(R.drawable.three);
            imageView.clearAnimation();
            imageView.startAnimation(rotationDice);
        } else if (numberOfDice == 5) {
            imageView.setImageResource(R.drawable.five);
            imageView.clearAnimation();
            imageView.startAnimation(rotationDice);
        } else if (numberOfDice == 6) {
            imageView.setImageResource(R.drawable.six);
            imageView.clearAnimation();
            imageView.startAnimation(rotationDice);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}

