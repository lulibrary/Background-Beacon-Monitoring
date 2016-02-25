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
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "Before put string");
    this.editor.putString(DEVICE_ID, text);
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "After put string");
    this.editor.commit();
    Log.d("uk.ac.lancaster.library.backgroundbeacons", "After commit");
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

    return true;

  }

}
