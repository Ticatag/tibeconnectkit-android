package com.ticatag.tibeconnectkitdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ticatag.tibeconnectkit.BluetoothLeService;

/**
 * Created by fvisticot on 18/01/2017.
 */

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        BeaconsManager beaconsManager = BeaconsManager.getInstance(context.getApplicationContext());


        Intent serviceIntent = new Intent(context.getApplicationContext(), BluetoothLeService.class);
        context.getApplicationContext().startService(serviceIntent);

    }

}