package uk.ac.lancaster.library.backgroundbeacons;

import org.json.JSONObject;
import org.json.JSONException;

public class BeaconInfo {

  private String uuid;
  private String major;
  private String minor;

  public BeaconInfo(String uuid, String major, String minor) {
    this.uuid = uuid;
    this.major = major;
    this.minor = minor;
  }

  public JSONObject toJsonObject() {
    JSONObject response = new JSONObject();

    try {

      response.accumulate("uuid", this.uuid);
      response.accumulate("major", this.major);
      response.accumulate("minor", this.minor);

    } catch (JSONException e) {

    }

    return response;
  }

  public boolean isEmpty() {

    if (this.uuid != null) {
      return false;
    }

    if (this.major != null) {
      return false;
    }

    if (this.minor != null) {
      return false;
    }

    return true;

  }

}
