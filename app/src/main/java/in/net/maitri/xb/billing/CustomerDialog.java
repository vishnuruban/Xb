package in.net.maitri.xb.billing;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.reginald.editspinner.EditSpinner;

import java.util.ArrayList;

import in.net.maitri.xb.R;

/**
 * Created by SYSRAJ4 on 23/11/2017.
 */

public class CustomerDialog extends Dialog implements DialogInterface.OnClickListener {



    Activity activity ;
    ArrayList<CustomerList> customerListArrayList;
    TextInputEditText etSearch;
    ListView ctList;
    EditSpinner editSpinner;
    CustomerAdapter customerAdapter;
    String[] pModes = {"NAME", "PHONE NUMBER"};

    public CustomerDialog(Activity activity)
    {
        super(activity);
        this.activity = activity;
       // this.customerListArrayList =customerListArrayList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.customer_list);
        etSearch = (TextInputEditText) findViewById(R.id.etSearch);
        ctList = (ListView)findViewById(R.id.lvCustomers);
        editSpinner = (EditSpinner)findViewById(R.id.etSearchType);
        ListAdapter adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, activity.getResources().getStringArray(R.array.search_mode));
        editSpinner.setAdapter(adapter);
        editSpinner.setEditable(false);
        editSpinner.setText("NAME");
        editSpinner.addTextChangedListener(searchMode);

        customerListArrayList = new ArrayList<>();
        customerListArrayList.add(new CustomerList("ashok", "9999900000"));
        customerListArrayList.add(new CustomerList("balu", "200"));
        customerListArrayList.add(new CustomerList("chandru", "300"));
        customerListArrayList.add(new CustomerList("dinesh", "400"));
        customerListArrayList.add(new CustomerList("elango", "500"));
        customerListArrayList.add(new CustomerList("fazil", "600"));
        customerListArrayList.add(new CustomerList("guru", "700"));
        customerListArrayList.add(new CustomerList("hari", "800"));
        customerListArrayList.add(new CustomerList("imad", "900"));
        customerListArrayList.add(new CustomerList("jaya", "1000"));
        customerListArrayList.add(new CustomerList("kumar", "1100"));
        customerListArrayList.add(new CustomerList("lakshan", "1200"));
        customerListArrayList.add(new CustomerList("mani", "1300"));
        customerListArrayList.add(new CustomerList("nandha", "1400"));
        customerListArrayList.add(new CustomerList("owl", "1500"));
        customerListArrayList.add(new CustomerList("praveen", "1600"));
        customerListArrayList.add(new CustomerList("jaya", "1000"));
        customerListArrayList.add(new CustomerList("kumar", "1100"));
        customerListArrayList.add(new CustomerList("lakshan", "1200"));
        customerListArrayList.add(new CustomerList("mani", "1300"));
        customerListArrayList.add(new CustomerList("nandha", "1400"));
        customerListArrayList.add(new CustomerList("owl", "1500"));
        customerListArrayList.add(new CustomerList("praveen", "8867518994"));
        customerListArrayList.add(new CustomerList("jaya", "1000"));
        customerListArrayList.add(new CustomerList("kumar", "1100"));
        customerListArrayList.add(new CustomerList("lakshan", "1200"));
        customerListArrayList.add(new CustomerList("mani", "1300"));
        customerListArrayList.add(new CustomerList("nandha", "1400"));
        customerListArrayList.add(new CustomerList("owl", "1500"));
        customerListArrayList.add(new CustomerList("praveen", "1600"));




        //customerAdapter =new CustomerAdapter(activity,customerListArrayList,editSpinner.getText().toString());
       // ctList.setAdapter(customerAdapter);





        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                customerAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }


    TextWatcher searchMode = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            //customerAdapter =new CustomerAdapter(activity,customerListArrayList,editSpinner.getText().toString());
//            customerAdapter.notifyDataSetChanged();
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
        }
    };










    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}
