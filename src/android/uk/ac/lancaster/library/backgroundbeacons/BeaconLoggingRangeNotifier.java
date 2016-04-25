package uk.ac.lancaster.library.backgroundbeacons;

import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.Beacon;

import android.util.Log;

import uk.ac.lancaster.library.backgroundbeacons.SharedPreferencesUtility;
import uk.ac.lancaster.library.backgroundbeacons.BeaconTrackingService;
import uk.ac.lancaster.library.backgroundbeacons.BeaconTrackingEvent;
import uk.ac.lancaster.library.backgroundbeacons.BeaconInfo;
import uk.ac.lancaster.library.backgroundbeacons.BeaconEvent;

import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Collection;

public class BeaconLoggingRangeNotifier implements RangeNotifier {

  private BeaconTrackingService beaconTrackingService;
  private SharedPreferencesUtility settings;

  public BeaconLoggingRangeNotifier(SharedPreferencesUtility settings) {
    this.settings = settings;
    this.beaconTrackingService = new BeaconTrackingService(this.settings);
  }

  public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

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
      BeaconEvent beaconEvent = new BeaconEvent(beaconInfo, beaconDistanceToProximity(beacon.getDistance()), Double.toString(beacon.getDistance()), Integer.toString(beacon.getRssi()));

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      String timestamp = dateFormat.format(new Date());

      BeaconTrackingEvent beaconTrackingEvent = new BeaconTrackingEvent(this.settings.getDeviceId(), beaconEvent, beaconRegion, timestamp);

      this.beaconTrackingService.RangeBeaconEvent(beaconTrackingEvent);

      Log.d("uk.ac.lancaster.library.backgroundbeacons", beaconTrackingEvent.toJsonObject().toString());

    }

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


}
