[![Build Status](https://travis-ci.com/SanojPunchihewa/InAppUpdater.svg?branch=master)](https://travis-ci.com/SanojPunchihewa/InAppUpdater)
[![](https://jitpack.io/v/SanojPunchihewa/InAppUpdater.svg)](https://jitpack.io/#SanojPunchihewa/InAppUpdater)
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-In%20App%20Updater-green.svg?style=flat )]( https://android-arsenal.com/details/1/7774)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/SanojPunchihewa/InAppUpdater/blob/master/LICENSE)

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

#### Update Mode
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
## Contributions
Any contributions are welcome!

## License
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
