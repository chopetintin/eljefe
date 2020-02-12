package com.example.compass1.compass;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.compass1.R;

import java.util.Arrays;

public class CompassFragment extends Fragment implements SensorEventListener {

    //Animation rotateAnimation;
    ImageView imageView2;
    TextView grados;
    TextView tv;
    TextView TV;
    private SensorManager sm;
    //private float x, y, z;
    //private double h;
    // we need two sensors in this application
    private Sensor aSensor;
    private Sensor mSensor;

    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compass_test, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        grados = view.findViewById(R.id.degreeview);
        tv = view.findViewById(R.id.tvDirection);
        TV = view.findViewById(R.id.TV);
        imageView2 = view.findViewById(R.id.imageView2);
        calculateOrientation();
    }

    private void calculateOrientation() {
        //TODO Auto-generated method stub
        float[] values = new float[3];
        float[] R = new float[9];

        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        float degree = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);
        Log.d("CREATIONFragment", "calculateOrientation is being executed");
        grados.setText(Float.toString(degree));

        if (degree >= -5 && degree < 5) {
            tv.setText("North");
        } else if (degree >= 5 && degree < 40) {
            tv.setText("North North East");
        } else if (degree >= 40 && degree < 50) {
            tv.setText("North East");
        } else if (degree >= 50 && degree < 85) {
            tv.setText("East North East");
        } else if (degree >= 85 && degree < 95) {
            tv.setText("East");
        } else if (degree >= 95 && degree < 130) {
            tv.setText("East South East");
        } else if (degree >= 130 && degree < 140) {
            tv.setText("South East");
        } else if (degree >= 140 && degree < 175) {
            tv.setText("South South East");
        } else if ((degree >= 175 && degree <= 180) || (degree >= -180 && degree < -175)) {
            tv.setText("South");
        } else if (degree >= -175 && degree < -140) {
            tv.setText("South South West");
        } else if (degree >= -140 && degree < -130) {
            tv.setText("South West");
        } else if (degree >= -130 && degree < -95) {
            tv.setText("West South West");
        } else if (degree >= -95 && degree < -85) {
            tv.setText("West");
        } else if (degree >= -85 && degree < -50) {
            tv.setText("West North West");
        } else if (degree >= -50 && degree < -40) {
            tv.setText("North West");
        } else if (degree >= -40 && degree < -50) {
            tv.setText("North North West");
        }

        RotateAnimation ra = new RotateAnimation(
                -degree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(500);
        ra.setFillAfter(true);
        imageView2.startAnimation(ra);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        //read sensor value from SensorEvent
        Log.d("V", "ValuesPre" + Arrays.toString(event.values));

        //calculate the total magnetic field
        final float alpha = 0.97f;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            //low pass filter
            magneticFieldValues[0] = alpha * magneticFieldValues[0] + (1 - alpha) * event.values[0];
            magneticFieldValues[1] = alpha * magneticFieldValues[1] + (1 - alpha) * event.values[1];
            magneticFieldValues[2] = alpha * magneticFieldValues[2] + (1 - alpha) * event.values[2];


        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //low pass filter
            accelerometerValues[0] = alpha * accelerometerValues[0] + (1 - alpha) * event.values[0];
            accelerometerValues[1] = alpha * accelerometerValues[1] + (1 - alpha) * event.values[1];
            accelerometerValues[2] = alpha * accelerometerValues[2] + (1 - alpha) * event.values[2];
        }


        calculateOrientation();

        Log.d("FragmentValues", "Values" + Arrays.toString(event.values));
        Log.d("FragmentValues2", "Magnetic:" + Arrays.toString(magneticFieldValues));
        Log.d("FragmentValues3", "Accelerometer:" + Arrays.toString(accelerometerValues));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //enable sensor manager when activity returns
        sm.registerListener(this, mSensor, sm.SENSOR_DELAY_GAME);
        sm.registerListener(this, aSensor, sm.SENSOR_DELAY_GAME);
        Log.d("RESUME", "onResume is being executed");


    }

    @Override
    public void onPause() {
        super.onPause();
        //Unregister sensor manager when activity is not visible
        sm.unregisterListener(this);
        Log.d("PAUSE", "onPause is being executed");

    }
}

