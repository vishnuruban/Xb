package in.net.maitri.xb;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_PHONE_STATE = 0;
    private DbHandler mDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Date", getCurrentDate());
        mDbHandler = new DbHandler(MainActivity.this);
        int currentDateCount = mDbHandler.getDateCount(getCurrentDate());
        String sysDateCount = mDbHandler.getSysValue("SYS_LOCK_DATE");
        Log.d("currentDateCount", String.valueOf(currentDateCount));
        Log.d("sysDateCount",sysDateCount);
        if ( currentDateCount >= Integer.parseInt(sysDateCount)){
            createErrorDialog("Registration Error","Your application license has expired. Contact Maitri.");
        } else {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                checkValidity();
            } else {
                checkPhoneStatePermission();
            }
        }
    }

    public void checkValidity(){
        String[] imeiList = {"911431850362828", "911431850362836", "351558073207583",
                "351558071729646", "911528701045419", "911367106180517","354115076530193"};
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        if (isIMEIRegistered(imei, imeiList)) {
            checkIfFirstTym();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            createErrorDialog("Registration Error","This device is not registered with us.");
        }
    }


    private boolean isIMEIRegistered(String imei, String[] imeiList) {
        for (String anImeiList : imeiList) {
            if (imei.equals(anImeiList)) {
                return true;
            }
        }
        return false;
    }

    private void createErrorDialog(String title, String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void createPermissionDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Permission Required")
                .setMessage("Application need phone state permission to check your registration validity.")
                .setCancelable(false)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkPhoneStatePermission();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkIfFirstTym() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("isFirstTym", true)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTym", true);
            editor.apply();
        }
    }

    public void checkPhoneStatePermission() {
        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkValidity();
                } else {
                    createPermissionDialog();
                }
                break;
            }


        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
