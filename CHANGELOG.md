# Changelog
All notable changes to InAppUpdater will be documented in this file.

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
