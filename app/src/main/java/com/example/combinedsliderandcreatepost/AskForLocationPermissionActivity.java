package com.example.combinedsliderandcreatepost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.example.login.R;
import com.example.login.non_mvvm.distance_finder.BaseClass;
import com.example.login.non_mvvm.distance_finder.MainActivity;



public class AskForLocationPermissionActivity extends AppCompatActivity {

    private boolean _isAppSettings = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (_isAppSettings) {
            _askForLocationPermission();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_location_permission);

        _askForLocationPermission();
    }

    private void _askForLocationPermission() {
        _isAppSettings = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // ask for permissions
            if (
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                /**
                 *
                 * Ask for Location Permissions
                 *
                 */
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        BaseClass._REQUEST_LOCATION_PERMISSION_CODE
                );
            } else {
                /**
                 *
                 * Permissions already granted
                 *
                 */
                _goToMainActivity();
            }
        } else {
            // No need of asking permissions
            _goToMainActivity();

        }
    }

    private void _goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

//        Toast.makeText(this, "Everything working fine.", Toast.LENGTH_SHORT).show();

    }

    private void _closingApplicationAlertDialog() {

        new AlertDialog.Builder(AskForLocationPermissionActivity.this)
                .setTitle("Closing application")
                .setMessage("\"You may come back after allowing location.\"")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                })
                .create()
                .show();

    }

    private void _closingApplicationWithManualPermissionAlertDialog() {

        new AlertDialog.Builder(AskForLocationPermissionActivity.this)
                .setTitle("Closing application")
                .setMessage("\"You have denied location permission. You may go to app setting to manually allow location.\"")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                })
                .setNegativeButton("App Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        _isAppSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .create()
                .show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BaseClass._REQUEST_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                /**
                 *
                 * Permission granted
                 *
                 */
                _goToMainActivity();


            } else {
                /**
                 *
                 * Check for ShouldRationale
                 *
                 */
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    /**
                     *
                     * Give a second change to user to allow location
                     *
                     */
                    new AlertDialog.Builder(this)
                            .setTitle("Need location")
                            .setMessage("\"This application needs your location to function.\" Allow location?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    _askForLocationPermission();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    _closingApplicationAlertDialog();
                                }
                            })
                            .create()
                            .show();

                } else {
                    /**
                     *
                     * Ask him to turn on permission manually
                     *
                     */
                    _closingApplicationWithManualPermissionAlertDialog();
                }
            }
        }
    }
}
