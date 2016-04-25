package uk.ac.lancaster.library.backgroundbeacons;

import uk.ac.lancaster.library.backgroundbeacons.BeaconInfo;
import org.json.JSONObject;
import org.json.JSONException;

public class BeaconEvent {

  private BeaconInfo beacon;
  private String proximity;
  private String accuracy;
  private String rssi;

  public BeaconEvent(BeaconInfo beacon, String proximity, String accuracy, String rssi) {
    this.beacon = beacon;
    this.proximity = proximity;
    this.accuracy = accuracy;
    this.rssi = rssi;
  }

  public JSONObject toJsonObject() {

    JSONObject response = new JSONObject();

    try {

      if (this.beacon.getUuid() != null) {
        response.accumulate("uuid", this.beacon.getUuid());
      }

      if (this.beacon.getMajor() != null) {
        response.accumulate("major", this.beacon.getMajor());
      }

      if (this.beacon.getMinor() != null) {
        response.accumulate("minor", this.beacon.getMinor());
      }

      if (this.proximity != null) {
        response.accumulate("proximity", this.proximity);
      }

      if (this.accuracy != null) {
        response.accumulate("distance", this.accuracy);
      }

      if (this.rssi != null) {
        response.accumulate("rssi", this.rssi);
      }

    } catch (JSONException e) {

    }

    return response;

  }

}
