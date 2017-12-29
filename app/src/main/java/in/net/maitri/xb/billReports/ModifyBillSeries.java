package in.net.maitri.xb.billReports;

import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.User;

/**
 * Created by SYSRAJ4 on 28/12/2017.
 */

public class ModifyBillSeries extends DialogFragment {


    DbHandler dbHandler;

    private String bName;
    private String bNumber;
    private String bPrefix;
    Preference gBillNo;
    Preference gPrefix;


      public ModifyBillSeries(String bName, String bNumber, String bPrefix, Preference gBillNo, Preference gPrefix)
      {
          this.bName = bName;
          this.bNumber = bNumber;
          this.bPrefix = bPrefix;
          this.gBillNo = gBillNo;
          this.gPrefix = gPrefix;
      }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_bill_series2, container, false);
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
        eBillName.setText(bName);
        eBillNumber.setText(bNumber);
        ePrefix.setText(bPrefix);


        Button save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String billNumber = eBillNumber.getText().toString();
                String prefix = ePrefix.getText().toString();
                if(billNumber.isEmpty())
                {
                    Toast.makeText(getActivity(),"Current Bill number field cant be empty",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    dbHandler = new DbHandler(getActivity());

                    if(dbHandler.updateBillNo(Integer.valueOf(billNumber),prefix))
                    {
                        gBillNo.setSummary(billNumber);
                        gPrefix.setSummary(prefix);
                        Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Update failed",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        return view;
    }

    private boolean doLogin(User user){
        return new DbHandler(getActivity()).addUser(user);
    }
}
