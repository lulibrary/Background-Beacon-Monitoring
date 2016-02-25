package uk.ac.lancaster.library.backgroundbeacons;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import uk.ac.lancaster.library.backgroundbeacons.SharedPreferencesUtility;

public class BackgroundBeaconManager extends CordovaPlugin {

  private SharedPreferencesUtility settings;

  public BackgroundBeaconManager() {

  }

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    this.settings = new SharedPreferencesUtility(this.cordova.getActivity().getApplicationContext());
  }

  public boolean execute(String action, JSONArray args, CallbackContext callbackContent) throws JSONException {

    if (action.equals("createService")) {

      if (!this.settings.exist()) {

        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Setting preferences");

        this.settings.setApiToken(args.getString(0));
        this.settings.setApiUser(args.getString(1));
        this.settings.setDeviceId(args.getString(2));
      } else {
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Preferences already exist");
      }

      Log.d("uk.ac.lancaster.library.backgroundbeacons", "Starting intent service");

      Intent startServiceIntent = new Intent(this.cordova.getActivity().getApplicationContext(), BackgroundBeaconService.class);
      this.cordova.getActivity().getApplicationContext().startService(startServiceIntent);

    } else {
      return false;
    }

    return true;

  }

}
