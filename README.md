## Background Beacon Monitoring

Cordova plugin allowing background beacon monitoring and ranging on Android. Uses the [altbeacon](http://altbeacon.github.io/) library for providing the API to interact with beacons.

This plugin is developed specifically for the Library Journeys research project at Lancaster University Library. As such much of the functionality and API endpoints that this plugin sends data to are related to this.

The altbeacon library jar is provided in `libs/android` directory.

## Installation

Installing the plugin is as simple as adding the plugin to cordova using the plugin add command.

`cordova plugin add https://github.com/lulibrary/Background-Beacon-Monitoring.git`

To install the plugin when using the Ionic framework the following command will add the plugin and save it to package.json.

`ionic plugin add https://github.com/lulibrary/Background-Beacon-Monitoring.git`

As https://github.com/driftyco/ionic-cli/issues/359 is still a re-occuring issue (even though its closed) you may need to do the following to see it referenced in package.json

If this is not referenced in package.json in the cordovaPlugins array run the following to save all current plugins to the package.json.

`ionic state save`

Currently the altbeacon jar file is not linked when the plugin is installed due to a clash if the cordova-plugin-ibeacon is installed as this also uses the altbeacon library for Android (currently working on fixing this). If this is only being used then uncomment the following line in plugin.xml, fix for this is being worked on.

`<!-- <source-file src="libs/android/altbeacon.jar" target-dir="libs" framework="true" /> -->`

## Usage

Once the plugin is added it is available for use as `BackgroundBeaconMonitoring`

In order to access the location information and details of the beacons detected the plugin requires either of `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. The app requests both of these permissions from the user through the `BackgroundBeaconMonitoring.requestPermissions()` method.

After the user has given permission to access their location information the service can be started using the `BackgroundBeaconMonitoring.startService()` method.

## API Reference

#### requestPermissions()

`BackgroundBeaconMonitoring.requestPermissions(successCallback, errorCallback)`

Requests the permissions needed to access the users location and therefore the beacon information. The success and error callbacks are optional.

| Parameter | Description |
| --- | --- |
| successCallback | [Optional] Callback function to run when the method returns successfully |
| errorCallback | [Optional] Callback function to run when the method returns an error, the function can accept a parameter to hold the message returned from the native layer so any errors encountered eg. 'PERMISSION DENIED' can be dealt with in the calling application appropriately |

#### startService()

`BackgroundBeaconMonitoring.startService(api_participant_token, api_participant_email, device_id, api_url, api_version, sendMovementData, successCallback, errorCallback)`

Starts the background service and sets the location and access details for the API.

| Parameter | Description |
| --- | --- |
| api_participant_token | The participants access token for the Library Journeys API |
| api_participant_email | The participants email linked to the api_participant_token for the Library Journeys API |
| device_id | The device id of the participants mobile device. For consistency with requests sent within the app this is generated using `$cordovaDevice.getUUID()` in ionic/cordova |
| api_url | The base URL for the Library Journeys API generally of the format `https://hostname/api/` |
| api_version | The version of the api to use i.e. `v2` |
| sendMovementData | Whether to actually send the interactions with the beacons to the Library Journeys API or not |
| successCallback | Callback function to run when the method returns successfully |
| errorCallback | Callback function to run when the method returns an error |


#### startMonitoringRegion()

`BackgroundBeaconMonitoring.startMonitoringRegion(identifier, uuid, major, minor, successCallback, errorCallback)`

Starts monitoring the region specified by the uuid, major and minor and labels it with the unique identifier.

| Parameter | Description |
| --- | --- |
| identifier | The unique identifier/name of the region to start monitoring |
| uuid | The uuid of the region to monitor |
| major | The major id number of the region to monitor - can be null so monitors all beacons for the uuid |
| minor | The minor id number of the region to monitor - can be null and will scan for all beacons with uuid and major if major is specified, If major is null then this value is ignored |
| successCallback | Callback function to run when the method returns successfully |
| errorCallback | callback function to run when the method returns an error |

#### stopMonitoringRegion()

`BackgroundBeaconMonitoring.stopMonitoringRegion(identifier, successCallback, errorCallback)`

Stop monitoring the region specified by identifier, this is the same unique identifier used in the `startMonitoringRegion()` method.

| Parameter | Description |
| --- | --- |
| identifier | The unique identifier/name of the region to stop monitoring, this is the same one as used to start monitoring |
| successCallback | Callback function to run when the method returns successfully |
| errorCallback | Callback function to run when the method returns an error |

#### startRangingRegion()

`BackgroundBeaconMonitoring.startRangingRegion(identifier, uuid, major, minor, successCallback, errorCallback)`

Starts ranging the region specified by the uuid, major and minor and labels it with the unique identifier. The unique identifier can be the same as one used with associated uuid/major/minor in `startMonitoringRegion()` to range the region as well.

| Parameter | Description |
| --- | --- |
| identifier | The unique identifier/name of the region to start ranging |
| uuid | The uuid of the region to range |
| major | The major id number of the region to range - can be null so monitors all beacons for the uuid |
| minor | The minor id number of the region to range - can be null and will scan for all beacons with uuid and major if major is specified, If major is null then this value is ignored |
| successCallback | Callback function to run when the method returns successfully |
| errorCallback | callback function to run when the method returns an error |

#### stopRangingRegion()

`BackgroundBeaconMonitoring.stopRangingRegion(identifier, successCallback, errorCallback)`

Stop ranging the region specified by identifier, this is the same unique identifier used in the `startRangingRegion()` method.

| Parameter | Description |
| --- | --- |
| identifier | The unique identifier/name of the region to stop ranging, this is the same one as used to start monitoring |
| successCallback | Callback function to run when the method returns successfully |
| errorCallback | Callback function to run when the method returns an error |

#### setMovementPreference()

`BackgroundBeaconMonitoring.setMovementPreference(preference, successCallback, errorCallback)`

Changes the movement preference which controls whether the service sends interactions with the beacons to the Library Journeys API, allows the user of the application to control the privacy of their movements.

| Parameter | Description |
| --- | --- |
| preference | True/False Boolean Parameter to change whether the service sends to the API |
| successCallback | Callback function to run when the method returns successfully |
| errorCallback | Callback function to run when the method returns an error |

## Contributors

Contributions welcome by pull request.

## License

Apache License v2.0
