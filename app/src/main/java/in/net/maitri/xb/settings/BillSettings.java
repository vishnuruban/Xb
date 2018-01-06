package in.net.maitri.xb.settings;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billReports.ModifyBillSeries;
import in.net.maitri.xb.billing.BillSeries;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.user.AddNewUser;

public class BillSettings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    DbHandler dbHandler;
    BillSeries bm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_bill);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        dbHandler = new DbHandler(getActivity());
         bm = dbHandler.getBillSeries(1);
        Preference getBillName = findPreference("key_settings_company_bill_name");
        final Preference getBillNumber = findPreference("key_settings_company_starting_bill_no");
        Preference getresetType = findPreference("key_settings_company_reset_type");
        final Preference getBillPrefix = findPreference("key_settings_company_prefix");
        Preference editBill = findPreference("key_settings_user_edit_bill_settings");
        final Preference getCustomerSelection = findPreference("key_settings_bill_customer_selection_rqd");
        final Preference getCashierSelection = findPreference("key_settings_bill_cashier_selection_rqd");

        editBill.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Bundle bundle = new Bundle();
                BillSeriesData data = new BillSeriesData();
                data.setbName(bm.getBillName());
                data.setbNumber(String.valueOf(bm.getCurrentBillNo()));
                data.setbPrefix(bm.getPrefix());
                data.setbCashier(bm.getCashierSelection());
                data.setbCustomer(bm.getCustomerSelection());
                data.setgBillNo(getBillNumber);
                data.setgPrefix(getBillPrefix);
                data.setgCashier(getCashierSelection);
                data.setgCustomer(getCustomerSelection);
                bundle.putSerializable("billSeriesObj", data);
                ModifyBillSeries newFragment = new ModifyBillSeries();
                newFragment.setArguments(bundle);
                newFragment.setCancelable(false);
                newFragment.show(getFragmentManager(),"");
               /* ModifyBillSeries newFragment = new ModifyBillSeries(bm.getBillName(),
                        String.valueOf(bm.getCurrentBillNo()),bm.getPrefix(),
                        bm.getCashierSelection(),bm.getCustomerSelection(),
                        getBillNumber,getBillPrefix,getCashierSelection,getCustomerSelection);*/


                return true;
            }
        });




        getBillName.setSummary(bm.getBillName());
        getBillNumber.setSummary(String.valueOf(bm.getCurrentBillNo()));
        getresetType.setSummary(bm.getResetType());
        getCashierSelection.setSummary(bm.getCashierSelection());
        getCustomerSelection.setSummary(bm.getCustomerSelection());
        if(!bm.getPrefix().isEmpty())
        getBillPrefix.setSummary(bm.getPrefix());
        else
            getBillPrefix.setSummary("-");
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
}