package in.net.maitri.xb.settings;

import android.os.Bundle;
import android.view.MenuItem;

import com.cie.btp.CieBluetoothPrinter;

import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.BackUpAndRestoreDb;


public class SettingsActivity extends AppCompatPreferenceActivity {


    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.settings_header, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return CompanySettings.class.getName().equals(fragmentName) ||
                BillSettings.class.getName().equals(fragmentName) ||
                PrinterSettings.class.getName().equals(fragmentName) ||
                UserSettings.class.getName().equals(fragmentName) ||
                BackupAndRestore.class.getName().equals(fragmentName);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}