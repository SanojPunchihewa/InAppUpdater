package com.sanojpunchihewa.updatemanager;


import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import static com.sanojpunchihewa.updatemanager.UpdateManagerConstant.FLEXIBLE;

public class UpdateManager {

    private static UpdateManager instance;

    // Default mode is FLEXIBLE
    private int mode = FLEXIBLE;

    // Creates instance of the manager.
    private AppUpdateManager appUpdateManager;

    // Returns an intent object that you use to check for an update.
    private Task<AppUpdateInfo> appUpdateInfoTask;

    private int availableVersionCode = 0;

    public static UpdateManager Builder (){
        if(instance == null){
            instance = new UpdateManager();
        }
        return instance;
    }

    public UpdateManager mode (int mode){
        this.mode = mode;
        return this;
    }

    public void start (Activity activity){
        this.appUpdateManager =  AppUpdateManagerFactory.create(activity);
        this.appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        if(mode == FLEXIBLE) {
            setUpListener(activity);
        }
        checkUpdate(activity);
    }

    private void checkUpdate (final Activity activity){
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(mode)) {
                    // Request the update.
                    availableVersionCode = appUpdateInfo.availableVersionCode();
                    startUpdate(activity, appUpdateInfo);
                } else {
                    Log.d("LIBRARY_ZMA", "No Update available");
                }
            }
        });
    }

    private void startUpdate(Activity activity, AppUpdateInfo appUpdateInfo){
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    mode,
                    activity,
                    UpdateManagerConstant.REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

//    public static void handleResult(int requestCode, int resultCode){
//        Log.d("LIBRARY_ZMA", "Req code Update : " + requestCode);
//        if (requestCode == UpdateManagerConstant.REQUEST_CODE) {
//            Log.d("LIBRARY_ZMA", "Result code Update : " + resultCode);
//            if (resultCode != RESULT_OK) {
//                Log.d("LIBRARY_ZMA", "Update flow failed! Result code: " + resultCode);
//                // If the update is cancelled or fails,
//                // you can request to start the update again.
//            }
//        }
//    }

    private void setUpListener (final Activity activity){
        InstallStateUpdatedListener listener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdate(activity);
                }
            }
        };
        appUpdateManager.registerListener(listener);
    }

    public static void continueUpdate(final Activity activity){
        if (instance.mode == FLEXIBLE){
            continueUpdateForFlexible(activity);
        } else {
            continueUpdateForImmediate(activity);
        }
    }

    private static void continueUpdateForFlexible(final Activity activity){
        instance.appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            instance.popupSnackbarForCompleteUpdate(activity);
                        }
                    }
                });
    }

    private static void continueUpdateForImmediate(final Activity activity) {
        Log.d("LIBRARY_ZMA", "Continue Update main");
        instance.appUpdateManager
            .getAppUpdateInfo()
            .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    Log.d("LIBRARY_ZMA", "AV Update : " + appUpdateInfo.updateAvailability());
                    if (appUpdateInfo.updateAvailability()
                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        Log.d("LIBRARY_ZMA", "Continue Update");
                        // If an in-app update is already running, resume the update.
                        try {
                            instance.appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    instance.mode,
                                    activity,
                                    UpdateManagerConstant.REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }

    private void popupSnackbarForCompleteUpdate(Activity activity) {
        Snackbar snackbar =
                Snackbar.make(
                        activity.getWindow().getDecorView().findViewById(android.R.id.content),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }
}
