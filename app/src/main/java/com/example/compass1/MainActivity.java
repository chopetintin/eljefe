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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.compass1.battery.BatteryFragment;
import com.example.compass1.compass.CompassFragment;
import com.example.compass1.steps.StepCounterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    String tv;
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
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initialization();
        calculateOrientation();

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
    private void initialization() {
        //TODO Auto-generated method stub
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


    }


    public void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    private Uri getOutputMediaFileUri (int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @SuppressLint("SimpleDateFormat")
    private File getOutputMediaFile(int type) {
        final File mediaStorageDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "HikerApp");
        } else {
            mediaStorageDir = new File("/storage/sdcard0/HikerApp");
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("HikerApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + tv +".jpg");

        } else {
            return null;
        }
        return mediaFile;
    }
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Image successfully saved", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


//    private Uri getOutputMediaFileUri() {
//        if (getOutputMediaFile() != null) {
//            return Uri.fromFile(getOutputMediaFile());
//        } else {
//            return null;
//        }
//    }
//
//    private File getOutputMediaFile() {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + tv + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        try {
//            File image = File.createTempFile(
//                    imageFileName,
//                    ".jpg",
//                    storageDir
//            );
//            currentPhotoPath = image.getAbsolutePath();
//            return image;
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, "Image successfully saved", Toast.LENGTH_SHORT).show();
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    //
//
//    private void initialization() {
//        //TODO Auto-generated method stub
//        //TextView initialization
//        // imageView2 = (ImageView) findViewById(R.id.imageView2);
//        //tv = (TextView) findViewById(R.id.tvDirection);
//        //grados = (TextView) findViewById(R.id.degreeview);
//        //tv.setText("Direction");
//
//        //sensors called when the activity starts
//        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//
//    }

    private void calculateOrientation() {
        //TODO Auto-generated method stub
        float[] values = new float[3];
        float[] R = new float[9];

        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        float degree = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);
        // Log.d("CREATION", "calculateOrientation is being executed");
        //grados.setText(Float.toString(degree));

        if (degree >= -5 && degree < 5) {
            //tv.setText("North");
            tv = "North";
        } else if (degree >= 5 && degree < 40) {
            // tv.setText("North North East");
            tv = "North North East";
        } else if (degree >= 40 && degree < 50) {
            //tv.setText("North East");
            tv = "North East";
        } else if (degree >= 50 && degree < 85) {
            //tv.setText("East North East");
            tv = "East North East";
        } else if (degree >= 85 && degree < 95) {
            //tv.setText("East");
            tv = "East";
        } else if (degree >= 95 && degree < 130) {
            //tv.setText("East South East");
            tv = "East South East";
        } else if (degree >= 130 && degree < 140) {
            //tv.setText("South East");
            tv = "South East";
        } else if (degree >= 140 && degree < 175) {
            //tv.setText("South South East");
            tv = "South South East";
        } else if ((degree >= 175 && degree <= 180) || (degree >= -180 && degree < -175)) {
            //tv.setText("South");
            tv = "South";
        } else if (degree >= -175 && degree < -140) {
            //tv.setText("South South West");
            tv = "South South West";
        } else if (degree >= -140 && degree < -130) {
            //tv.setText("South West");
            tv = "South West";
        } else if (degree >= -130 && degree < -95) {
            //tv.setText("West South West");
            tv = "West South West";
        } else if (degree >= -95 && degree < -85) {
            // tv.setText("West");
            tv = "West";
        } else if (degree >= -85 && degree < -50) {
            //tv.setText("West North West");
            tv = "West North West";
        } else if (degree >= -50 && degree < -40) {
            //tv.setText("North West");
            tv = "North West";
        } else if (degree >= -40 && degree < -50) {
            //tv.setText("North North West");
            tv = "North North East";
        }

        RotateAnimation ra = new RotateAnimation(
                -degree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(500);
        ra.setFillAfter(true);
        //imageView2.startAnimation(ra);
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
        //read sensor value from SensorEvent
        // Log.d("V", "ValuesPre" + Arrays.toString(event.values));

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


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
