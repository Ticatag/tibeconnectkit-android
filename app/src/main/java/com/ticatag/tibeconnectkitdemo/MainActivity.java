package com.ticatag.tibeconnectkitdemo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.ticatag.tibeconnectkit.Beacon;
import com.ticatag.tibeconnectkit.BeaconConsumer;
import com.ticatag.tibeconnectkit.TBCManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private BeaconsAdapter mBeaconsAdapter;
    private ArrayList<Beacon> mBeacons = new ArrayList<Beacon>();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private TBCManager mBeaconManager;
    protected static final String TAG = "MainActivity";
    private static MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        mixpanel = ((TiBeConnectKitApplication) getApplication()).getMixpanel();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBeaconsAdapter = new BeaconsAdapter(mBeacons);
        mRecyclerView.setAdapter(mBeaconsAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            } else {
                mBeaconManager = TBCManager.getInstance(this);
                mBeaconManager.bind(this);
            }
        } else {
            mBeaconManager = TBCManager.getInstance(this);
            mBeaconManager.bind(this);
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void unbindService(ServiceConnection connection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection connection, int mode) {
        return getBaseContext().bindService(intent, connection, mode);
    }

    @Override
    public void onBeaconServiceInitializationFailed() {
        Log.e(TAG, "onBeaconServiceInitializationFailed");
    }

    @Override
    public void onBeaconServiceInitializationSuccess() {
        Log.i(TAG, "onBeaconServiceInitializationSuccess");
    }

    @Override
    public void onBeaconsInRange(List<Beacon> beacons) {
        mBeacons.clear();
        mBeacons.addAll(beacons);
        mBeaconsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "onBeaconServiceConnect");
        mBeaconManager.scanDevice(true);

    }

    @Override
    public void onBeaconConnected(Beacon beacon) {
        //createLocationRequest();
    }

    @Override
    public void onBeaconDisconnected(Beacon beacon) {
        //createLocationRequest();
    }



    @Override
    public void onTemperatureChanged(Beacon beacon) {

    }

    @Override
    public void onAccelerometerChanged(Beacon beacon) {

    }

    @Override
    public void onButtonModeChanged(Beacon beacon) {

    }

    @Override
    public void onBatteryLevelChanged(Beacon beacon) {

    }

    @Override
    public void onFirmwareChanged(Beacon beacon) {

    }

    @Override
    public void onButtonActionChanged(Beacon beacon, Integer action) {

    }

    @Override
    public void onRSSIChanged(Beacon beacon) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                    mBeaconManager =TBCManager.getInstance(MainActivity.this);
                    mBeaconManager.bind(MainActivity.this);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
    }




}
