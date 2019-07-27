# Changelog
All notable changes to InAppUpdater will be documented in this file.

## [Unreleased]
### Added
- Method to get the Version code of the Available Update

### Changed
- Pass the Activity to the `Builder()` instead of `start()`
```java
mUpdateManager = UpdateManager.Builder(this);
```
- No need to pass Activity to `continueUpdate()`
## [1.0.3-alpha.1] - 2019-07-25
### Added
- Debug Logs to the UpdateManager

## [1.0.2] - 2019-07-16

## [1.0.1] - 2019-07-16
### Changed
- Package name

## [v1.0] - 2019-07-16
:tada: Initial Release