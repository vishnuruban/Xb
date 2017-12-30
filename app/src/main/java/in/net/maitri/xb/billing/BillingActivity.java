package in.net.maitri.xb.billing;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import in.net.maitri.xb.R;

public class BillingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        if (savedInstanceState == null) {

            FragmentOne filterFirstFragment = new FragmentOne();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.frameLayout1, filterFirstFragment, "TAG FRAGMENT");
            ft.commit();

            FragmentThree filterThirdFragment = new FragmentThree();
            FragmentManager fm3 = getFragmentManager();
            FragmentTransaction ft3 = fm3.beginTransaction();
            ft3.add(R.id.frameLayout2, filterThirdFragment, "TAG1 FRAGMENT");
            ft3.commit();

        }
    }

}
