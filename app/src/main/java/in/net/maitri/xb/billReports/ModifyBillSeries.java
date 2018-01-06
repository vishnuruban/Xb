package in.net.maitri.xb.billReports;

import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.reginald.editspinner.EditSpinner;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.db.User;
import in.net.maitri.xb.settings.BillSeriesData;

public class ModifyBillSeries extends DialogFragment {

    private DbHandler dbHandler;
    private String bName;
    private String bNumber;
    private String bPrefix;
    private String bCashier;
    private String bCustomer;
    Preference gCashier;
    Preference gCustomer;
    Preference gBillNo;
    Preference gPrefix;

    public ModifyBillSeries(){

    }

 /*   public ModifyBillSeries(String bName, String bNumber, String bPrefix, String bCashier, String bCustomer,
                            Preference gBillNo, Preference gPrefix, Preference gCashier, Preference gCustomer) {
        this.bName = bName;
        this.bNumber = bNumber;
        this.bPrefix = bPrefix;
        this.gBillNo = gBillNo;
        this.gPrefix = gPrefix;
        this.bCashier = bCashier;
        this.bCustomer = bCustomer;
        this.gCashier = gCashier;
        this.gCustomer = gCustomer;
    }
*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_bill_series2, container, false);

        Bundle bundle = getArguments();
        final BillSeriesData data = (BillSeriesData) bundle.getSerializable("billSeriesObj");
        bName = data.getbName();
        bNumber = data.getbNumber();
        bPrefix = data.getbPrefix();
        gBillNo = data.getgBillNo();
        gPrefix = data.getgPrefix();
        bCashier = data.getbCashier();
        bCustomer = data.getbCustomer();
        gCashier = data.getgCashier();
        gCustomer = data.getgCustomer();

        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final EditText eBillName = (EditText) view.findViewById(R.id.billName);
        eBillName.setFocusable(false);
        eBillName.setFocusableInTouchMode(false);
        final EditText eBillNumber = (EditText) view.findViewById(R.id.billNumber);
        final EditText ePrefix = (EditText) view.findViewById(R.id.billPrefix);
        final EditSpinner eCustomer = (EditSpinner) view.findViewById(R.id.billCustSelection);
        final EditSpinner eCashier = (EditSpinner) view.findViewById(R.id.billCashSelection);
        eBillName.setText(bName);
        eBillNumber.setText(bNumber);
        ePrefix.setText(bPrefix);
        eCustomer.setText(bCustomer);
        eCashier.setText(bCashier);
        ListAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.yesno_mode));
        eCustomer.setAdapter(adapter);
        eCashier.setAdapter(adapter);
        eCustomer.setEditable(false);
        eCashier.setEditable(false);

        Button save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String billNumber = eBillNumber.getText().toString();
                String prefix = ePrefix.getText().toString();
                String cashName = eCashier.getText().toString();
                String custName = eCustomer.getText().toString();
                if (billNumber.isEmpty()) {
                    Toast.makeText(getActivity(), "Current Bill number field cant be empty", Toast.LENGTH_SHORT).show();
                } else {

                    dbHandler = new DbHandler(getActivity());

                    if (dbHandler.updateBillSeries(Integer.valueOf(billNumber), prefix, custName, cashName)) {
                        gBillNo.setSummary(billNumber);
                        gPrefix.setSummary(prefix);
                        gCashier.setSummary(custName);
                        gCustomer.setSummary(cashName);
                        Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        return view;
    }

    private boolean doLogin(User user) {
        return new DbHandler(getActivity()).addUser(user);
    }

}
