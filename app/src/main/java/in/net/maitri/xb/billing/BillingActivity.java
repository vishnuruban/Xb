package in.net.maitri.xb.billing;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.net.maitri.xb.R;

/**
 * Created by SYSRAJ4 on 16/11/2017.
 */

public class BillingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);


        if (savedInstanceState == null) {
            FragmentOne filterFirstFragment = new FragmentOne();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.frameLayout1, filterFirstFragment, "TAG FRAGMENT");
            ft.commit();

            FragmentTwo filterSecondFragment = new FragmentTwo();
            FragmentManager fm1 = getFragmentManager();
            FragmentTransaction ft1 = fm1.beginTransaction();
            ft1.add(R.id.frameLayout2, filterSecondFragment, "TAG1 FRAGMENT");
            ft1.commit();

        }
    }
}
