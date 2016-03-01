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
// import org.altbeacon.beacon.service.BeaconService;
// import org.altbeacon.beacon.startup.BootstrapNotifier;
// import org.altbeacon.beacon.startup.RegionBootstrap;
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
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "API TOKEN: " + this.settings.getApiToken());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "API USER: " + this.settings.getApiUser());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "DEVICE ID: " + this.settings.getDeviceId());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "API URL: " + this.settings.getApiUrl());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "API VERSION: " + this.settings.getApiVersion());
    }

    iBeaconManager = BeaconManager.getInstanceForApplication(this);
    iBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    backgroundPowerSaver = new BackgroundPowerSaver(this);
    iBeaconManager.setDebug(true);

    monitoringRegions = new HashMap<String, Region>();
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

    Region region = monitoringRegions.get(identifier);

    monitoringRegions.remove(identifier);

    try {
      iBeaconManager.stopMonitoringBeaconsInRegion(region);
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: stopped monitoring region");
    } catch (RemoteException e) {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: error stopping monitoring region");
    }

  }

  // public void onBeaconServiceConnect() {
  //     Log.d("uk.ac.lancaster.library.backgroundbeacons", "Connected to iBeacon Service");
  //     // this.rangeRegion();
  // }
  //
  // public void unbindService(ServiceConnection connection) {
  //   Log.d("uk.ac.lancaster.library.backgroundbeacons", "unbind service");
  //   this.getApplicationContext().unbindService(connection);
  // }
  //
  // public boolean bindService(Intent intent, ServiceConnection connection, int mode) {
  //   Log.d("uk.ac.lancaster.library.backgroundbeacons", "Bind Service");
  //   return this.getApplicationContext().bindService(intent, connection, mode);
  // }

  // private String beaconDistanceToProximity(double distance) {
  //
  //   if (distance > 0.0 && distance < 0.5) {
  //     return "ProximityImmediate";
  //   }
  //
  //   if (distance > 0.5 && distance < 3.0) {
  //     return "ProximityNear";
  //   }
  //
  //   if (distance > 3.0) {
  //     return "ProximityFar";
  //   }
  //
  //   return "ProximityUnknown";
  //
  // }
  //

  //
  // public void rangeRegion() {
  //   try {
  //     Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Starting to range regions");
  //
  //     iBeaconManager.startRangingBeaconsInRegion(region);
  //     iBeaconManager.setRangeNotifier(this);
  //
  //   } catch (RemoteException e) {
  //     Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: RANGE REMOTE EXCEPTION");
  //   }
  // }
  //
  // public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, final Region region) {
  //   Log.d("uk.ac.lancaster.library.backgroundbeacons", "didRangeBeaconsInRegion called");
  //
  //   String regionIdentifier = null;
  //   String regionUUID = null;
  //   String regionMajor = null;
  //   String regionMinor = null;
  //
  //   if (region.getUniqueId() != null) {
  //     regionIdentifier = region.getUniqueId().toString();
  //   }
  //
  //   if (region.getId1() != null) {
  //     regionUUID = region.getId1().toString();
  //   }
  //
  //   if (region.getId2() != null) {
  //     regionMajor = region.getId2().toString();
  //   }
  //
  //   if (region.getId3() != null) {
  //     regionMinor = region.getId3().toString();
  //   }
  //
  //   BeaconRegion beaconRegion = new BeaconRegion(regionIdentifier, regionUUID, regionMajor, regionMinor);
  //
  //   for (Beacon beacon: beacons) {
  //     Log.d("uk.ac.lancaster.library.backgroundbeacons", "I see beacon " + beacon);
  //
  //     String beaconUUID = null;
  //     String beaconMajor = null;
  //     String beaconMinor = null;
  //
  //     if (beacon.getId1() != null) {
  //       beaconUUID = beacon.getId1().toString();
  //     }
  //
  //     if (beacon.getId2() != null) {
  //       beaconMajor = beacon.getId2().toString();
  //     }
  //
  //     if (beacon.getId3() != null) {
  //       beaconMinor = beacon.getId3().toString();
  //     }
  //
  //     BeaconInfo beaconInfo = new BeaconInfo(beaconUUID, beaconMajor, beaconMinor);
  //     BeaconEvent beaconEvent = new BeaconEvent("onExitRegion", beaconInfo, beaconDistanceToProximity(beacon.getDistance()), Double.toString(beacon.getDistance()), Integer.toString(beacon.getRssi()));
  //
  //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  //     dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
  //     String timestamp = dateFormat.format(new Date());
  //
  //     BeaconTrackingEvent beaconTrackingEvent = new BeaconTrackingEvent(this.settings.getDeviceId(), beaconEvent, beaconRegion, timestamp);
  //
  //     beaconTrackingService.RangeBeaconEvent(beaconTrackingEvent);
  //
  //     Log.d("uk.ac.lancaster.library.backgroundbeacons", beaconTrackingEvent.toJsonObject().toString());
  //
  //   }
  // }
  //
  // public Context getApplicationContext() {
  //   return this.getApplication().getApplicationContext();
  // }

}
