package com.ticatag.tibeconnectkitdemo;

import android.app.Application;


import com.crashlytics.android.Crashlytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.ticatag.tibeconnectkit.BackgroundPowerSaver;
import io.fabric.sdk.android.Fabric;


/**
 * Created by fvisticot on 27/12/2016.
 */

public class TiBeConnectKitApplication extends Application {

    private MixpanelAPI mixpanel;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        String projectToken = "e14428e453802415bd67871b9869b241";
        mixpanel = MixpanelAPI.getInstance(this, projectToken);
        //BackgroundPowerSaver backgroundPowerSaver = new BackgroundPowerSaver(this);

    }

    public MixpanelAPI getMixpanel() {
        return mixpanel;
    }
}
