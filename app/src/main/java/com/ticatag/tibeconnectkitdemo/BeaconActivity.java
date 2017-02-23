package com.ticatag.tibeconnectkitdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.ticatag.tibeconnectkit.Beacon;
import com.ticatag.tibeconnectkit.BeaconConsumer;
import com.ticatag.tibeconnectkit.TBCManager;

import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.ticatag.tibeconnectkitdemo.R.id.accelerometer_textview;
import static com.ticatag.tibeconnectkitdemo.R.id.battery_textview;
import static com.ticatag.tibeconnectkitdemo.R.id.button_mode_switch;
import static com.ticatag.tibeconnectkitdemo.R.id.firmware_version_textview;
import static com.ticatag.tibeconnectkitdemo.R.id.ping_beacon_button;
import static com.ticatag.tibeconnectkitdemo.R.id.rssi_textview;
import static com.ticatag.tibeconnectkitdemo.R.id.temperature_textview;


/**
 * Created by fvisticot on 17/12/2016.
 */


//http://developer.radiusnetworks.com/2015/09/29/is-your-beacon-app-ready-for-android-6.html
public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "BeaconActivity";
    private TBCManager mBeaconManager;
    private Button mPingButton;
    private Beacon mBeacon;
    private static MixpanelAPI mixpanel;
    private boolean mBackPressed;

    private TextView mBatteryTextView;
    private TextView mRssiTextView;
    private TextView mAccelerometerTextView;
    private TextView mFirmwareVersionTextView;
    private TextView mTemperatureTextView;
    private Switch mButtonModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_beacon);
        mixpanel = ((TiBeConnectKitApplication)getApplication()).getMixpanel();
        mBeacon = this.getIntent().getParcelableExtra("BEACON");
        Log.i(TAG, "onCreate: " + mBeacon.getAddress());
        mBeaconManager = TBCManager.getInstance(this);
        mBeaconManager.bind(this);
        mixpanel.timeEvent("TiBe connection");
        mBeaconManager.connect(mBeacon, false);

        mPingButton = (Button) findViewById(ping_beacon_button);
        mPingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBeaconManager.pingBeacon(mBeacon);
            }
        });
        mPingButton.setVisibility(INVISIBLE);


        mBatteryTextView = (TextView)findViewById(battery_textview);
        mRssiTextView = (TextView)findViewById(rssi_textview);
        mAccelerometerTextView = (TextView)findViewById(accelerometer_textview);
        mFirmwareVersionTextView = (TextView)findViewById(firmware_version_textview);
        mTemperatureTextView = (TextView)findViewById(temperature_textview);
        mButtonModeSwitch = (Switch)findViewById(button_mode_switch);

        mButtonModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBeaconManager.setButtonMode(mBeacon, isChecked?1:0);
            }
        });

        setTitle(mBeacon.getMajor()+ "_" + mBeacon.getMinor());
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        //mBeaconManager.disconnect(mBeacon);
        //mBeaconManager.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        mBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

    }

    @Override
    public void onBeaconServiceInitializationFailed() {

    }

    @Override
    public void onBeaconServiceInitializationSuccess() {

    }

    @Override
    public void onBeaconsInRange(List<Beacon> beacons) {

    }

    @Override
    public void onBeaconConnected(Beacon beacon) {
        Log.i(TAG, beacon.getAddress() +" connected");
        BeaconsRepository.getInstance(this).addBeacon(beacon);
        mixpanel.track("TiBe connection");
        mPingButton.setVisibility(VISIBLE);
        mBeaconManager.getTemperature(mBeacon);
        mBeaconManager.getButtonMode(mBeacon);
        mBeaconManager.getBatteryLevel(mBeacon);
        mBeaconManager.getFirmware(mBeacon);
        mBeaconManager.setButtonMode(mBeacon, 0);
        mBeaconManager.registerForTemperatureNotifications(mBeacon);
        mBeaconManager.registerForBatteryNotification(mBeacon);
        mBeaconManager.registerForAccelerometerNotifications(mBeacon);
        mBeaconManager.registerForButtonNotification(mBeacon);
        mBeaconManager.startRSSIMonitoring(mBeacon);
    }

    @Override
    public void onBeaconDisconnected(Beacon beacon) {
        Log.i(TAG, beacon.getAddress() + " disconnected");
        mPingButton.setVisibility(INVISIBLE);
        if (mBackPressed) {
            finish();
        }
    }

    @Override
    public void onTemperatureChanged(Beacon beacon) {
        Log.i(TAG, "onTemperatureChanged: " + beacon.getTemperature());
        mTemperatureTextView.setText("Temperature: " + beacon.getTemperature() + " °C");
    }

    @Override
    public void onAccelerometerChanged(Beacon beacon) {
        Log.i(TAG, "onAccelerometerChanged: " + beacon.getAccelerometer().getAxisX() + ", " + beacon.getAccelerometer().getAxisY() +", " + beacon.getAccelerometer().getAxisZ());
        mAccelerometerTextView.setText("AxisX: " + beacon.getAccelerometer().getAxisX() +
                ", Axis Y: " + beacon.getAccelerometer().getAxisY() +
                " , Axis Z: " + beacon.getAccelerometer().getAxisZ());
    }

    @Override
    public void onButtonModeChanged(Beacon beacon) {
        Log.i(TAG, "onButtonModeChanged: " + beacon.getButtonMode());
        mButtonModeSwitch.setChecked((beacon.getButtonMode()==1)?true: false);
    }

    @Override
    public void onBatteryLevelChanged(Beacon beacon) {
        Log.i(TAG, "onBatteryLevelChanged: " + beacon.getBatteryLevel() +"%");
        mBatteryTextView.setText("Battery: " + beacon.getBatteryLevel() +"%");
    }

    @Override
    public void onFirmwareChanged(Beacon beacon) {
        Log.i(TAG, "onFirmwareChanged: " + beacon.getFirmware());
        mFirmwareVersionTextView.setText("firmware: " + beacon.getFirmware());
    }

    @Override
    public void onButtonActionChanged(Beacon beacon, Integer action) {
        Log.i(TAG, "onButtonActionChanged: " + action);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Button clicked")
                        .setContentText("Button: " + Integer.toString(action));
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        //https://developer.android.com/guide/topics/ui/notifiers/notifications.html

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onRSSIChanged(Beacon beacon) {
        mRssiTextView.setText("RSSI: " + beacon.getRSSI() + " dB");
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        mBeaconManager.disconnect(mBeacon);
        mBackPressed=true;
    }
}

