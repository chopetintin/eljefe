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

//Extend the Fragment class
public class StepCounterFragment extends Fragment implements SensorEventListener {

    //Declare the primitives and variables to be used in the Fragment.
    private SensorManager mSensorManager;
    private Sensor mstep;
    float subtractedStepValue;
    float rawStepValue;
    float lastStepValue;
    TextView stepNumberDisplay;
    TextView stepTextDisplay;
    Button reset;
    SharedPreferences pref;

    //call to have the fragment instantiate the user interface view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }

    //Once the view is created, we can instantiate the parameters of the Fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Instantiate the sensor manager
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mstep = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepNumberDisplay = view.findViewById(R.id.test2);
        stepTextDisplay = view.findViewById(R.id.steptext);

        //create the shared preferences
        pref = getActivity().getPreferences(Context.MODE_PRIVATE);

        //subtractedStepValue is the actual step value, so we set it to 0 in initialization
        subtractedStepValue = pref.getFloat("subtractedStepValue", 0);

        //cannot assign null to a primitive, so have to give it an impossible value instead
        lastStepValue = pref.getFloat("lastStepValue", -1);

        //Instantiate the button which we will use to set the step counter back to 0.
        reset = view.findViewById(R.id.button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To reset, we set the lastStepValue to the rawStepValue
                lastStepValue = rawStepValue;

                //calling the method that saves the last stepNumberDisplay
                //and set the actual stepNumberDisplay count to 0. Reset is complete
                saveStepValue("lastStepValue", lastStepValue);
                subtractedStepValue = 0;

                //Register in SharedPreferences the subtracted step value, which is 0
                saveStepValue("subtractedStepValue", subtractedStepValue);
                stepNumberDisplay.setText(Float.toString(subtractedStepValue));
            }
        });
    }


    //In onSensorChanged we used shared preferences to save the values that we need
    //to use when the fragment is destroyed and we set the number display to substractedStepValue,
    //which is the overall counter value (event.values[0] - lastStepValue)
    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            //If there is no lastStepValue saved, then the last step value is the actual last
            //sensor value. We save it in shared preferences.
            if (lastStepValue == -1) {
                lastStepValue = event.values[0];
                saveStepValue("lastStepValue", lastStepValue);
            }

            //The actual sensor values are registered in rawStepValue.
            //To get the actual steps, we must subtract the totality of the sensor values
            //from that of the last step registered.
            rawStepValue = event.values[0];
            subtractedStepValue = event.values[0] - lastStepValue;

            //We then save this value in SharedPreferences in case the user switches to another fragment
            //so that we don't lose it.
            saveStepValue("subtractedStepValue", subtractedStepValue);
            stepNumberDisplay.setText(Float.toString(subtractedStepValue));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //Register the sensor manager
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mstep, SensorManager.SENSOR_DELAY_FASTEST);
    }

    //Do not unregister the sensor manager, since we want the steps to always be counted.
    @Override
    public void onPause() {
        super.onPause();
    }

    //Define the method which we will call to save the values in shared preferences
    private void saveStepValue(String key, Float value) {
        //Adding value into shared preferences, this is the actual steps value and apply
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.apply();
    }
}
