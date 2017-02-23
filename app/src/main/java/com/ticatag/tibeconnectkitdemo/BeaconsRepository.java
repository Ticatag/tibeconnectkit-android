package com.ticatag.tibeconnectkitdemo;

import android.content.Context;
import android.util.Log;

import com.ticatag.tibeconnectkit.Beacon;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fvisticot on 22/01/2017.
 */

public class BeaconsRepository {
    private static BeaconsRepository mBeaconsRepository;
    private static Context mContext;
    private static String TAG = BeaconsManager.class.getSimpleName();
    private List<Beacon> mBeacons = new ArrayList<Beacon>();

    public static BeaconsRepository getInstance(Context context) {
        if (mBeaconsRepository == null) {
            mBeaconsRepository = new BeaconsRepository(context);
            mContext=context;
        }
        return mBeaconsRepository;
    }

    public BeaconsRepository(Context context) {
        mContext=context;
        mBeacons = loadBeacons();
    }

    public List<Beacon>getBeacons() {
        return mBeacons;
    }

    public void addBeacon(Beacon beacon) {
        Log.i(TAG, "addingBeacon");
        for (Beacon beaconIt: mBeacons) {
            if (beaconIt.getAddress().equals(beacon.getAddress())) {
                Log.i(TAG, "Beacon already persisted returning");
                return;
            }
        }
        mBeacons.add(beacon);
        saveBeacons();
    }

    public void saveBeacons() {
        Log.i(TAG, "saveBeacons");
        String filename = "beacons.txt";
        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
            for (Beacon beacon : mBeacons) {
                outWriter.write(beacon.getAddress());
                outWriter.append("\r\n");
            }
            outWriter.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Beacon> loadBeacons() {
        Log.i(TAG, "loadBeacons");
        String filename = "beacons.txt";
        FileInputStream inputStream;
        List<Beacon> beacons = new ArrayList<Beacon>();
        try {
            inputStream = mContext.openFileInput(filename);
            StringBuffer fileContent = new StringBuffer("");
            int n;
            byte[] buffer = new byte[1024];

            while ((n = inputStream.read(buffer)) != -1) {
                fileContent.append(new String(buffer, 0, n));
            }
            Log.i(TAG, fileContent.toString());
            String[] splitContent = fileContent.toString().split("\r\n");
            for (String address: splitContent) {
                address=address.trim();
                if (address.length() > 0) {
                    Beacon beacon = new Beacon(address);
                    beacons.add(beacon);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return beacons;
    }
}
