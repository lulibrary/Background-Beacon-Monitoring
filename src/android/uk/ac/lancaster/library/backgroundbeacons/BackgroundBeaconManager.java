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
import android.Manifest;
import android.content.pm.PackageManager;

public class BackgroundBeaconManager extends CordovaPlugin {

  private SharedPreferencesUtility settings;
  private BackgroundBeaconService backgroundBeaconService;
  private CallbackContext callbackContext;
  boolean serviceBound = false;
  public static final String [] PERMISSIONS = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };

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

        this.settings.setApiParticipantToken(args.getString(0));
        this.settings.setApiParticpantEmail(args.getString(1));
        this.settings.setDeviceId(args.getString(2));
        this.settings.setApiUrl(args.getString(3));
        this.settings.setApiVersion(args.getString(4));
        this.settings.setSendMovementData(args.getBoolean(5));
      } else {
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Preferences already exist");
      }

      Log.d("uk.ac.lancaster.library.backgroundbeacons", "Starting intent service");

      Intent startServiceIntent = new Intent(this.cordova.getActivity().getApplicationContext(), BackgroundBeaconService.class);
      this.cordova.getActivity().getApplicationContext().bindService(startServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
      this.cordova.getActivity().getApplicationContext().startService(startServiceIntent);

      callbackContent.success();

    } else if (action.equals("startMonitoringRegion")) {
      Log.d("uk.ac.lancaster.library.backgroundbeacons", "Started monitoring region");
      if(serviceBound) {

        Integer major;
        Integer minor;

        if (args.getString(2).isEmpty()) {
          major = null;
        } else {
          major = args.getInt(2);
        }

        if (args.getString(3).isEmpty()) {
          minor = null;
        } else {
          minor = args.getInt(3);
        }

        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Major: " + major);
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Minor: " + minor);

        backgroundBeaconService.startMonitoringRegion(args.getString(0), args.getString(1), major, minor);
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Service bound and starting monitoring region called");
        callbackContent.success();
      } else {
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Service not bound");
        callbackContent.error("SERVICE NOT BOUND");
      }

    } else if (action.equals("stopMonitoringRegion")) {

      Log.d("uk.ac.lancaster.library.backgroundbeacons", "Stop monitoring region");
      if(serviceBound) {
        backgroundBeaconService.stopMonitoringRegion(args.getString(0));
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Service bound and stop monitoring region called");
        callbackContent.success();
      } else {
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Service not bound");
        callbackContent.error("SERVICE NOT BOUND");
      }

    } else if (action.equals("startRangingRegion")) {

      Log.d("uk.ac.lancaster.library.backgroundbeacons", "Started ranging region");
      if(serviceBound) {

        Integer major;
        Integer minor;

        if (args.getString(2).isEmpty()) {
          major = null;
        } else {
          major = args.getInt(2);
        }

        if (args.getString(3).isEmpty()) {
          minor = null;
        } else {
          minor = args.getInt(3);
        }

        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Major: " + major);
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Minor: " + minor);

        backgroundBeaconService.startRangingRegion(args.getString(0), args.getString(1), major, minor);
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Service bound and starting ranging region called");
        callbackContent.success();
      } else {
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Service not bound");
        callbackContent.error("SERVICE NOT BOUND");
      }

    } else if (action.equals("stopRangingRegion")) {

      Log.d("uk.ac.lancaster.library.backgroundbeacons", "Stop Ranging region");
      if(serviceBound) {
        backgroundBeaconService.stopRangingRegion(args.getString(0));
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Service bound and stop ranging region called");
        callbackContent.success();
      } else {
        Log.d("uk.ac.lancaster.library.backgroundbeacons", "Service not bound");
        callbackContent.error("SERVICE NOT BOUND");
      }

    } else if (action.equals("setMovementPreference")) {
      Log.d("uk.ac.lancaster.library.myjourneys", "Passed in arg: " + args.getBoolean(0));
      backgroundBeaconService.setMovementPreference(args.getBoolean(0));
    } else if (action.equals("requestPermissions")) {
      this.callbackContext = callbackContent;
      cordova.requestPermissions(this, 0, PERMISSIONS);
    } else {
      callbackContent.error("UNKNOWN OPERATION");
      return false;
    }

    return true;

  }

  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {

    Log.d("uk.ac.lancaster.library.myjourneys", "Grant Results length: " + grantResults.length);
    Log.d("uk.ac.lancaster.library.myjourneys", "request code: " + requestCode);

    switch(requestCode) {

      case 0:

        if (grantResults.length != 2) {
          Log.d("uk.ac.lancaster.library.myjourneys", "PERMISSION CHECK FAILED");
          this.callbackContext.error("PERMISSION CHECK FAILED");
        } else {

          // Both permission grant results need to be denied for permissions to have both been denied.
          // The plugin only requires one of COARSE_LOCATION or FINE_LOCATION to access iBeacon information.

          if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
            Log.d("uk.ac.lancaster.library.myjourneys", "BOTH PERMISSIONS DENIED");
            this.callbackContext.error("PERMISSION DENIED");
            return;
          }

          Log.d("uk.ac.lancaster.library.myjourneys", "PERMISSIONS NOT DENIED");

          this.callbackContext.success();
        }

        break;

    }

  }

  private ServiceConnection serviceConnection = new ServiceConnection() {

    public void onServiceConnected(ComponentName className, IBinder service) {
      LocalBinder binder = (LocalBinder) service;
      backgroundBeaconService = binder.getService();
      serviceBound = true;
    }

    public void onServiceDisconnected(ComponentName arg0) {
      serviceBound = false;
    }

  };

}
