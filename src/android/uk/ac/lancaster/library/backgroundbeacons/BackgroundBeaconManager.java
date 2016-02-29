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
import uk.ac.lancaster.library.backgroundbeacons.BackgroundBeaconService;
import uk.ac.lancaster.library.backgroundbeacons.BackgroundBeaconService.LocalBinder;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;

public class BackgroundBeaconManager extends CordovaPlugin {

  private SharedPreferencesUtility settings;
  private BackgroundBeaconService backgroundBeaconService;
  boolean mBound = false;

  public BackgroundBeaconManager() {

  }

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    this.settings = new SharedPreferencesUtility(this.cordova.getActivity().getApplicationContext());

    Log.d("uk.ac.lancaster.library.backgroundbeacons", "Starting intent service");

    Intent startServiceIntent = new Intent(this.cordova.getActivity().getApplicationContext(), BackgroundBeaconService.class);
    this.cordova.getActivity().getApplicationContext().bindService(startServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
    this.cordova.getActivity().getApplicationContext().startService(startServiceIntent);
  }

  public boolean execute(String action, JSONArray args, CallbackContext callbackContent) throws JSONException {

    if (action.equals("createService")) {

      if (!this.settings.exist()) {

        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Setting preferences");

        this.settings.setApiToken(args.getString(0));
        this.settings.setApiUser(args.getString(1));
        this.settings.setDeviceId(args.getString(2));
        this.settings.setApiUrl(args.getString(3));
        this.settings.setApiVersion(args.getString(4));
      } else {
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Preferences already exist");
      }

      if (mBound) {
        backgroundBeaconService.testBinding();
      }

    } else {
      return false;
    }

    return true;

  }

  private ServiceConnection mConnection = new ServiceConnection() {

    public void onServiceConnected(ComponentName className, IBinder service) {
      LocalBinder binder = (LocalBinder) service;
      backgroundBeaconService = binder.getService();
      mBound = true;
    }

    public void onServiceDisconnected(ComponentName arg0) {
      mBound = false;
    }

  };

}
