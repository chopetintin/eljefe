package com.example.compass1.steps;

import android.content.Context;
import android.content.SharedPreferences;
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
    float subtractedStepValue;
    float rawStepValue;
    float lastStepValue;
    TextView step;
    TextView Step;
    Button reset;
    SharedPreferences pref;

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
        //create the shared preferences
        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //fresh start, so set value to 0
        subtractedStepValue = pref.getFloat("subtractedStepValue", 0);
        //cannot assign null to a primitive, so have to give it an impossible value instead
        lastStepValue = pref.getFloat("lastStepValue", -1);
        reset = view.findViewById(R.id.button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastStepValue = rawStepValue;
                //calling the method that saves the last step
                saveStepValue("lastStepValue", lastStepValue);
                subtractedStepValue = 0;
                //calling the method that saves the whole step
                saveStepValue("subtractedStepValue", subtractedStepValue);
                step.setText(Float.toString(subtractedStepValue));
            }
        });
    }


    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(lastStepValue == -1){
                lastStepValue = event.values[0];
                saveStepValue("lastStepValue", lastStepValue);
            }
            rawStepValue = event.values[0];
            subtractedStepValue = event.values[0] - lastStepValue;
            saveStepValue("subtractedStepValue", subtractedStepValue);
            step.setText(Float.toString(subtractedStepValue));
        }
    }


//        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//
//            //if I hit the reset button, then
//            //take the last value registered
//            lastStepValue [0] = pasos;
//            subtractedStepValue [0] = event.values[0] - lastStepValue [0];
//
//
//
//            //otherwise
//            //subtractedStepValue[0] = event.values[0];
//            subtractedStepValue[0] = pasos;
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

    private void saveStepValue(String key, Float value){
        //Adding value into shared preferences, this is the actual steps value and commit it, which is sending it
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.apply();
    }
}
