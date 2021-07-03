package com.example.weatherapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;
import android.provider.Settings.System;
import com.example.weatherapp.R;

public class SettingsActivity extends AppCompatActivity {

    SeekBar seekBar;
    int currentBrightness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(checkSystemWritePermission()){}
        seekBar  =(SeekBar) findViewById(R.id.seekbar);
        currentBrightness = System.getInt(getBaseContext().getContentResolver(),System.SCREEN_BRIGHTNESS,0);

        seekBar.setProgress(currentBrightness);
        //Toast.makeText(this, "Current Brightness : "+currentBrightness, Toast.LENGTH_SHORT).show();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    System.putInt(getBaseContext().getContentResolver(),System.SCREEN_BRIGHTNESS,progress);

                }
                catch (Exception e){

                    Toast.makeText(SettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               //

            }
        });
        ((findViewById(R.id.txt))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(this))
                return true;
            else {
                new AlertDialog.Builder(this)
                        .setMessage("To change brightess of phone need to allow permission")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openAndroidPermissionsMenu();
                                dialog.cancel();
                            }
                        })
                        .setCancelable(false).create().show();
            }
        }
        return false;
    }
    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }
    }
}