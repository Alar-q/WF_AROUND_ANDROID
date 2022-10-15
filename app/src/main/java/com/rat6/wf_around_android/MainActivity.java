package com.rat6.wf_around_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;


/**
 *
 * Нужно дать разрешение и включить геолокацию вручную
 *
 * */
public class MainActivity extends AppCompatActivity {
    static final String TAG = "WIFI";

    WifiManager wifiManager;

    BroadcastReceiver wifiReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Срабатывает, когда завершается wifiManager.startScan()
        wifiReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                Log.d(TAG, "Receiver " + success);

                List<ScanResult> result = wifiManager.getScanResults();

                Log.d(TAG, "Founded : " + result.size());
                for(ScanResult rs: result){
                    Log.d(TAG, "Result : " + rs.toString());
                }

//                unregisterReceiver(this); // хз нужно ли это
            }
        };

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if(!wifiManager.isWifiEnabled())
            Log.d(TAG, "WIFI is not enabled");

        // Это вроде не работает из-за ограничений андроида, нельзя часто сканировать wifi-сети
//        long nanoTime = System.nanoTime();
//        while(true){
//            if(System.nanoTime() - nanoTime > 1000000000l){
//                nanoTime = System.nanoTime();
//                scanWifi();
//            }
//        }

        scanWifi();
    }

    private void scanWifi(){
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        boolean success = wifiManager.startScan();
        Log.d(TAG, "Scanning..." + success);
    }

}