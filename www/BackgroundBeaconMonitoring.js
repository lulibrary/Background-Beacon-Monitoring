var exec = require('cordova/exec');

function BackgroundBeaconMonitoring() {

}

BackgroundBeaconMonitoring.prototype.startService = function (api_token, api_user, device_id, api_url, api_version, successCallback, errorCallback) {
  exec(successCallback, errorCallback, "BackgroundBeaconManager", "createService", [api_token, api_user, device_id, api_url, api_version]);
};

var backgroundBeaconMonitoring = new BackgroundBeaconMonitoring();

module.exports = backgroundBeaconMonitoring;
