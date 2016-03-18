package uk.ac.lancaster.library.backgroundbeacons;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class SharedPreferencesUtility {

  public static final String PREFERENCES_NAME = "BACKGROUND_BEACONS_PREFS";

  public static final String API_TOKEN = "API_TOKEN";
  public static final String API_USER = "API_USER";
  public static final String DEVICE_ID = "DEVICE_ID";
  public static final String API_URL = "API_URL";
  public static final String API_VERSION = "API_VERSION";
  public static final String SEND_MOVEMENT_DATA = "SEND_MOVEMENT_DATA";

  private Context context;
  private SharedPreferences settings;
  private Editor editor;

  public SharedPreferencesUtility(Context c) {
    super();
    this.context = c;
    this.settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    this.editor = this.settings.edit();
  }

  public void setApiToken(String text) {
    this.editor.putString(API_TOKEN, text);
    this.editor.commit();
  }

  public void setApiUser(String text) {
    this.editor.putString(API_USER, text);
    this.editor.commit();
  }

  public void setDeviceId(String text) {
    this.editor.putString(DEVICE_ID, text);
    this.editor.commit();
  }

  public void setApiUrl(String text) {
    this.editor.putString(API_URL, text);
    this.editor.commit();
  }

  public void setApiVersion(String text) {
    this.editor.putString(API_VERSION, text);
    this.editor.commit();
  }

  public void setSendMovementData(Boolean sendMovementData) {
    Log.d("uk.ac.lancs.library.myjourenys", "Movement Data: " + sendMovementData);
    this.editor.putBoolean(SEND_MOVEMENT_DATA, sendMovementData);
    this.editor.commit();
  }

  public String getApiToken() {
    return this.settings.getString(API_TOKEN, null);
  }

  public String getApiUser() {
    return this.settings.getString(API_USER, null);
  }

  public String getDeviceId() {
    return this.settings.getString(DEVICE_ID, null);
  }

  public String getApiUrl() {
    return this.settings.getString(API_URL, null);
  }

  public String getApiVersion() {
    return this.settings.getString(API_VERSION, null);
  }

  public boolean getSendMovementData() {
    return this.settings.getBoolean(SEND_MOVEMENT_DATA, false);
  }

  public boolean exist() {

    if (!this.settings.contains(API_TOKEN)) {
      return false;
    }

    if (!this.settings.contains(API_USER)) {
      return false;
    }

    if (!this.settings.contains(DEVICE_ID)) {
      return false;
    }

    if (!this.settings.contains(API_VERSION)) {
      return false;
    }

    if (!this.settings.contains(API_URL)) {
      return false;
    }

    if(!this.settings.contains(SEND_MOVEMENT_DATA)) {
      return false;
    }

    return true;

  }

}
