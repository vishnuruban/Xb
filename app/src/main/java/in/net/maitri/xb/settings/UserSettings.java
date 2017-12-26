package in.net.maitri.xb.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import in.net.maitri.xb.R;
import in.net.maitri.xb.user.AddNewUser;


public class UserSettings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_user);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        Preference addNewUser = findPreference("key_settings_user_add_new_user");
        addNewUser.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AddNewUser newFragment = new AddNewUser();
                newFragment.setCancelable(false);
                newFragment.show(getFragmentManager(),"");
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
}
