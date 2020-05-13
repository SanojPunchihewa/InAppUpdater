package com.sanojpunchihewa.updatemanager;


import static com.sanojpunchihewa.updatemanager.UpdateManagerConstant.FLEXIBLE;

import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
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
import java.lang.ref.WeakReference;

public class UpdateManager implements LifecycleObserver {

    private static final String TAG = "InAppUpdateManager";

    private WeakReference<AppCompatActivity> mActivityWeakReference;

    private static UpdateManager instance;

    // Default mode is FLEXIBLE
    private int mode = FLEXIBLE;

    // Creates instance of the manager.
    private AppUpdateManager appUpdateManager;

    // Returns an intent object that you use to check for an update.
    private Task<AppUpdateInfo> appUpdateInfoTask;

    private FlexibleUpdateDownloadListener flexibleUpdateDownloadListener;

    private UpdateManager(AppCompatActivity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
        this.appUpdateManager = AppUpdateManagerFactory.create(getActivity());
        this.appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        activity.getLifecycle().addObserver(this);
    }

    public static UpdateManager Builder(AppCompatActivity activity) {
        if (instance == null) {
            instance = new UpdateManager(activity);
        }
        Log.d(TAG, "Instance created");
        return instance;
    }

    public UpdateManager mode(int mode) {
        String strMode = mode == FLEXIBLE ? "FLEXIBLE" : "IMMEDIATE";
        Log.d(TAG, "Set update mode to : " + strMode);
        this.mode = mode;
        return this;
    }

    public void start() {
        if (mode == FLEXIBLE) {
            setUpListener();
        }
        checkUpdate();
    }

    private void checkUpdate() {
        // Checks that the platform will allow the specified type of update.
        Log.d(TAG, "Checking for updates");
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(mode)) {
                    // Request the update.
                    Log.d(TAG, "Update available");
                    startUpdate(appUpdateInfo);
                } else {
                    Log.d(TAG, "No Update available");
                }
            }
        });
    }

    private void startUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            Log.d(TAG, "Starting update");
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    mode,
                    getActivity(),
                    UpdateManagerConstant.REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            Log.d(TAG, "" + e.getMessage());
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

    private InstallStateUpdatedListener listener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState installState) {
            if (installState.installStatus() == InstallStatus.DOWNLOADING) {
                long bytesDownloaded = installState.bytesDownloaded();
                long totalBytesToDownload = installState.totalBytesToDownload();
                if (flexibleUpdateDownloadListener != null) {
                    flexibleUpdateDownloadListener.onDownloadProgress(bytesDownloaded, totalBytesToDownload);
                }
            }
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                Log.d(TAG, "An update has been downloaded");
                popupSnackbarForCompleteUpdate();
            }
        }
    };

    private void setUpListener() {
        appUpdateManager.registerListener(listener);
    }

    private void continueUpdate() {

        if (instance.mode == FLEXIBLE) {
            continueUpdateForFlexible();
        } else {
            continueUpdateForImmediate();
        }
    }

    private void continueUpdateForFlexible() {
        instance.appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            Log.d(TAG, "An update has been downloaded");
                            instance.popupSnackbarForCompleteUpdate();
                        }
                    }
                });
    }

    private void continueUpdateForImmediate() {
        instance.appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        if (appUpdateInfo.updateAvailability()
                                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                            // If an in-app update is already running, resume the update.
                            try {
                                instance.appUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        instance.mode,
                                        getActivity(),
                                        UpdateManagerConstant.REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                Log.d(TAG, "" + e.getMessage());
                            }
                        }
                    }
                });
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
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

    public void addUpdateInfoListener(final UpdateInfoListener updateInfoListener) {
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    // Request the update.
                    Log.d(TAG, "Update available");
                    int availableVersionCode = appUpdateInfo.availableVersionCode();
                    int stalenessDays = appUpdateInfo.clientVersionStalenessDays() != null ? appUpdateInfo
                            .clientVersionStalenessDays() : -1;
                    updateInfoListener.onReceiveVersionCode(availableVersionCode);
                    updateInfoListener.onReceiveStalenessDays(stalenessDays);
                } else {
                    Log.d(TAG, "No Update available");
                }
            }
        });
    }

    public void addFlexibleUpdateDownloadListener(FlexibleUpdateDownloadListener flexibleUpdateDownloadListener) {
        this.flexibleUpdateDownloadListener = flexibleUpdateDownloadListener;
    }

    private Activity getActivity() {
        return mActivityWeakReference.get();
    }

    private void unregisterListener() {
        if (appUpdateManager != null && listener != null) {
            appUpdateManager.unregisterListener(listener);
            Log.d(TAG, "Unregistered the install state listener");
        }
    }

    public interface UpdateInfoListener {

        void onReceiveVersionCode(int code);

        void onReceiveStalenessDays(int days);
    }

    public interface FlexibleUpdateDownloadListener {

        void onDownloadProgress(long bytesDownloaded, long totalBytes);

    }

    @OnLifecycleEvent(Event.ON_RESUME)
    private void onResume() {
        continueUpdate();
    }

    @OnLifecycleEvent(Event.ON_DESTROY)
    private void onDestroy() {
        unregisterListener();
    }
}