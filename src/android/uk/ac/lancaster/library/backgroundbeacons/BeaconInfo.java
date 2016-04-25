package uk.ac.lancaster.library.backgroundbeacons;

public class BeaconInfo {

  private String uuid;
  private String major;
  private String minor;

  public BeaconInfo(String uuid, String major, String minor) {
    this.uuid = uuid;
    this.major = major;
    this.minor = minor;
  }

  public String getUuid() {
    return this.uuid;
  }

  public String getMajor() {
    return this.major;
  }

  public String getMinor() {
    return this.minor;
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
