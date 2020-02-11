package com.example.compass1.battery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.compass1.R;

public class BatteryFragment extends Fragment {

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_battery, container, false);


    }
}


//    public TextView info;
//    public int BatteryL;
//    private int BatteryV;
//    private double BatteryT;
//    private String BatteryTe;
//    private String BatteryStatus;
//    private String BatteryHealth;
//    private String BatteryPlugged;
//    private Button button3;
//
//    private IntentFilter ifilter;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_battery);
//
//        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        registerReceiver(mBatInfoReceiver, ifilter);
//        info = (TextView) findViewById(R.id.info);
//
//        configureBackButton();
//    }

//    private void configureBackButton() {
//        button3 = (Button) findViewById(R.id.button3);
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//    }
//
//    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//
//            info.setText("Battery Level: " + BatteryL + "%" + "\n" + "\n" +
//                    "BatteryStatus: " + BatteryStatus + "\n" + "\n" +
//                    "Battery Plugged: " + BatteryPlugged + "\n" + "\n" +
//                    "Battery Health: " + BatteryPlugged + "\n" + "\n" +
//                    "Battery Voltage: " + (BatteryV / 1000) + "V" + "\n" + "\n" +
//                    "Battery Temperature: " + (BatteryT * 0.1) + "C" + "\n" + "\n" +
//                    "Battery Technology: " + BatteryTe);
//            String action = intent.getAction();
//            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
//                BatteryL = intent.getIntExtra("level", 0);
//                BatteryV = intent.getIntExtra("voltage", 0);
//                BatteryT = intent.getIntExtra("temperature", 0);
//                BatteryTe = intent.getStringExtra("technology");
//
//                switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
//                    case BatteryManager.BATTERY_STATUS_CHARGING:
//                        BatteryStatus = "Charging";
//                        break;
//                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
//                        BatteryStatus = "Discharging";
//                        break;
//                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
//                        BatteryStatus = "Not Charging";
//                        break;
//                    case BatteryManager.BATTERY_STATUS_FULL:
//                        BatteryStatus = "Fully Charged";
//                        break;
//                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
//                        BatteryStatus = "Unknown status";
//                        break;
//                }
//                switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
//                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
//                        BatteryHealth = "Unknown Status";
//                        break;
//                    case BatteryManager.BATTERY_HEALTH_GOOD:
//                        BatteryHealth = "Good Status";
//                        break;
//                    case BatteryManager.BATTERY_HEALTH_DEAD:
//                        BatteryHealth = "Dead Status";
//                        break;
//                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
//                        BatteryHealth = "Over Voltage";
//                        break;
//                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
//                        BatteryHealth = "Overheat";
//                        break;
//                }
//                switch (intent.getIntExtra("plugged", 0)) {
//                    case BatteryManager.BATTERY_PLUGGED_AC:
//                        BatteryPlugged = "Plugged to AC";
//                        break;
//                    case BatteryManager.BATTERY_PLUGGED_USB:
//                        BatteryPlugged = "Plugged to USB";
//                        break;
//                    case BatteryManager.BATTERY_PLUGGED_WIRELESS:
//                        BatteryPlugged = "Plugged to Wireless";
//                        break;
//                    default:
//                        BatteryPlugged = "-----";
//                }
//            }
//        }
//    };



