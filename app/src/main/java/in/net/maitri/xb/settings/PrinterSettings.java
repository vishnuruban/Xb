package in.net.maitri.xb.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.PreferenceCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import in.net.maitri.xb.R;
import in.net.maitri.xb.printing.epson.DiscoveryActivity;

import static android.app.Activity.RESULT_OK;

public class PrinterSettings extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private BluetoothAdapter mBluetoothAdapter;
    private ListPreference mPairedDevices;
    private ListPreference usb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_printer);

        usb = (ListPreference) findPreference("key_settings_printer_usb");
        usb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
               /* Intent intent = new Intent(getActivity(), DiscoveryActivity.class);
                startActivityForResult(intent, 0);*/
               getUsbDeviceList();
                return true;
            }
        });

        SwitchPreference bluetooth =
                (SwitchPreference) findPreference("key_settings_printer_bluetooth");
        mPairedDevices =
                (ListPreference) findPreference("key_settings_printer_paired_device");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        getPairedDevises();
        if (mBluetoothAdapter == null) {
            bluetooth.setEnabled(false);
            Toast.makeText(getActivity(),
                    "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                bluetooth.setChecked(true);
                mPairedDevices.setEnabled(true);
            } else {
                bluetooth.setChecked(false);
                mPairedDevices.setEnabled(false);
            }
        }
        bluetooth.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean selected = Boolean.parseBoolean(o.toString());
                if (selected) {
                    if (mBluetoothAdapter != null) {
                        mBluetoothAdapter.enable();
                        mPairedDevices.setEnabled(true);
                    }
                } else {
                    if (mBluetoothAdapter != null) {
                        mBluetoothAdapter.disable();
                        mPairedDevices.setEnabled(false);
                    }
                }
                return true;
            }
        });

        mPairedDevices.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getPairedDevises();
                return true;
            }
        });
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }


    @Override
    public void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            String target = data.getStringExtra("PrinterName")
                    + "\n" + data.getStringExtra(getString(R.string.title_target));
            if (target != null) {
                usb.setSummary(target);
                SharedPreferences.Editor prefs =
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                prefs.putString("key_settings_printer_usb", data.getStringExtra(getString(R.string.title_target)));
                prefs.apply();
                Toast.makeText(getActivity(),
                        data.getStringExtra(getString(R.string.title_target)), Toast.LENGTH_SHORT).show();
            } else {
                usb.setSummary("No printer selected");
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceCategory) {
                PreferenceCategory preferenceGroup = (PreferenceCategory) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    Preference singlePref = preferenceGroup.getPreference(j);
                    updatePreference(singlePref, singlePref.getKey());
                }
            } else {
                updatePreference(preference, preference.getKey());
            }
        }
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

    private void getPairedDevises() {
        if (mBluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            CharSequence[] mPairedDeviceEntries = new CharSequence[pairedDevices.size()];
            CharSequence[] mPairedDeviceEntryValues = new CharSequence[pairedDevices.size()];
            int i = 0;
            for (BluetoothDevice dev : pairedDevices) {
                mPairedDeviceEntries[i] = dev.getName();
                if (mPairedDeviceEntries[i] == null) mPairedDeviceEntries[i] = "unknown";
                mPairedDeviceEntryValues[i] = dev.getAddress();
                i++;
            }
            mPairedDevices.setEntries(mPairedDeviceEntries);
            mPairedDevices.setEntryValues(mPairedDeviceEntryValues);
        } else {
            Toast.makeText(getActivity(),
                    "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUsbDeviceList() {
        UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        ArrayList<UsbDevice> deviceArrayList = new ArrayList<>(deviceList.values());
        CharSequence[] mUsbDeviceEntries = new CharSequence[deviceArrayList.size()];
        CharSequence[] mUsbDeviceEntryValues = new CharSequence[deviceArrayList.size()];
        for (int i = 0; i < deviceArrayList.size(); i++) {
            mUsbDeviceEntries[i] = deviceArrayList.get(i).getDeviceName();
            mUsbDeviceEntryValues[i] = deviceArrayList.get(i).getDeviceName();
        }
        usb.setEntries(mUsbDeviceEntries);
        usb.setEntryValues(mUsbDeviceEntryValues);
    }
}