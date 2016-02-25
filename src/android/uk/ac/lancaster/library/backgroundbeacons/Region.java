package uk.ac.lancaster.library.backgroundbeacons;

import org.json.JSONObject;

public class Region {

  private String identitifer;
  private String uuid;
  private String major;
  private String minor;

  public Region(String identifier, String uuid, String major, String minor) {
    this.identifier = identifier;
    this.uuid = uuid;
    this.major = major;
    this.minor = minor;
  }

  public JSONObject toJson() {
    JSONObject response = new JSONObject();
    response.accumulate("identifier", this.identifier);
    response.accumulate("uuid", this.uuid);
    response.accumulate("major", this.major);
    response.accumulate("minor", this.minor);
    return response;
  }

}
