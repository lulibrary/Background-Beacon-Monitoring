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
    JSONObject beacon_event = new JSONObject();
    JSONObject device = new JSONObject();

    try {

      device.accumulate("uuid", this.deviceId);
      device.accumulate("timestamp", this.timestamp);
      beacon_event.accumulate("device", device);
      beacon_event.accumulate("region", this.region.toJsonObject());
      beacon_event.accumulate("beacon", this.beaconEvent.toJsonObject());

      response.accumulate("beacon_event", beacon_event);



    } catch (JSONException e) {

    }

    return response;

  }

}
