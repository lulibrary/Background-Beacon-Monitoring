package uk.ac.lancaster.library.backgroundbeacons;

import uk.ac.lancaster.library.backgroundbeacons.Beacon;
import org.json.JSONObject;

public class BeaconEvent {

  private String eventType;
  private Beacon beacon;
  private String proximity;
  private String accuracy;
  private String rssi;

  public BeaconEvent(String eventType, Beacon beacon, String proximity, String accuracy, String rssi) {
    this.eventType = eventType;
    this.beacon = beacon;
    this.proximity = proximity;
    this.accuracy = accuracy;
    this.rssi = rssi;
  }

  public JSONObject toJsonObject() {

    JSONObject response = new JSONObject();

    response.accumulate("eventType", this.eventType);
    response.accumulate("beacon", this.beacon.toJson().toString());
    response.accumulate("proximity", this.proximity);
    response.accumulate("accuracy", this.accuracy);
    response.accumulate("rssi", this.rssi);

    return response;

  }

}
