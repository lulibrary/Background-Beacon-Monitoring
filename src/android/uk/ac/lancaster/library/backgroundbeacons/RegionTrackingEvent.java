package uk.ac.lancaster.library.backgroundbeacons;

import uk.ac.lancaster.library.backgroundbeacons.BeaconEvent;
import uk.ac.lancaster.library.backgroundbeacons.BeaconRegion;

import org.json.JSONObject;
import org.json.JSONException;

public class RegionTrackingEvent {

  private String deviceId;
  private String eventType;
  private BeaconRegion region;
  private String timestamp;

  public RegionTrackingEvent(String deviceId, String eventType, BeaconRegion region, String timestamp) {
    this.deviceId = deviceId;
    this.eventType = eventType;
    this.region = region;
    this.timestamp = timestamp;
  }

  public JSONObject toJsonObject() {

    JSONObject response = new JSONObject();
    JSONObject region_event = new JSONObject();
    JSONObject device = new JSONObject();

    try {

      device.accumulate("uuid", this.deviceId);
      device.accumulate("timestamp", this.timestamp);

      region_event.accumulate("event_type", this.eventType);
      region_event.accumulate("device", device);
      region_event.accumulate("region", this.region.toJsonObject());

      response.accumulate("region_event", region_event);



    } catch (JSONException e) {

    }

    return response;

  }

}
