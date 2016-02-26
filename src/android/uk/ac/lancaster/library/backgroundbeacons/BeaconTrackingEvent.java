package uk.ac.lancaster.library.backgroundbeacons;

import uk.ac.lancaster.library.backgroundbeacons.BeaconEvent;
import uk.ac.lancaster.library.backgroundbeacons.BeaconRegion;

import org.json.JSONObject;
import org.json.JSONException;

public class BeaconTrackingEvent {

  private String deviceId;
  private BeaconEvent beaconEvent;
  private BeaconRegion region;
  private String timestamp;

  public BeaconTrackingEvent(String deviceId, BeaconEvent beaconEvent, BeaconRegion region, String timestamp) {
    this.deviceId = deviceId;
    this.beaconEvent = beaconEvent;
    this.region = region;
    this.timestamp = timestamp;
  }

  public JSONObject toJsonObject() {

    JSONObject response = new JSONObject();

    try {

      response.accumulate("device_id", this.deviceId);
      response.accumulate("beacon_event", this.beaconEvent.toJsonObject());
      response.accumulate("region", this.region.toJsonObject());
      response.accumulate("event_timestamp", this.timestamp);

    } catch (JSONException e) {

    }

    return response;

  }

}
