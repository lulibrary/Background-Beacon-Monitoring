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
import org.altbeacon.beacon.service.BeaconService;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import android.content.ServiceConnection;

import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import uk.ac.lancaster.library.backgroundbeacons.SharedPreferencesUtility;
import uk.ac.lancaster.library.backgroundbeacons.BeaconTrackingEvent;
import uk.ac.lancaster.library.backgroundbeacons.BeaconTrackingService;
import uk.ac.lancaster.library.backgroundbeacons.BeaconInfo;
import uk.ac.lancaster.library.backgroundbeacons.BeaconEvent;
import uk.ac.lancaster.library.backgroundbeacons.BeaconRegion;

public class BackgroundBeaconService extends Service implements BootstrapNotifier, BeaconConsumer, RangeNotifier {

  public BackgroundBeaconService()  {
    super();
  }

  private BackgroundPowerSaver backgroundPowerSaver;
  private BeaconManager iBeaconManager;
  private RegionBootstrap regionBootstrap;
  private Region region;
  private SharedPreferencesUtility settings;
  private BeaconTrackingService beaconTrackingService;
  private final IBinder mBinder = new LocalBinder();

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

    region = new Region("testingRegion", Identifier.parse("f7826da6-4fa2-4e98-8024-bc5b71e0893e"), null, null);
    regionBootstrap = new RegionBootstrap(this, region);

    iBeaconManager.bind(this);

    beaconTrackingService = new BeaconTrackingService(this.settings);

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Created RegionBootstrap in BackgroundBeaconService.");

  }

  public void onDestroy() {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "Destroying BackgroundBeaconService");
  }

  public void didEnterRegion(Region region) {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Entered region.");

    BeaconInfo beaconInfo = new BeaconInfo(null, null, null);
    BeaconEvent beaconEvent = new BeaconEvent("onEnterRegion", beaconInfo, null, null, null);

    String regionIdentifier = null;
    String regionUUID = null;
    String regionMajor = null;
    String regionMinor = null;

    if (region.getUniqueId() != null) {
      regionIdentifier = region.getUniqueId().toString();
    }

    if (region.getId1() != null) {
      regionUUID = region.getId1().toString();
    }

    if (region.getId2() != null) {
      regionMajor = region.getId2().toString();
    }

    if (region.getId3() != null) {
      regionMinor = region.getId3().toString();
    }

    BeaconRegion beaconRegion = new BeaconRegion(regionIdentifier, regionUUID, regionMajor, regionMinor);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    String timestamp = dateFormat.format(new Date());

    BeaconTrackingEvent beaconTrackingEvent = new BeaconTrackingEvent(this.settings.getDeviceId(), beaconEvent, beaconRegion, timestamp);

    beaconTrackingService.EnterRegionEvent(beaconTrackingEvent);

    Log.d("uk.ac.lancaster.library.backgroundbeacons", beaconTrackingEvent.toJsonObject().toString());
  }

  public void didExitRegion(Region region) {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Exited region.");

    BeaconInfo beaconInfo = new BeaconInfo(null, null, null);
    BeaconEvent beaconEvent = new BeaconEvent("onExitRegion", beaconInfo, null, null, null);

    String regionIdentifier = null;
    String regionUUID = null;
    String regionMajor = null;
    String regionMinor = null;

    if (region.getUniqueId() != null) {
      regionIdentifier = region.getUniqueId().toString();
    }

    if (region.getId1() != null) {
      regionUUID = region.getId1().toString();
    }

    if (region.getId2() != null) {
      regionMajor = region.getId2().toString();
    }

    if (region.getId3() != null) {
      regionMinor = region.getId3().toString();
    }

    BeaconRegion beaconRegion = new BeaconRegion(regionIdentifier, regionUUID, regionMajor, regionMinor);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    String timestamp = dateFormat.format(new Date());

    BeaconTrackingEvent beaconTrackingEvent = new BeaconTrackingEvent(this.settings.getDeviceId(), beaconEvent, beaconRegion, timestamp);

    beaconTrackingService.ExitRegionEvent(beaconTrackingEvent);

    Log.d("uk.ac.lancaster.library.backgroundbeacons", beaconTrackingEvent.toJsonObject().toString());
  }

  public void didDetermineStateForRegion(int state, Region region) {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Region changed state.");
  }

  public void rangeRegion() {
    try {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Starting to range regions");

      iBeaconManager.startRangingBeaconsInRegion(region);
      iBeaconManager.setRangeNotifier(this);

    } catch (RemoteException e) {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: RANGE REMOTE EXCEPTION");
    }
  }

  public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, final Region region) {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "didRangeBeaconsInRegion called");

    String regionIdentifier = null;
    String regionUUID = null;
    String regionMajor = null;
    String regionMinor = null;

    if (region.getUniqueId() != null) {
      regionIdentifier = region.getUniqueId().toString();
    }

    if (region.getId1() != null) {
      regionUUID = region.getId1().toString();
    }

    if (region.getId2() != null) {
      regionMajor = region.getId2().toString();
    }

    if (region.getId3() != null) {
      regionMinor = region.getId3().toString();
    }

    BeaconRegion beaconRegion = new BeaconRegion(regionIdentifier, regionUUID, regionMajor, regionMinor);

    for (Beacon beacon: beacons) {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "I see beacon " + beacon);

      String beaconUUID = null;
      String beaconMajor = null;
      String beaconMinor = null;

      if (beacon.getId1() != null) {
        beaconUUID = beacon.getId1().toString();
      }

      if (beacon.getId2() != null) {
        beaconMajor = beacon.getId2().toString();
      }

      if (beacon.getId3() != null) {
        beaconMinor = beacon.getId3().toString();
      }

      BeaconInfo beaconInfo = new BeaconInfo(beaconUUID, beaconMajor, beaconMinor);
      BeaconEvent beaconEvent = new BeaconEvent("onExitRegion", beaconInfo, beaconDistanceToProximity(beacon.getDistance()), Double.toString(beacon.getDistance()), Integer.toString(beacon.getRssi()));

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      String timestamp = dateFormat.format(new Date());

      BeaconTrackingEvent beaconTrackingEvent = new BeaconTrackingEvent(this.settings.getDeviceId(), beaconEvent, beaconRegion, timestamp);

      beaconTrackingService.RangeBeaconEvent(beaconTrackingEvent);

      Log.d("uk.ac.lancaster.library.backgroundbeacons", beaconTrackingEvent.toJsonObject().toString());

    }
  }

  public Context getApplicationContext() {
    return this.getApplication().getApplicationContext();
  }

  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  public void onBeaconServiceConnect() {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "Connected to iBeacon Service");
      this.rangeRegion();
  }

  public void unbindService(ServiceConnection connection) {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "unbind service");
    this.getApplicationContext().unbindService(connection);
  }

  public boolean bindService(Intent intent, ServiceConnection connection, int mode) {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "Bind Service");
    return this.getApplicationContext().bindService(intent, connection, mode);
  }

  private String beaconDistanceToProximity(double distance) {

    if (distance > 0.0 && distance < 0.5) {
      return "ProximityImmediate";
    }

    if (distance > 0.5 && distance < 3.0) {
      return "ProximityNear";
    }

    if (distance > 3.0) {
      return "ProximityFar";
    }

    return "ProximityUnknown";

  }

  public void testBinding() {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "TESTING SERVICE IS BOUND");
  }

}
