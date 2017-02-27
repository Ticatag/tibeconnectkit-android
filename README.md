[![TiBeConnect Kit by Ticatag](logo.png)](https://ticatag.com)

## Prerequisites
To use this SDK, you need to use the Ticatag TibeConnect button.
You can buy this button online [here](http://www.ticatag.com/categorie-produit/b2c)

## About TiBeConnect Kit for Android 

This Kit provides all the capabilities to manage the TiBeConnect button:
- Scan buttons around you
- Button connection / reconnection
- Button RSSI
- Button buzzer
- Button temperature change notification
- Button accelerometer 3 axis notifications
- Button clicks (simple, double, long press) change notification
- Mode button : with this mode the button is placed in standby and is awake only when movement is detected on the button. This feature will drastically increase button battery life.
- Button firmware version

## Getting started
A complete working example is available [Here](https://github.com/Ticatag/tibeconnectkit-android)

## Download
Gradle

```javascript
dependencies {
  compile 'com.ticatag.tibeconnectkit:tibeconnectkit:0.0.1'
}
```

## init TBCManager
your acvitiy need to implement BeaconConsumer
```
  mBeaconManager = TBCManager.getInstance(this);
  mBeaconManager.bind(this);
```
## Scan devices
```   
  @Override
  public void onBeaconServiceConnect() {
    mBeaconManager.scanDevice(true);
  }
  
  @Override
  public void onBeaconsInRange(List<Beacon> beacons) {
    //list of beacons detected
  }
  
```

## Issues
 For general service questions and help consult the Ticatag  [support knowledge base](https://ticatag.zendesk.com/).

If you've found a problem in this library, perform a search under
[Issues](https://github.com/TicatagSAS/tibeconnectkit-ios/issues?q=is%3Aissue+)
in case it has already been reported. If you do not find any issue addressing it, feel free to [open a new
one](https://github.com/TicatagSAS/tibeconnectkit-ios/issues/new).

Your issue report should contain a title and a clear description of the issue at a minimum. Please provide as much relevant information as possible to replicate the issue. This should include the Java and library versions, a code
sample demonstrating the issue, and device OS information. Providing a unit test that demonstrates the issue is greatly appreciated. Your goal should be to make it easy for yourself - and others - to replicate the bug and figure out a
fix.

## License

    Copyright 2017 Tuyen Monkey

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
