package com.ticatag.tibeconnectkitdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.ticatag.tibeconnectkit.Beacon;
import com.ticatag.tibeconnectkit.BeaconConsumer;
import com.ticatag.tibeconnectkit.BluetoothLeService;
import com.ticatag.tibeconnectkit.TBCManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

/**
 * Created by fvisticot on 19/01/2017.
 */

public class BeaconsManager implements BeaconConsumer {
    private static BeaconsManager mBeaconsManager;
    private TBCManager mTBCManager;
    private static String TAG = BeaconsManager.class.getSimpleName();
    private static Context mContext;

    public static BeaconsManager getInstance(Context context) {

        if (mBeaconsManager == null) {
            mBeaconsManager = new BeaconsManager(context);
            mContext=context;
        }
        return mBeaconsManager;
    }

    public BeaconsManager(Context context) {
        Log.i(TAG, "Starting BeaconsManager");
        mTBCManager = TBCManager.getInstance(context);
        mTBCManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "onBeaconServiceConnect");
        mTBCManager.scanDevice(true);
        List<Beacon> beacons = BeaconsRepository.getInstance(mContext).getBeacons();
        if (beacons.size() == 0) {
            Log.i(TAG, "No beacon persisted");
        } else {
            Log.i(TAG, "Reconnecting persisted beacons");
            for (Beacon beacon: beacons) {
                mTBCManager.connect(beacon, true);
            }
        }

        //Beacon beacon = new Beacon("C6:C1:D8:95:D3:08");
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }

    @Override
    public void onBeaconServiceInitializationFailed() {
        Log.i(TAG, "onBeaconServiceInitializationFailed");
    }

    @Override
    public void onBeaconServiceInitializationSuccess() {
        Log.i(TAG, "onBeaconServiceInitializationSuccess");
    }

    @Override
    public void onBeaconsInRange(List<Beacon> list) {
        //Log.i(TAG, "onBeaconsInRange with " + list.size() + " beacons");
    }

    @Override
    public void onBeaconConnected(Beacon beacon) {
        Log.i(TAG, "onBeaconConnected");
        playNotificationWithSound("Connection changed for beacon " + beacon.getAddress(), "CONNECTED");
        mTBCManager.getTemperature(beacon);
        mTBCManager.getButtonMode(beacon);
        mTBCManager.getBatteryLevel(beacon);
        mTBCManager.getFirmware(beacon);
        mTBCManager.setButtonMode(beacon, 0);
        mTBCManager.registerForTemperatureNotifications(beacon);
        mTBCManager.registerForBatteryNotification(beacon);
        mTBCManager.registerForAccelerometerNotifications(beacon);
        mTBCManager.registerForButtonNotification(beacon);
    }

    @Override
    public void onBeaconDisconnected(Beacon beacon) {
        playNotificationWithSound("Connection changed for beacon " + beacon.getAddress(), "DISCONNECTED");
    }

    @Override
    public void onTemperatureChanged(Beacon beacon) {
        Log.i(TAG, "onTemperatureChanged: " + beacon.getTemperature());
    }

    @Override
    public void onAccelerometerChanged(Beacon beacon) {

    }

    @Override
    public void onButtonModeChanged(Beacon beacon) {

    }

    @Override
    public void onBatteryLevelChanged(Beacon beacon) {
        Log.i(TAG, "onBatteryLevelChanged: " + beacon.getBatteryLevel() +"%");
    }

    @Override
    public void onFirmwareChanged(Beacon beacon) {
        Log.i(TAG, "onFirmwareChanged: " + beacon.getFirmware());
    }

    private void playNotificationWithSound(String title, String message) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message);
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        //https://developer.android.com/guide/topics/ui/notifiers/notifications.html

        Intent resultIntent = new Intent(mContext, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onButtonActionChanged(Beacon beacon, Integer action) {
        Log.i(TAG, "onButtonActionChanged: " + action);
        playNotificationWithSound("Button clicked", "Button: " + Integer.toString(action));
    }

    @Override
    public void onRSSIChanged(Beacon beacon) {

    }


}
