package uk.ac.lancaster.library.backgroundbeacons;

import uk.ac.lancaster.library.backgroundbeacons.BeaconEvent;
import uk.ac.lancaster.library.backgroundbeacons.Region;

import org.json.JSONObject;

public class BeaconTrackingEvent {

  private String deviceId;
  private BeaconEvent beaconEvent;
  private Region region;
  private String timestamp

  public BeaconTrackingEvent(String deviceId, BeaconEvent beaconEvent, Region region, String timestamp) {
    this.deviceId = deviceId;
    this.beaconEvent = beaconEvent;
    this.region = region;
    this.timestamp = timestamp;
  }

  public JSONObject toJsonObject() {

    JSONObject response = new JSONObject();

    response.accumulate("device_id", this.deviceId);
    response.accumulate("beacon_event", this.beaconEvent.toJsonObject().toString());
    response.accumulate("region", this.region.toJsonObject().toString());
    response.accumulate("event_timestamp", this.timestamp);

  }

}
