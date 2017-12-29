package in.net.maitri.xb.settings;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.user.AddNewUser;
import in.net.maitri.xb.user.ChangePassword;

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
                newFragment.show(getFragmentManager(), "");
                return true;
            }
        });

        Preference deleteUser = findPreference("key_settings_user_delete_user");
        deleteUser.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return true;
            }
        });
        deleteUser.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                List<String> getAllUsers = new DbHandler(getActivity()).getAllUsers();
                final String[] users = getAllUsers.toArray(new String[getAllUsers.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle("Delete User");
                builder.setItems(users, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (users[i].equals("admin")){
                            Toast.makeText(getActivity(),"Admin user can't be deleted.",Toast.LENGTH_SHORT).show();
                        } else {
                          askConfirmation(users[i]);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!sharedPreferences.getBoolean("user_is_admin", false)){
            addNewUser.setEnabled(false);
            deleteUser.setEnabled(false);
        }

        Preference changePassword = findPreference("key_settings_user_change_password");
        changePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ChangePassword newFragment = new ChangePassword();
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

     private void askConfirmation(final String userName){
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         builder.setCancelable(false);
         builder.setTitle("Are you sure to delete " + userName + "?");
         builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 if (new DbHandler(getActivity()).deleteUser(userName)){
                     Toast.makeText(getActivity(), userName + " deleted successfully.",Toast.LENGTH_SHORT).show();
                 } else {
                     Toast.makeText(getActivity(), userName + " deleting failed.",Toast.LENGTH_SHORT).show();
                 }
                 dialogInterface.cancel();
             }
         });
         builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.cancel();
             }
         });
         builder.create().show();
     }
}
