var exec = require('cordova/exec');

function BackgroundBeaconMonitoring() {

}

BackgroundBeaconMonitoring.prototype.startService = function (api_token, api_user, device_id, successCallback, errorCallback) {
  exec(successCallback, errorCallback, "BackgroundBeaconManager", "createService", [api_token, api_user, device_id]);
};

var backgroundBeaconMonitoring = new BackgroundBeaconMonitoring();

module.exports = backgroundBeaconMonitoring;
