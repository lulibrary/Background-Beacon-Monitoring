package uk.ac.lancaster.library.backgroundbeacons;

import android.app.Application;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.Collection;
import org.altbeacon.beacon.*;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import android.content.ServiceConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import uk.ac.lancaster.library.backgroundbeacons.SharedPreferencesUtility;
import uk.ac.lancaster.library.backgroundbeacons.BeaconLoggingMonitorNotifier;

public class BackgroundBeaconService extends Service {

  public BackgroundBeaconService()  {
    super();
  }

  private BackgroundPowerSaver backgroundPowerSaver;
  private BeaconManager iBeaconManager;
  private SharedPreferencesUtility settings;
  private final IBinder mBinder = new LocalBinder();
  private HashMap<String, Region> monitoringRegions;
  private HashMap<String, Region> rangingRegions;
  private BeaconConsumer monitoringConsumer;
  private MonitorNotifier notifier;

  public class LocalBinder extends Binder {
    BackgroundBeaconService getService() {
      return BackgroundBeaconService.this;
    }
  }

  public void onCreate() {

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Creating BackgroundBeaconService");

    super.onCreate();

    this.settings = new SharedPreferencesUtility(this.getApplicationContext());

    if (this.settings.exist()) {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "API PARTICIPANT TOKEN: " + this.settings.getApiParticipantToken());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "API PARTICIPANT EMAIL: " + this.settings.getApiParticipantEmail());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "DEVICE ID: " + this.settings.getDeviceId());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "API URL: " + this.settings.getApiUrl());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "API VERSION: " + this.settings.getApiVersion());
      Log.d("ul.ac.lancaster.library.backgroundbeacons", "SEND MOVEMENT DATA: " + this.settings.getSendMovementData());
    }

    iBeaconManager = BeaconManager.getInstanceForApplication(this);
    iBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    backgroundPowerSaver = new BackgroundPowerSaver(this);
    iBeaconManager.setDebug(true);

    monitoringRegions = new HashMap<String, Region>();
    rangingRegions = new HashMap<String, Region>();
    monitoringConsumer = new MonitoringConsumer(this);

  }

  public void onDestroy() {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "Destroying BackgroundBeaconService");
  }

  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  public void testBinding() {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "TESTING SERVICE IS BOUND");
  }

  public void startMonitoringRegion(String identifier, String uuid, Integer major, Integer minor) {

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "Before local method declaration");

    Identifier regionMajor;
    Identifier regionMinor;

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: starting monitoring region");

    if (major == null) {
      regionMajor = null;
    } else {
      regionMajor = Identifier.fromInt(major);
    }

    if (minor == null) {
      regionMinor = null;
    } else {
      regionMinor = Identifier.fromInt(minor);
    }

    Region region = new Region(identifier, Identifier.parse(uuid), regionMajor, regionMinor);

    monitoringRegions.put(identifier, region);

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: added region to array");

    iBeaconManager.setMonitorNotifier(new BeaconLoggingMonitorNotifier(this.settings));

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: set notifier");

    try {

      iBeaconManager.startMonitoringBeaconsInRegion(region);

      if(iBeaconManager.isBackgroundModeUninitialized()) {
        iBeaconManager.setBackgroundMode(true);
      }

      Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: started monitoring region");

    } catch (RemoteException e) {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "Error monitoring region");
    }

  }

  public void stopMonitoringRegion(String identifier) {

    if (monitoringRegions.containsKey(identifier)) {

      Region region = monitoringRegions.get(identifier);

      monitoringRegions.remove(identifier);

      try {
        iBeaconManager.stopMonitoringBeaconsInRegion(region);
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: stopped monitoring region : " + region.toString());
      } catch (RemoteException e) {
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: error stopping monitoring region");
      }

    }

  }

  public void startRangingRegion(String identifier, String uuid, Integer major, Integer minor) {

    Identifier regionMajor;
    Identifier regionMinor;

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: starting ranging region");

    if (major == null) {
      regionMajor = null;
    } else {
      regionMajor = Identifier.fromInt(major);
    }

    if (minor == null) {
      regionMinor = null;
    } else {
      regionMinor = Identifier.fromInt(minor);
    }

    Region region = new Region(identifier, Identifier.parse(uuid), regionMajor, regionMinor);

    rangingRegions.put(identifier, region);

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: added region to array");

    iBeaconManager.setRangeNotifier(new BeaconLoggingRangeNotifier(this.settings));

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: set range notifier");

    try {

      iBeaconManager.startRangingBeaconsInRegion(region);

      if(iBeaconManager.isBackgroundModeUninitialized()) {
        iBeaconManager.setBackgroundMode(true);
      }

      Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: started ranging beacons in region");

    } catch (RemoteException e) {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "Error ranging region");
    }

  }

  public void stopRangingRegion(String identifier) {

    if (rangingRegions.containsKey(identifier)) {

      Region region = rangingRegions.get(identifier);

      rangingRegions.remove(identifier);

      try {
        iBeaconManager.stopRangingBeaconsInRegion(region);
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: stopped ranging region : "  + region.toString());
      } catch (RemoteException e) {
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: error stopping ranging region");
      }

    }

  }

  public void setMovementPreference(Boolean preference) {
    this.settings.setSendMovementData(preference);
  }

}
