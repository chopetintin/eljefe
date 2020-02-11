package com.example.compass1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compass1.BatteryFragment.BatteryFragmentClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

//public class MainActivity extends Activity implements SensorEventListener {
    public class MainActivity extends Activity{




//    Animation rotateAnimation;
//    ImageView imageView2;
//    TextView grados;
//    TextView tv;
//    private SensorManager sm;
//    private float x, y, z;
//    private double h;
//    // we need two sensors in this application
//    private Sensor aSensor;
//    private Sensor mSensor;
//    float[] accelerometerValues = new float[3];
//    float[] magneticFieldValues = new float[3];
//
//    public TextView TV;
//    private Button button2;
//    private Uri fileUri;
//    public static final int MEDIA_TYPE_IMAGE = 1;
//    private String currentPhotoPath;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//
//        TV = (TextView) findViewById(R.id.TV);

        //initialization();
//        calculateOrientation();
//        configureNextButton();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_bar_history_item:
                        Toast.makeText(MainActivity.this, "History", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bottom_nav_bar_fitness_item:
                        Toast.makeText(MainActivity.this, "Fitness", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bottom_nav_bar_compass_item:
                        Toast.makeText(MainActivity.this, "Compass", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bottom_nav_bar_picture_item:
                        Toast.makeText(MainActivity.this, "Picture", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bottom_nav_bar_battery_item:
                        Toast.makeText(MainActivity.this, "Battery", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });



    }




//    public void takePhoto(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        fileUri = getOutputMediaFileUri();
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//    }
//
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
//        String imageFileName = "JPEG_" + timeStamp + "_";
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
//
////
//
//    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
//        imageView2 = (ImageView) findViewById(R.id.imageView2);
//        tv = (TextView) findViewById(R.id.tvDirection);
//        grados = (TextView) findViewById(R.id.degreeview);
//        tv.setText("Direction");
//
//
//        //sensors called when the activity starts
//        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//
//    }
//
//    private void configureNextButton() {
//
//        button2 = (Button) findViewById(R.id.button2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, BatteryActivity.class));
//            }
//        });
//    }
//
//    private void calculateOrientation() {
//        //TODO Auto-generated method stub
//        float[] values = new float[3];
//        float[] R = new float[9];
//
//        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
//        SensorManager.getOrientation(R, values);
//
//        float degree = (float) Math.toDegrees(values[0]);
//        values[1] = (float) Math.toDegrees(values[1]);
//        values[2] = (float) Math.toDegrees(values[2]);
//        Log.d("CREATION", "calculateOrientation is being executed");
//        grados.setText(Float.toString(degree));
//
//        if (degree >= -5 && degree < 5) {
//            tv.setText("North");
//        } else if (degree >= 5 && degree < 40) {
//            tv.setText("North North East");
//        } else if (degree >= 40 && degree < 50) {
//            tv.setText("North East");
//        } else if (degree >= 50 && degree < 85) {
//            tv.setText("East North East");
//        } else if (degree >= 85 && degree < 95) {
//            tv.setText("East");
//        } else if (degree >= 95 && degree < 130) {
//            tv.setText("East South East");
//        } else if (degree >= 130 && degree < 140) {
//            tv.setText("South East");
//        } else if (degree >= 140 && degree < 175) {
//            tv.setText("South South East");
//        } else if ((degree >= 175 && degree <= 180) || (degree >= -180 && degree < -175)) {
//            tv.setText("South");
//        } else if (degree >= -175 && degree < -140) {
//            tv.setText("South South West");
//        } else if (degree >= -140 && degree < -130) {
//            tv.setText("South West");
//        } else if (degree >= -130 && degree < -95) {
//            tv.setText("West South West");
//        } else if (degree >= -95 && degree < -85) {
//            tv.setText("West");
//        } else if (degree >= -85 && degree < -50) {
//            tv.setText("West North West");
//        } else if (degree >= -50 && degree < -40) {
//            tv.setText("North West");
//        } else if (degree >= -40 && degree < -50) {
//            tv.setText("North North West");
//        }
//
//        RotateAnimation ra = new RotateAnimation(
//                -degree,
//                -degree,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF,
//                0.5f);
//        ra.setDuration(500);
//        ra.setFillAfter(true);
//        imageView2.startAnimation(ra);
//    }


    @Override
    protected void onPause() {
        super.onPause();
        //Unregister sensor manager when activity is not visible
        //sm.unregisterListener(this);
        Log.d("PAUSE", "onPause is being executed");

    }

    @Override
    protected void onResume() {
        super.onResume();
        //enable sensor manager when activity returns
        //sm.registerListener(this, mSensor, sm.SENSOR_DELAY_GAME);
        //sm.registerListener(this, aSensor, sm.SENSOR_DELAY_GAME);
        Log.d("RESUME", "onResume is being executed");


    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//        //read sensor value from SensorEvent
//
//
//        Log.d("V", "ValuesPre" + Arrays.toString(event.values));
//
//        //calculate the total magnetic field
//        //h = Math.sqrt(event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2]);
//        final float alpha = 0.97f;
//        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//            //low pass filter
//            magneticFieldValues[0] = alpha * magneticFieldValues[0] + (1 - alpha) * event.values[0];
//            magneticFieldValues[1] = alpha * magneticFieldValues[1] + (1 - alpha) * event.values[1];
//            magneticFieldValues[2] = alpha * magneticFieldValues[2] + (1 - alpha) * event.values[2];
//
//
//        }
//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            //low pass filter
//            accelerometerValues[0] = alpha * accelerometerValues[0] + (1 - alpha) * event.values[0];
//            accelerometerValues[1] = alpha * accelerometerValues[1] + (1 - alpha) * event.values[1];
//            accelerometerValues[2] = alpha * accelerometerValues[2] + (1 - alpha) * event.values[2];
//        }
//
//
//        calculateOrientation();
//
//        Log.d("V", "Values" + Arrays.toString(event.values));
//        Log.d("M", "Magnetic:" + Arrays.toString(magneticFieldValues));
//        Log.d("A", "Accelerometer:" + Arrays.toString(accelerometerValues));
//    }
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }


}