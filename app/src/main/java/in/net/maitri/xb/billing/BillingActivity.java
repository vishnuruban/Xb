package in.net.maitri.xb.billing;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billReports.BillReportActivity;
import in.net.maitri.xb.billReports.CustomerReportActivity;
import in.net.maitri.xb.billReports.ItemReportActivity;
import in.net.maitri.xb.billReports.TodayBillReport;
import in.net.maitri.xb.customer.CustomerDetail;
import in.net.maitri.xb.itemdetails.AddItemCategory;
import in.net.maitri.xb.reports.TotalSales;
import in.net.maitri.xb.settings.SettingsActivity;
import in.net.maitri.xb.util.CheckDeviceType;

public class BillingActivity extends AppCompatActivity {

    private FragmentTransaction ft;
    private FragmentOne filterFirstFragment;
    private FragmentThree filterThirdFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

  /*      if (savedInstanceState == null) {
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
        */

        if (new CheckDeviceType(BillingActivity.this).isTablet()) {

            if (savedInstanceState == null) {

                filterFirstFragment = new FragmentOne();
                filterThirdFragment = new FragmentThree();
                FragmentManager fm = getFragmentManager();
                ft = fm.beginTransaction();

                ft.add(R.id.frameLayout2, filterThirdFragment, "FRAGMENT3");
                ft.add(R.id.frameLayout1, filterFirstFragment, "FRAGMENT1");

                ft.commit();
            }

        } else {
            if (savedInstanceState == null) {

                filterFirstFragment = new FragmentOne();
                filterThirdFragment = new FragmentThree();
                FragmentManager fm = getFragmentManager();
                ft = fm.beginTransaction();

                ft.add(R.id.frameLayout2, filterThirdFragment, "FRAGMENT3");
                ft.add(R.id.frameLayout1, filterFirstFragment, "FRAGMENT1");

                ft.commit();
            }

            final FloatingActionButton cart = findViewById(R.id.cart);
            final FloatingActionButton item = findViewById(R.id.item);
            final FrameLayout fl1 = findViewById(R.id.frameLayout1);
            final FrameLayout fl2 = findViewById(R.id.frameLayout2);

            cart.setVisibility(View.GONE);
            fl2.setVisibility(View.GONE);

            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cart.setVisibility(View.GONE);
                    item.setVisibility(View.VISIBLE);
                    fl2.setVisibility(View.GONE);
                    fl1.setVisibility(View.VISIBLE);
                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setVisibility(View.GONE);
                    cart.setVisibility(View.VISIBLE);
                    fl1.setVisibility(View.GONE);
                    fl2.setVisibility(View.VISIBLE);
                }
            });
        }
    }

/*
    private void showFrag1(){
        FragmentManager fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.detach(filterThirdFragment);
        ft.attach(filterFirstFragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void showFrag3(){
        FragmentManager fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.detach(filterFirstFragment);
        ft.attach(filterThirdFragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_item_category, menu);
        MenuItem item = menu.findItem(R.id.bill_report);
        MenuItem item1 = menu.findItem(R.id.bill_item_report);
        MenuItem item2 = menu.findItem(R.id.bill_customer_report);
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(BillingActivity.this);
        if (!sharedPreferences.getBoolean("user_is_admin", false)) {
            item.setVisible(false);
            item1.setVisible(false);
            item2.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.add_item:
                startActivity(new Intent(BillingActivity.this, AddItemCategory.class));
                finish();
                break;

            case R.id.settings:
                startActivity(new Intent(BillingActivity.this, SettingsActivity.class));
                break;

            case R.id.customers:
                startActivity(new Intent(BillingActivity.this, CustomerDetail.class));
                break;

            case R.id.bill_report:
                startActivity(new Intent(BillingActivity.this, BillReportActivity.class));
                break;

            case R.id.today_bill_report:
                startActivity(new Intent(BillingActivity.this, TodayBillReport.class));
                break;

            case R.id.bill_item_report:
                startActivity(new Intent(BillingActivity.this, ItemReportActivity.class));
                break;

            case R.id.bill_customer_report:
                startActivity(new Intent(BillingActivity.this, CustomerReportActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder builder
                = new android.support.v7.app.AlertDialog.Builder(BillingActivity.this);
        builder.setMessage("Do you want to close the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }
}
