package com.zanojmobiapps.inappupdatedemoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.zma.updatemanager.UpdateManager;
import com.zma.updatemanager.UpdateManagerConstant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpdateManager.Builder().mode(UpdateManagerConstant.IMMEDIATE).start(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateManager.continueUpdate(this);
    }
}
