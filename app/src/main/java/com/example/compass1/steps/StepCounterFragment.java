package com.example.compass1.steps;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.compass1.R;

public class StepCounterFragment extends Fragment implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mstep;
    float[] stepValues = new float[3];
    TextView step;
    TextView Step;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mstep = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        step = view.findViewById(R.id.test2);
        Step = view.findViewById(R.id.steptext);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        float pasos = event.values[0];


        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            stepValues[0] = event.values[0];
        }

        step.setText(Float.toString(pasos));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mstep, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
