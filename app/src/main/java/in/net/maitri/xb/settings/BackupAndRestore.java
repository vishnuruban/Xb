package in.net.maitri.xb.settings;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.CheckoutActivity;
import in.net.maitri.xb.db.BackUpAndRestoreDb;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.itemdetails.AddItemCategory;
import in.net.maitri.xb.registration.Registration;
import in.net.maitri.xb.user.AddNewUser;
import in.net.maitri.xb.util.FilePicker;
import in.net.maitri.xb.util.Permissions;

import static android.app.Activity.RESULT_OK;

public class BackupAndRestore extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int REQUEST_PICK_FILE = 1;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_backup_restore);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        Preference backup = findPreference("key_settings_backupAndRestore_backup");
        Preference restore = findPreference("key_settings_backupAndRestore_restore");
        Preference delete = findPreference("key_settings_backupAndRestore_delete");

        backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new BackUpAndRestoreDb(getActivity()).exportDB();
                return true;
            }
        });

        restore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), FilePicker.class);
                    startActivityForResult(intent, REQUEST_PICK_FILE);
                } else {
                    createPermissionDialog("Application need Storage permission to search backup db.");
                }
                return true;
            }
        });

        delete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are you sure you want to reset the app?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reset();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        updatePreference(findPreference(key), key);
    }

    private void updatePreference(Preference preference, String key) {
        if (preference == null) return;
        if (preference instanceof EditTextPreference) {
            EditTextPreference listPreference = (EditTextPreference) preference;
            listPreference.setSummary(listPreference.getText());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_FILE:
                    if (data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {
                        File selectedFile = new File
                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        if (new BackUpAndRestoreDb(getActivity()).importDB(selectedFile)) {
                            Intent intent = new Intent(getActivity(), AddItemCategory.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                    break;
            }
        }

    }

    private void reset() {
        SharedPreferences preferences = getActivity().getSharedPreferences(CheckoutActivity.mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        new DbHandler(getActivity()).resetData();
    }

    private void createPermissionDialog(String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Permission Required")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       checkWriteExternalStoragePermission();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkWriteExternalStoragePermission() {
        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((getActivity()),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), FilePicker.class);
                    startActivityForResult(intent, REQUEST_PICK_FILE);
                } else {
                    createPermissionDialog("Application need Storage permission to search backup db.");
                }
                break;



        }
    }
}