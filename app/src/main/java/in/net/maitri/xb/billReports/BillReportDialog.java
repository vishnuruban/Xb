package in.net.maitri.xb.billReports;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Messenger;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cie.btp.CieBluetoothPrinter;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.billing.BillListAdapter;
import in.net.maitri.xb.billing.BillPrint;
import in.net.maitri.xb.billing.CheckoutActivity;
import in.net.maitri.xb.billing.FragmentOne;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.SalesDet;
import in.net.maitri.xb.settings.GetSettings;


/**
 * Created by SYSRAJ4 on 29/11/2017.
 */

public class BillReportDialog extends Dialog implements DialogInterface.OnClickListener{

    TextView selectedBill,selectedBillDate,dSubTotal,dDiscount,dNetAmt,customerName,cashierName;

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
    Button printBill;
    double netAmt;
     String discount,subTotal;
    BillPrint billPrint;
    GetSettings getSettings;
    ArrayList<BillItems> billItems;
    double dQty;
    String rs;
    DecimalFormat df;
    int internalBillNo;
    String custName;
    String cashName;
    Messenger messenger;
    CieBluetoothPrinter bluetoothPrinter;


    BillReportDialog(Context context, int fromDate, int toDate, int billNo,String dateTime, ProgressDialog mDialog,String discount,double netAmt,String subTotal,double dQty,int internalBillNo,String custName,String cashName)
    {
        super (context);
        this.context = context;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.billNo = billNo;
        this.mDialog = mDialog;
        this.dateTime = dateTime;
        this.discount = discount;
        this.netAmt = netAmt;
        this.subTotal = subTotal;
        this.dQty = dQty;
        this.internalBillNo = internalBillNo;
        this.custName = custName;
        this.cashName = cashName;

    }

    BillReportDialog(Context context, int fromDate, int toDate, int billNo,String dateTime, ProgressDialog mDialog,String discount,double netAmt,String subTotal,double dQty,int internalBillNo,String custName,String cashName,Messenger messenger,CieBluetoothPrinter bluetoothPrinter)
    {
        super (context);
        this.context = context;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.billNo = billNo;
        this.mDialog = mDialog;
        this.dateTime = dateTime;
        this.discount = discount;
        this.netAmt = netAmt;
        this.subTotal = subTotal;
        this.dQty = dQty;
        this.internalBillNo = internalBillNo;
        this.custName = custName;
        this.cashName = cashName;
        this.messenger = messenger;
        this.bluetoothPrinter = bluetoothPrinter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bill_report_dialog);

        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(context, "Bluetooth not connected", Toast.LENGTH_SHORT).show();

        }
        //un comment the line below to debug the print service
        //mPrinter.setDebugService(BuildConfig.DEBUG);
        if (mAdapter == null) {
            Toast.makeText(context, "Bluetooth not connected", Toast.LENGTH_SHORT).show();
          //  finish();
        }
        //un comment the line below to debug the print service
        //mPrinter.setDebugService(BuildConfig.DEBUG);

        rs = "\u20B9";
        try{
            byte[] utf8 = rs.getBytes("UTF-8");


            rs = new String(utf8, "UTF-8");}
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        df = new DecimalFormat("0.00");

        dbHandler = new DbHandler(context);
        selectedBill = (TextView) findViewById(R.id.selected_bill);
        selectedBillDate = (TextView)findViewById(R.id.selected_bill_date);
       dSubTotal =(TextView)findViewById(R.id.dSubTotal);
        dDiscount =(TextView)findViewById(R.id.dSubDiscount);
        dNetAmt =(TextView)findViewById(R.id.dNetAmt);
        customerName =(TextView)findViewById(R.id.custName);
        cashierName = (TextView)findViewById(R.id.cashName);
        billListView = (ListView) findViewById(R.id.bill_lv);
        closeDialog =(Button)findViewById(R.id.closeDialog);
        printBill = (Button) findViewById(R.id.printBill);
        billPrint = new BillPrint(context);
        dDiscount.setText("Discount:  "+rs+ discount);
        dNetAmt.setText("Net Amount:  "+rs+ df.format(netAmt));
        dSubTotal.setText("Subtotal:  "+rs+ subTotal);
        getSettings = new GetSettings(context);
        customerName.setText("Customer Name : "+custName);
        cashierName.setText("Cashier Name : "+cashName);
        printBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String printSize = getSettings.getPrintingPaperSize();
                // String printSize1 = getResources().getString((R.array.paper_size_name)[printSize]);
                if (printSize.equals("1")) {
                    billPrint.printTwoInch(bluetoothPrinter,billItems,netAmt,String.valueOf(billNo),subTotal,discount,dQty,dateTime,cashName,custName);
                } else {
                    billPrint.printThreeInch(bluetoothPrinter,billItems,netAmt,String.valueOf(billNo),subTotal,discount,dQty,dateTime,cashName,custName);
                }
            }
        });



        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        mDialog.show();

        selectedBill.setText("Bill No: "+String.valueOf(billNo));
        selectedBillDate.setText(dateTime);
        getBillDetails(internalBillNo,fromDate,toDate,dateTime);

    }



    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
    }

    void getBillDetails(int billNo,int fromDate,int toDate,String billDateTime) {

        mGetBillDetails = dbHandler.getBillDetails(billNo,fromDate,toDate,billDateTime);
        int quantity = 0;
        int items = 0;

        billItems = new ArrayList<>();


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
