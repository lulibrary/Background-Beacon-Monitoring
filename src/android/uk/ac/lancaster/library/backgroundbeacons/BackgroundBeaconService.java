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

import uk.ac.lancaster.library.backgroundbeacons.SharedPreferencesUtility;

public class BackgroundBeaconService extends Service implements BootstrapNotifier, BeaconConsumer {

  public BackgroundBeaconService()  {
    super();
  }

  private BackgroundPowerSaver backgroundPowerSaver;
  private BeaconManager iBeaconManager;
  private RegionBootstrap regionBootstrap;
  private Region region;
  private SharedPreferencesUtility settings;

  public void onCreate() {

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Creating BackgroundBeaconService");

    super.onCreate();

    this.settings = new SharedPreferencesUtility(this.getApplicationContext());

    if (this.settings.exist()) {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", this.settings.getApiToken());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", this.settings.getApiUser());
      Log.d("uk.ac.lancaster.library.backgroundbeacons", this.settings.getDeviceId());
    }

    iBeaconManager = BeaconManager.getInstanceForApplication(this);
    iBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    backgroundPowerSaver = new BackgroundPowerSaver(this);
    iBeaconManager.setDebug(true);

    region = new Region("testingRegion", Identifier.parse("f7826da6-4fa2-4e98-8024-bc5b71e0893e"), null, null);
    regionBootstrap = new RegionBootstrap(this, region);

    iBeaconManager.bind(this);

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Created RegionBootstrap in BackgroundBeaconService.");

  }

  public void onDestroy() {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "Destroying BackgroundBeaconService");
  }

  public void didEnterRegion(Region region) {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Entered region.");
  }

  public void didExitRegion(Region region) {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Exited region.");
  }

  public void didDetermineStateForRegion(int state, Region region) {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Region changed state.");
  }

  public void rangeRegion() {
    try {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: Starting to range regions");

      iBeaconManager.startRangingBeaconsInRegion(region);
      iBeaconManager.setRangeNotifier(new RangeNotifier() {

        public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, final Region region) {
          Log.d("uk.ac.lancaster.library.backgroundbeacons", "didRangeBeaconsInRegion called");
          for (Beacon beacon: beacons) {
            Log.d("uk.ac.lancaster.library.backgroundbeacons", "I see beacon " + beacon);
          }
        }

      });
    } catch (RemoteException e) {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "BACKGROUND: RANGE REMOTE EXCEPTION");
    }
  }

  public Context getApplicationContext() {
    return this.getApplication().getApplicationContext();
  }

  public IBinder onBind(Intent intent) {
    return null;
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

}
