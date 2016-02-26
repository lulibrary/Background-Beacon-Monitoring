package uk.ac.lancaster.library.backgroundbeacons;

import uk.ac.lancaster.library.backgroundbeacons.BeaconInfo;
import org.json.JSONObject;
import org.json.JSONException;

public class BeaconEvent {

  private String eventType;
  private BeaconInfo beacon;
  private String proximity;
  private String accuracy;
  private String rssi;

  public BeaconEvent(String eventType, BeaconInfo beacon, String proximity, String accuracy, String rssi) {
    this.eventType = eventType;
    this.beacon = beacon;
    this.proximity = proximity;
    this.accuracy = accuracy;
    this.rssi = rssi;
  }

  public JSONObject toJsonObject() {

    JSONObject response = new JSONObject();

    try {

      response.accumulate("eventType", this.eventType);

      if (!this.beacon.isEmpty()) {
        response.accumulate("beacon", this.beacon.toJsonObject());
      }

      if (this.proximity != null) {
        response.accumulate("proximity", this.proximity);
      }

      if (this.accuracy != null) {
        response.accumulate("accuracy", this.accuracy);
      }

      if (this.rssi != null) {
        response.accumulate("rssi", this.rssi);
      }

    } catch (JSONException e) {

    }

    return response;

  }

}
