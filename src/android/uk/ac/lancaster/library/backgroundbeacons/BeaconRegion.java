package uk.ac.lancaster.library.backgroundbeacons;

import org.json.JSONObject;

import org.json.JSONException;

public class BeaconRegion {

  private String identifier;
  private String uuid;
  private String major;
  private String minor;

  public BeaconRegion(String identifier, String uuid, String major, String minor) {
    this.identifier = identifier;
    this.uuid = uuid;
    this.major = major;
    this.minor = minor;
  }

  public JSONObject toJsonObject() {
    JSONObject response = new JSONObject();

    try {

      response.accumulate("identifier", this.identifier);
      response.accumulate("uuid", this.uuid);
      response.accumulate("major", this.major);
      response.accumulate("minor", this.minor);

    } catch (JSONException e) {

    }

    return response;
  }

}
