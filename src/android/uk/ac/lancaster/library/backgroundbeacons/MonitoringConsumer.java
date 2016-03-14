package uk.ac.lancaster.library.backgroundbeacons;

import org.altbeacon.beacon.BeaconConsumer;
// import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.BeaconManager;
// import org.altbeacon.beacon.Region;
import android.content.Context;
import android.content.Intent;
// import android.os.RemoteException;
// import java.util.ArrayList;
// import java.util.List;
import android.util.Log;
import android.content.ServiceConnection;

public class MonitoringConsumer implements BeaconConsumer {

  Context context;
  Intent serviceIntent;
  BeaconManager beaconManager;

  public MonitoringConsumer(Context context) {
    this.context = context;
    beaconManager = BeaconManager.getInstanceForApplication(this.context.getApplicationContext());
    beaconManager.bind(this);
  }

  public void onBeaconServiceConnect() {
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "BEACON CONSUMER CONNECTED");
  }

  public boolean bindService(Intent intent, ServiceConnection conn, int arg2) {
    this.serviceIntent = intent;
    return this.context.getApplicationContext().bindService(intent, conn, arg2);
  }

  public Context getApplicationContext() {
      return this.context.getApplicationContext();
  }

  public void unbindService(ServiceConnection conn) {
    this.getApplicationContext().unbindService(conn);
  }

}
