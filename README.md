# InAppUpdater
Android Library to easily implement in-app updates

# Usage

## Add to project


Add it to your build.gradle with:
```Gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and:
```Gradle
dependencies {
    compile 'com.github.SanojPunchihewa:InAppUpdater:{latest version}'
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

