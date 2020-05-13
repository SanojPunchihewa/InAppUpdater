[![Build Status](https://travis-ci.com/SanojPunchihewa/InAppUpdater.svg?branch=master)](https://travis-ci.com/SanojPunchihewa/InAppUpdater)
[![](https://jitpack.io/v/SanojPunchihewa/InAppUpdater.svg)](https://jitpack.io/#SanojPunchihewa/InAppUpdater)
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-In%20App%20Updater-green.svg?style=flat )]( https://android-arsenal.com/details/1/7774)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/SanojPunchihewa/InAppUpdater/blob/master/LICENSE)

<div align="center">
    <img src="/images/logo.png"/>
</div>

<h1 align="center">InAppUpdater</h1>
<p align="center">Android Library to easily implement <a href="https://developer.android.com/guide/app-bundle/in-app-updates">in-app updates</a></p>

## :pencil2: Usage

### Step 1: Add it in your root build.gradle
```Gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
### Step 2: Add the dependency
```Gradle
dependencies {
    implementation 'com.github.SanojPunchihewa:InAppUpdater:1.0.5'
}
```

### Step 3: Initialize the UpdateManager
Declare the UpdateManager in your Activity
```java
    // Declare the UpdateManager
    UpdateManager mUpdateManager;
```
Initialize the UpdateManager in your `onCreate` method of the Activity
```java
    // Initialize the Update Manager with the Activity and the Update Mode
    mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.FLEXIBLE);
    mUpdateManager.start();
```

**Update Mode**

There are two modes
* Flexible(`UpdateManagerConstant.FLEXIBLE`) *(default)* - User can use the app during update download, installation and restart needs to be triggered by user

* Immediate(`UpdateManagerConstant.IMMEDIATE`) - User will be blocked until download and installation is finished, restart is triggered automatically

**Additionally** you can get the Available Version Code of the update and the Number of days passed since the user was notified of an update through the Google Play. You can find these codes in the [demo app](/app/src/main/java/com/zanojmobiapps/inappupdatedemoapp/MainActivity.java)

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

**Monitoring the flexible update download progres**

You can monitor the download progress of a Flexible Update using this callback.
***Note***: This is only available for Flexible update mode. You can find more from the [official doc](https://developer.android.com/guide/playcore/in-app-updates#monitor_flexible)
```java
// Callback from Flexible Update Progress
mUpdateManager.addFlexibleUpdateDownloadListener(new FlexibleUpdateDownloadListener() {
    @Override
    public void onDownloadProgress(final long bytesDownloaded, final long totalBytes) {
       // Show a progress bar or anything you want
    }
});
```

## :movie_camera: Demo
Flexible Update             |  Immediate Update
:-------------------------:|:-------------------------:
![](/images/gif/flexible_update.gif)  |  ![](/images/gif/immediate_update.gif)

## :exclamation: Troubleshoot
- In-app updates works only with devices running **Android 5.0 (API level 21) or higher**
- In-app updates are available only to user accounts that own the app. So, **make sure the account you’re using has downloaded your app from Google Play at least once before using the account to test in-app updates.**
- Make sure that the app that you are testing in-app updates with has the **same application ID and is signed with the same signing key** as the one available from Google Play.
- Because Google Play can only update an app to a higher version code, make sure the app you are **testing as a lower version code than the update version code.**

You can find more information at [Frequently Asked Questions](FAQ.md)

## :open_hands: Contributions
Any contributions are welcome!

## :page_facing_up: License
```
MIT License

Copyright (c) 2019 Sanoj Punchihewa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
