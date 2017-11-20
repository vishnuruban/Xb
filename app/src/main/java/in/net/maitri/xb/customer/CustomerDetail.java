package in.net.maitri.xb.customer;

import android.support.v4.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Customer;
import in.net.maitri.xb.db.DbHandler;

public class CustomerDetail extends AppCompatActivity {

    private CustomerAdapter mCustomerAdapter;
    private DbHandler mDbHandler;
    private List<Customer> mGetAllCustomers;
    private TextView mNoCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        mNoCustomer = (TextView) findViewById(R.id.no_customer);
        RecyclerView customerView = (RecyclerView) findViewById(R.id.customer_view);
        mDbHandler = new DbHandler(CustomerDetail.this);
        mGetAllCustomers = mDbHandler.getAllCustomer();
        mCustomerAdapter = new CustomerAdapter(CustomerDetail.this, mGetAllCustomers);
        if (mGetAllCustomers.isEmpty()){
            mNoCustomer.setVisibility(View.VISIBLE);
        } else{
            mNoCustomer.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomerDetail.this);
        customerView.setLayoutManager(linearLayoutManager);
        customerView.setAdapter(mCustomerAdapter);

        FloatingActionButton addCustomer = (FloatingActionButton) findViewById(R.id.add_customer);
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new AddCustomer();
                Bundle bundle = new Bundle();
                bundle.putBoolean("updateAdapter", true);
                newFragment.setCancelable(false);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    void updateCustomerAdapter(){
        mGetAllCustomers.clear();
        mGetAllCustomers = mDbHandler.getAllCustomer();
        if (mGetAllCustomers.isEmpty()){
            mNoCustomer.setVisibility(View.VISIBLE);
        } else{
            mNoCustomer.setVisibility(View.GONE);
        }
        mCustomerAdapter.notifyDataSetChanged();
    }
}
