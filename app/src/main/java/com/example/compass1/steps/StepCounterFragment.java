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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.compass1.R;

public class StepCounterFragment extends Fragment implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mstep;
    float[] stepValues = new float[3];
    float[] lastValue = new float[3];
    TextView step;
    TextView Step;
    Button reset;

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
        reset = view.findViewById(R.id.button);
    }


    @Override
    public void onSensorChanged(final SensorEvent event) {

        final float pasos = event.values[0];

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastValue[0] = pasos;
                    stepValues[0] = event.values[0] - lastValue[0];
                }
            });
            stepValues[0] = event.values[0];
        }
        step.setText(Float.toString(pasos));

    }


//        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//
//            //if I hit the reset button, then
//            //take the last value registered
//            lastValue [0] = pasos;
//            stepValues [0] = event.values[0] - lastValue [0];
//
//
//
//            //otherwise
//            //stepValues[0] = event.values[0];
//            stepValues[0] = pasos;
//
//        }
//
//        step.setText(Float.toString(pasos));
//}


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
