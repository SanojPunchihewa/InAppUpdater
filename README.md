[![Build Status](https://travis-ci.com/SanojPunchihewa/InAppUpdater.svg?branch=master)](https://travis-ci.com/SanojPunchihewa/InAppUpdater)
[![](https://jitpack.io/v/SanojPunchihewa/InAppUpdater.svg)](https://jitpack.io/#SanojPunchihewa/InAppUpdater)

# InAppUpdater
Android Library to easily implement in-app updates

# Usage

## Add to project

Step 1: Add it in your root build.gradle at the end of repositories
```Gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Step 2: Add the dependency
```Gradle
dependencies {
    implementation 'com.github.SanojPunchihewa:InAppUpdater:1.0.2'
}
```

## Step 1: Initialize the UpdateManager
Initialize the UpdateManager in your `onCreate` method of the Activity
```java
    UpdateManager.Builder().mode(UpdateManagerConstant.IMMEDIATE).start(this);
```

## Update Mode
There are two modes
* Flexible *(default)* - User can use the app during update download, installation and restart needs to be triggered by user

* Immediate - User will be blocked until download and installation is finished, restart is triggered automatically


#### Set the update mode
```java
     UpdateManager.Builder().mode(UpdateManagerConstant.IMMEDIATE)
```

## Step 2: Resume the updates
Call `continueUpdate` method in your `onResume` method to install waiting updates
```java
@Override
protected void onResume() {
    super.onResume();
    UpdateManager.continueUpdate(this);
}
```

