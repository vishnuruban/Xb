package in.net.maitri.xb;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.List;

import in.net.maitri.xb.itemdetails.AddItemCategory;
import in.net.maitri.xb.util.Permissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] imeiList = {"911431850362828", "911431850362836", "351558073207583","351558071729646"};

        new Permissions(MainActivity.this).checkPhoneStatePermission();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();

            if (isIMEIRegistered(imei, imeiList)) {
                checkIfFirstTym();
                startActivity(new Intent(MainActivity.this, AddItemCategory.class));
            } else {
                createErrorDialog("This device is not registered with us.");
            }
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

    private void createErrorDialog(String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Registration Error")
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

    private void checkIfFirstTym(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("isFirstTym", true)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTym", true);
            editor.apply();
        }
    }
}
