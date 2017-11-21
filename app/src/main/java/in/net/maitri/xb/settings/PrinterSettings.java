package in.net.maitri.xb.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.PreferenceCategory;

import java.util.Set;

import in.net.maitri.xb.R;


public class PrinterSettings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private CharSequence[] mPairedDeviceEntries;
    private CharSequence[] mPairedDeviceEntryValues;
    private BluetoothAdapter mBluetoothAdapter;
    private ListPreference mPairedDevices;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_printer);

        SwitchPreference bluetooth = (SwitchPreference) findPreference("key_settings_printer_bluetooth");
        mPairedDevices = (ListPreference) findPreference("key_settings_printer_paired_device");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        getPairedDevises();
        if (mBluetoothAdapter == null) {
            bluetooth.setEnabled(false);
            Toast.makeText(getActivity(), "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
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
                boolean selected =   Boolean.parseBoolean(o.toString());
                if (selected){
                    if (mBluetoothAdapter != null) {
                        mBluetoothAdapter.enable();
                        mPairedDevices.setEnabled(true);
                    }
                }else {
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

    private void getPairedDevises(){
        if (mBluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            mPairedDeviceEntries = new CharSequence[pairedDevices.size()];
            mPairedDeviceEntryValues = new CharSequence[pairedDevices.size()];
            int i = 0;
            for (BluetoothDevice dev : pairedDevices) {
                mPairedDeviceEntries[i] = dev.getName();
                if (mPairedDeviceEntries[i] == null) mPairedDeviceEntries[i] = "unknown";
                mPairedDeviceEntryValues[i] = dev.getAddress();
                i++;
            }
            mPairedDevices.setEntries(mPairedDeviceEntries);
            mPairedDevices.setEntryValues(mPairedDeviceEntryValues);
        }else {
            Toast.makeText(getActivity(), "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
        }
    }
}
