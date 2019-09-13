# Frequently asked questions

### 1. The app downloads the update but it won't install the update?

<img src="/images/gif/update-failed-install.gif" width="180">

This happen when the app that you are testing in-app updates is **not signed with the same signing key** as the one available from 
Google Play.The reason is android OS is not able to install the update as the app that you are testing is not signed.(ie using the debug 
version)
#### Fix

Generate a signed apk using the same signing key(key used to sign the app uploaded to Google Play) and install it in your device. This will 
download and install the update.

Generate a signed apk            |  Use the same signing key
:-------------------------:|:-------------------------:
<img src="/images/step2.jpg" width="250">  |  <img src="/images/step3.jpg" width="250">
