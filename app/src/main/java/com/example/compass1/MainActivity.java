package com.example.compass1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.compass1.battery.BatteryFragment;
import com.example.compass1.compass.CompassFragment;
import com.example.compass1.steps.StepCounterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    //Definition of variables and primitives to be used
    //in the main activity file.
    //Note: We have devidedthe project into Fragments,
    //except for the camera activity which we implement
    //here. The compass code is implemented both here
    //and in its fragment. This is because passing the
    //directions information from the fragment to the
    //main activity and attach it to the file name was
    //difficult. Notice that despite having the compass
    //code in here, there is no UI display. This is all
    //done in the compass Fragment.
    String ReadingsDisplay;
    private SensorManager sm;
    private Sensor aSensor;
    private Sensor mSensor;
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Add the following two lines to remove camera bug
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //Call the initialization methodd, where all the pertinent initializations are made.
        initialization();

        //call calculate orientation method
        calculateOrientation();

    }

    private void initialization() {

        //Getting an instance of SensorManager for accessing sensors
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Determining the default sensor type: magnetometer and accelerometer
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Initializing the bottom navigation bar view and creating
        //the listener for the different cases. Like this, on clicking
        //each item we can replace the previous fragment with the new one.
        // Notice that we're calling for each case a fragment, except in
        // the last case (picture icon) where we're simply calling the takePhoto method,
        // which will lead directly to the camera. A toast is included, which allows the
        //user to see that he has indeed selected the desired app functionality.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.bottom_nav_bar_fitness_item:
                        Toast.makeText(MainActivity.this, "Steps", Toast.LENGTH_SHORT).show();
                        fragment = new StepCounterFragment();
                        fragmentTransaction.replace(R.id.frame_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.bottom_nav_bar_compass_item:
                        Toast.makeText(MainActivity.this, "Compass", Toast.LENGTH_SHORT).show();
                        fragment = new CompassFragment();
                        fragmentTransaction.replace(R.id.frame_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.bottom_nav_bar_battery_item:
                        Toast.makeText(MainActivity.this, "Battery", Toast.LENGTH_SHORT).show();
                        fragment = new BatteryFragment();
                        fragmentTransaction.replace(R.id.frame_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.bottom_nav_bar_picture_item:
                        takePhoto();
                        break;
                }
                return true;
            }
        });


    }

    // Definining the calculateOrientation method.

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];

        //Processing the data read from the sensors
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        //Convert data from radians to degrees
        float degree = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);

        //Setting the string "ReadingsDisplay" to the corresponding
        //angle value. This string will then be attached to the file
        //name, so that it contains the direction of the compass.
        if (degree >= -5 && degree < 5) {
            ReadingsDisplay = "North";
        } else if (degree >= 5 && degree < 40) {
            ReadingsDisplay = "North North East";
        } else if (degree >= 40 && degree < 50) {
            ReadingsDisplay = "North East";
        } else if (degree >= 50 && degree < 85) {
            ReadingsDisplay = "East North East";
        } else if (degree >= 85 && degree < 95) {
            ReadingsDisplay = "East";
        } else if (degree >= 95 && degree < 130) {
            ReadingsDisplay = "East South East";
        } else if (degree >= 130 && degree < 140) {
            ReadingsDisplay = "South East";
        } else if (degree >= 140 && degree < 175) {
            ReadingsDisplay = "South South East";
        } else if ((degree >= 175 && degree <= 180) || (degree >= -180 && degree < -175)) {
            ReadingsDisplay = "South";
        } else if (degree >= -175 && degree < -140) {
            ReadingsDisplay = "South South West";
        } else if (degree >= -140 && degree < -130) {
            ReadingsDisplay = "South West";
        } else if (degree >= -130 && degree < -95) {
            ReadingsDisplay = "West South West";
        } else if (degree >= -95 && degree < -85) {
            ReadingsDisplay = "West";
        } else if (degree >= -85 && degree < -50) {
            ReadingsDisplay = "West North West";
        } else if (degree >= -50 && degree < -40) {
            ReadingsDisplay = "North West";
        } else if (degree >= -40 && degree < -50) {
            ReadingsDisplay = "North North East";
        }

    }

    //Method to display the camera.

    public void takePhoto() {

        //intent the existing camera application and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //get the uri of a file to save the image
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        //specifying the path and file name for the received image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        //start to intent the image capture activity
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    //Create the file uri
    private Uri getOutputMediaFileUri(int type) {
        //uri is defined by media File
        return Uri.fromFile(getOutputMediaFile(type));
    }

    //Create a file for saving image
    @SuppressLint("SimpleDateFormat")
    private File getOutputMediaFile(int type) {

        //Create the storage directory. If SD card exists, create a directory of standard, shared
        //and recommended location for saving pictures. If not, create a directory in the device
        final File mediaStorageDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "HikerApp");
        } else {
            mediaStorageDir = new File("/storage/sdcard0/HikerApp");
        }

        //creates the directory named by this file, creating missing parent directories if necessary.
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("HikerApp", "failed to create directory");
                return null;
            }
        }

        //create a media file name. NOTE: We inclde in the name of the file "ReadingsDisplay", so attach the direction of the compass
        //to the picture taken.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ReadingsDisplay + ".jpg");

        } else {
            return null;
        }
        return mediaFile;
    }

    //present the state of saved image
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Image captured and saved to fileUri specified in the intent
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Image successfully saved", Toast.LENGTH_SHORT).show();
            }
            //User cancelled the image capture
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show();
            }
            //Image capture failed, advice user
            else {
                Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //Unregister sensor manager when activity is not visible
        sm.unregisterListener(this);
        Log.d("PAUSE", "onPause is being executed");

    }

    @Override
    protected void onResume() {
        super.onResume();
        //enable sensor manager when activity returns
        sm.registerListener(this, mSensor, sm.SENSOR_DELAY_GAME);
        sm.registerListener(this, aSensor, sm.SENSOR_DELAY_GAME);
        Log.d("RESUME", "onResume is being executed");


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //Register the content from the sensors and apply a low pass filter to smooth the signals
        //and filter out unwanted noise. Use the variable alpha to achieve the wanted smoothness.
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

        //Call calculateOrientation so that the orientation is recalculated with every new sensor event
        calculateOrientation();


    }

    //No sensor accuracy changed, method can be left blank.
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
