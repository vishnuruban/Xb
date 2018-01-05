package in.net.maitri.xb.billing;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Customer;
import in.net.maitri.xb.db.DbHandler;

/**
 * Created by SYSRAJ4 on 14/12/2017.
 */

public class AddCustomerDialog extends Dialog {


    Context context;
    EditText cName,cNumber,cEmail,cAdd1,cAdd2,cCity,cState,cGst;
    Button addCustomer , clearCusData;
    DbHandler dbHandler ;
    ImageView close;
    CustomerAdapter customerAdapter;
    ArrayList<Customer> customerArrayList;
    String name;



    public AddCustomerDialog (Context context,CustomerAdapter customerAdapter,ArrayList<Customer> customerArrayList,String name)
    {
        super(context);
        this.context = context;
        this.customerAdapter = customerAdapter;
        this.customerArrayList = customerArrayList;
        this.name = name;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_customer);

        cName = (EditText)findViewById(R.id.name);
        cNumber = (EditText)findViewById(R.id.mobile);
        cEmail = (EditText) findViewById(R.id.email);
        cAdd1 = (EditText) findViewById(R.id.add1);
        cAdd2 = (EditText)findViewById(R.id.add2);
        cCity = (EditText)findViewById(R.id.city);
        cState = (EditText)findViewById(R.id.state);
        cGst = (EditText)findViewById(R.id.gst);
        close = (ImageView) findViewById(R.id.close);
        dbHandler = new DbHandler(context);

        cName.addTextChangedListener(customerWatcher);

        addCustomer = (Button)findViewById(R.id.addCustomer);
        clearCusData = (Button)findViewById(R.id.clearCustomer);

        if(!name.isEmpty()) {
            cName.setText(name);
            cNumber.requestFocus();
        }


        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = cName.getText().toString();
                String number =cNumber.getText().toString();
                String mail = cEmail.getText().toString();
                String add1 = cAdd1.getText().toString();
                String add2 = cAdd2.getText().toString();
                String city = cCity.getText().toString();
                String state = cState.getText().toString();
                String gst = cGst.getText().toString();

                if(cName.getText().toString().isEmpty() || cNumber.getText().toString().isEmpty() || cNumber.getText().length()<10)
                {

                    Toast.makeText(context,"Enter valid fields!",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Customer customer = new Customer(name,number,mail,add1,add2,city,state,gst);
                    long result = dbHandler.addCustomer(customer);

                    if(result !=-1)
                    {
                        customerArrayList.add(customer);
                        Toast.makeText(context,"Customer added successfully!",Toast.LENGTH_SHORT).show();
                        FragmentOne.autoCustomer.setText(name);
                        customerAdapter.notifyDataSetChanged();

                        dismiss();
                    }
                    else
                    {
                        Toast.makeText(context,"Mobile number already present!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

         clearCusData.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 cName.setText("");
                 cNumber.setText("");
                 cEmail.setText("");
                 cAdd1.setText("");
                 cAdd2.setText("");
                 cCity.setText("");
                 cState.setText("");
                 cGst.setText("");
             }
         });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }







    TextWatcher customerWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            String searchString = s.toString();
            int textLength = searchString.length();

            if (s.toString().matches("[0-9]+") && textLength > 2 && textLength <=10) {
             cNumber.setText(s.toString());
            }
            else if(s.toString().matches("[0-9]+") && textLength < 2)
            {
                cNumber.setText("");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }
        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
        }
    };

}
