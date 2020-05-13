# Changelog
All notable changes to InAppUpdater will be documented in this file.

## [1.0.5] - 2020-05-13
- Add progress Listener to Flexible Update
- Add listener to return the number of staleness days after an update is notified
  Above features were added with the new play core library update 1.7.2
- Use Lifecycle observer to resume updates. No need to call `continueUpdate` in your activities
- You need to use UpdateInfoListener instead of onVersionCheckListener
```java
mUpdateManager.addUpdateInfoListener(new UpdateInfoListener() {
    @Override
    public void onReceiveVersionCode(final int code) {
        // You can get the available version code of the apk in Google Play
        // Do something here
    }

    @Override
    public void onReceiveStalenessDays(final int days) {
        // Number of days passed since the user was notified of an update through the Google Play
        // If the user hasn't notified this will return -1 as days
        // You can decide the type of update you want to call
    }
});
```

## [1.0.5-alpha.1] - 2019-10-06
- Add Lifecycle observer to unregister the update state listener

## [1.0.4] - 2019-09-22
- Fix fading of Material TextInputLayout

## [1.0.4-alpha.2] - 2019-09-07
- Fix Send Intent Exception

## [1.0.4-alpha.1] - 2019-07-29
- Fix Jitpack release issue

## [1.0.3] - 2019-07-27
### Added
- Method to get the Version code of the Available Update

### Changed
- Pass the Activity to the `Builder()` instead of `start()`
```java
mUpdateManager = UpdateManager.Builder(this);
```
- No need to pass Activity to `continueUpdate()`

You can find these codes in the [demo app](/app/src/main/java/com/zanojmobiapps/inappupdatedemoapp/MainActivity.java)

## [1.0.3-alpha.1] - 2019-07-25
### Added
- Debug Logs to the UpdateManager

## [1.0.2] - 2019-07-16

## [1.0.1] - 2019-07-16
### Changed
- Package name

## [v1.0] - 2019-07-16
:tada: Initial Release
