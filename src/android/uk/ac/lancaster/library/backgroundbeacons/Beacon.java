package uk.ac.lancaster.library.backgroundbeacons;

import org.json.JSONObject;

public class Beacon {

  private String uuid;
  private String major;
  private String minor;

  public Beacon(String uuid, String major, String minor) {
    this.uuid = uuid;
    this.major = major;
    this.minor = minor;
  }

  public JSONObject toJson() {
    JSONObject response = new JSONObject();
    response.accumulate("uuid", this.uuid);
    response.accumulate("major", this.major);
    response.accumulate("minor", this.minor);
    return response;
  }

}
