package in.net.maitri.xb.billReports;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.billing.BillListAdapter;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.SalesDet;

/**
 * Created by SYSRAJ4 on 29/11/2017.
 */

public class BillReportDialog extends Dialog implements DialogInterface.OnClickListener{

    TextView selectedBill,selectedBillDate;

    Context context;
    int fromDate=0;int toDate=0;
    DbHandler dbHandler;
    private List<SalesDet> mGetBillDetails;
    int billNo;
    String dateTime;
    ProgressDialog mDialog;
    BillListAdapter billListAdapter;
    private ListView billListView;
    Button closeDialog;


    BillReportDialog(Context context, int fromDate, int toDate, int billNo,String dateTime, ProgressDialog mDialog)
    {
        super (context);
        this.context = context;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.billNo = billNo;
        this.mDialog = mDialog;
        this.dateTime = dateTime;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bill_report_dialog);

        dbHandler = new DbHandler(context);
        selectedBill = (TextView) findViewById(R.id.selected_bill);
        selectedBillDate = (TextView)findViewById(R.id.selected_bill_date);
        billListView = (ListView) findViewById(R.id.bill_lv);
        closeDialog =(Button)findViewById(R.id.closeDialog);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        mDialog.show();

        selectedBill.setText("Bill No: "+String.valueOf(billNo));
        selectedBillDate.setText(dateTime);
        getBillDetails(billNo,fromDate,toDate);

    }



    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }







    void getBillDetails(int billNo,int fromDate,int toDate) {

        mGetBillDetails = dbHandler.getBillDetails(billNo,fromDate,toDate);
        int quantity = 0;
        int items = 0;

        ArrayList<BillItems> billItems = new ArrayList<>();


        for(int i =0;i<mGetBillDetails.size();i++)
        {
            SalesDet sd =mGetBillDetails.get(i);

            billItems.add(sd.billItems);





            System.out.println("DESC"+sd.billItems.getDesc());
        }

        for(int i=0;i<billItems.size();i++)
        {

            BillItems bItm = billItems.get(i);
            quantity = quantity + bItm.getQty();
        }



        billListAdapter = new BillListAdapter(context,billItems);
        billListView.setAdapter(billListAdapter);


        mDialog.dismiss();
    }


}
