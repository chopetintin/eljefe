package com.example.compass1.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.compass1.R;

//Note that we are extending from the class Fragment
public class BatteryFragment extends Fragment {

    //Defining the variables that will be used in the Fragment
    private TextView info;
    private int BatteryL;
    private int BatteryV;
    private double BatteryT;
    private String BatteryTe;
    private String BatteryStatus;
    private String BatteryHealth;
    private String BatteryPlugged;
    private IntentFilter ifilter;


    //call to have the fragment instantiate the user interface view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_battery, container, false);
    }

    //Once the view is created, we can instantiate the parameters of the Fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Display the text view
        info = (TextView) view.findViewById(R.id.info);

        //Define the intent filter for the battery
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(mBatInfoReceiver, ifilter);
    }

    //Define the broadcast receiver

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            //Set the text to display

            info.setText("Battery Level: " + BatteryL + "%" + "\n" + "\n" +
                    "BatteryStatus: " + BatteryStatus + "\n" + "\n" +
                    "Battery Plugged: " + BatteryPlugged + "\n" + "\n" +
                    "Battery Health: " + BatteryHealth + "\n" + "\n" +
                    "Battery Voltage: " + (BatteryV / 1000) + "V" + "\n" + "\n" +
                    "Battery Temperature: " + (BatteryT * 0.1) + "C" + "\n" + "\n" +
                    "Battery Technology: " + BatteryTe);
            String action = intent.getAction();

            //Retrieve data from intent
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                BatteryL = intent.getIntExtra("level", 0);
                BatteryV = intent.getIntExtra("voltage", 0);
                BatteryT = intent.getIntExtra("temperature", 0);
                BatteryTe = intent.getStringExtra("technology");

                //Get the information of battery status
                switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        BatteryStatus = "Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        BatteryStatus = "Discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        BatteryStatus = "Not Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        BatteryStatus = "Fully Charged";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        BatteryStatus = "Unknown status";
                        break;
                }

                //Get the information of battery health status
                switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        BatteryHealth = "Unknown Status";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        BatteryHealth = "Good Status";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        BatteryHealth = "Dead Status";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        BatteryHealth = "Over Voltage";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        BatteryHealth = "Overheat";
                        break;
                }

                //Get the information of power resources
                switch (intent.getIntExtra("plugged", 0)) {
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        BatteryPlugged = "Plugged to AC";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        BatteryPlugged = "Plugged to USB";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                        BatteryPlugged = "Plugged to Wireless";
                        break;
                    default:
                        BatteryPlugged = "-----";
                }
            }
        }
    };
}
