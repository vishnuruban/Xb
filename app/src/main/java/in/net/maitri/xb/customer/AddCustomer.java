package in.net.maitri.xb.customer;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Customer;
import in.net.maitri.xb.db.DbHandler;

public class AddCustomer extends DialogFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_customer, container, false);

        TextView dialogHeader = (TextView) view.findViewById(R.id.dialog_header);
        dialogHeader.setText("Add Customer");

        final EditText customerName = (EditText) view.findViewById(R.id.customer_name);
        final EditText customerMobile = (EditText) view.findViewById(R.id.mobile);
        final EditText customerGstin = (EditText) view.findViewById(R.id.gstin);
        final EditText customerAddress = (EditText) view.findViewById(R.id.address);
        Button add = (Button) view.findViewById(R.id.add_details);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = customerName.getText().toString();
                String mob = customerMobile.getText().toString();
                String gstin = customerGstin.getText().toString();
                String address = customerAddress .getText().toString();
                if (name.isEmpty() || mob.isEmpty()){
                    Toast.makeText(getActivity(), "Enter customer name or mobile no.", Toast.LENGTH_SHORT).show();
                } else {
                    Customer customer = new Customer();
                    if (name.isEmpty()){
                        customer.setName("");
                    } else {
                        customer.setName(name);
                    }
                    if (mob.isEmpty()){
                        customer.setMobileno(Integer.parseInt(""));
                    } else {
                        customer.setMobileno(Integer.parseInt(mob));
                    }
                    if (gstin.isEmpty()){
                        customer.setGstin("");
                    } else {
                        customer.setGstin(gstin);
                    }
                    if (address.isEmpty()){
                        customer.setAddress("");
                    } else {
                        customer.setAddress(address);
                    }
                    addCustomer(customer);
                }
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void addCustomer(Customer customer){
        DbHandler dbHandler = new DbHandler(getActivity());
        dbHandler.addCustomer(customer);
        dismiss();
    }
}
