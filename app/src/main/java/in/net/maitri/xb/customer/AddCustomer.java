package in.net.maitri.xb.customer;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Customer;
import in.net.maitri.xb.db.DbHandler;

public class AddCustomer extends DialogFragment {

    private CustomerDetail mCustomerDetail;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCustomerDetail = (CustomerDetail) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_customer, container, false);

        TextView dialogHeader = (TextView) view.findViewById(R.id.dialog_header);
        dialogHeader.setText("Add Customer");
        final boolean updateAdapter = getArguments().getBoolean("updateAdapter");
        final EditText customerName = (EditText) view.findViewById(R.id.customer_name);
        final EditText customerMobile = (EditText) view.findViewById(R.id.mobile);
        final EditText customerGstin = (EditText) view.findViewById(R.id.gstin);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final EditText customerAddress = (EditText) view.findViewById(R.id.address);
        Button add = (Button) view.findViewById(R.id.add_details);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = customerName.getText().toString();
                String mob = customerMobile.getText().toString();
                String gstin = customerGstin.getText().toString();
                String address = customerAddress.getText().toString();
                if (name.isEmpty() || mob.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter customer name or mobile no.", Toast.LENGTH_SHORT).show();
                } else {
                    Customer customer = new Customer();
                    if (name.isEmpty()) {
                        customer.setName("");
                    } else {
                        customer.setName(name);
                    }
                    if (mob.isEmpty()) {
                        customer.setMobileno("");
                    } else {
                        customer.setMobileno(mob);
                    }
                    if (gstin.isEmpty()) {
                        customer.setGstin("");
                    } else {
                        customer.setGstin(gstin);
                    }
                    if (address.isEmpty()) {
                        customer.setAddress1("");
                    } else {
                        customer.setAddress1(address);
                    }
                    addCustomer(customer);
                    if (updateAdapter) {
                        mCustomerDetail.updateCustomerAdapter();
                    }
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

    private void addCustomer(Customer customer) {
        DbHandler dbHandler = new DbHandler(getActivity());
        dbHandler.addCustomer(customer);
        dismiss();
    }

}
