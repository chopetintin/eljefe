package com.example.compass1.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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


//Extend the Fragment class and implement the SensorEventListener
public class CompassFragment extends Fragment implements SensorEventListener {

    //Declare variables and primitives to be used in the Fragment
    ImageView compassImage;
    TextView directionText;
    private SensorManager sm;
    private Sensor aSensor;
    private Sensor mSensor;
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];

    //call to have the fragment instantiate the user interface view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compass, container, false);

    }

    //Once the view is created, we can instantiate the parameters of the Fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Getting an instance of SensorManager for accessing sensors
        sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        //Determining the default sensor type: magnetometer and accelerometer
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Initialize the text and image displays
        directionText = view.findViewById(R.id.tvDirection);
        compassImage = view.findViewById(R.id.imageView2);

        //Call calculate orientation method
        calculateOrientation();
    }

    // Definining the calculateOrientation method.
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];

        //Processing the data read from the sensors
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        //Convert from radians to degrees
        float degree = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);


        //Setting the textview "directionText" to the corresponding
        //angle value.
        if (degree >= -5 && degree < 5) {
            directionText.setText("North");
        } else if (degree >= 5 && degree < 40) {
            directionText.setText("North North East");
        } else if (degree >= 40 && degree < 50) {
            directionText.setText("North East");
        } else if (degree >= 50 && degree < 85) {
            directionText.setText("East North East");
        } else if (degree >= 85 && degree < 95) {
            directionText.setText("East");
        } else if (degree >= 95 && degree < 130) {
            directionText.setText("East South East");
        } else if (degree >= 130 && degree < 140) {
            directionText.setText("South East");
        } else if (degree >= 140 && degree < 175) {
            directionText.setText("South South East");
        } else if ((degree >= 175 && degree <= 180) || (degree >= -180 && degree < -175)) {
            directionText.setText("South");
        } else if (degree >= -175 && degree < -140) {
            directionText.setText("South South West");
        } else if (degree >= -140 && degree < -130) {
            directionText.setText("South West");
        } else if (degree >= -130 && degree < -95) {
            directionText.setText("West South West");
        } else if (degree >= -95 && degree < -85) {
            directionText.setText("West");
        } else if (degree >= -85 && degree < -50) {
            directionText.setText("West North West");
        } else if (degree >= -50 && degree < -40) {
            directionText.setText("North West");
        } else if (degree >= -40 && degree < -50) {
            directionText.setText("North North West");
        }

        //RotateAnimation function applied to the compass image.
        //Degree offset is -degree and we're moving towards -degree,
        //since we want a rotation opposite to that of the angle. e.g.: If the user
        //rotates 3 degrees to the right, the image has to rotate 3 degrees to the left
        //to remain accurate. Rotation is done relative to itself.
        RotateAnimation rotateCompass = new RotateAnimation(
                -degree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateCompass.setDuration(500);
        rotateCompass.setFillAfter(true);
        compassImage.startAnimation(rotateCompass);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        //Register the content from the sensors and apply a low pass filter to smooth the signals
        //and filter out unwanted noise.
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

    }

    //No sensor accuracy changed, method can be left blank.
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //Enable sensors when we return to the Fragment.
    @Override
    public void onResume() {
        super.onResume();
        //enable sensor manager when activity returns
        sm.registerListener(this, mSensor, sm.SENSOR_DELAY_GAME);
        sm.registerListener(this, aSensor, sm.SENSOR_DELAY_GAME);


    }

    //Disable sensors when we leave fragment, in order to not drain battery.
    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);

    }
}

