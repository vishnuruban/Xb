package in.net.maitri.xb.billReports;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.billing.BillListAdapter;
import in.net.maitri.xb.billing.FragmentOne;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.SalesDet;
import in.net.maitri.xb.db.SalesMst;
import in.net.maitri.xb.itemdetails.AddItemCategory;
import in.net.maitri.xb.itemdetails.RecyclerTouchListener;
import in.net.maitri.xb.reports.TotalSales;

/**
 * Created by SYSRAJ4 on 27/11/2017.
 */

public class BillReportActivity extends AppCompatActivity {



    RecyclerView billView, billDetailsView;
    TextView noBills,noBillDetails;
    TextView selectedBill,selectedBillDate,tItems,tQty,tDiscount,tNetAmount,tPaymentStatus;
    BillMasterAdapter billMasterAdapter;
    private ListView billListView;
    private EditText mFromDate, mToDate;
    private int mYear, mMonth, mDay, mMinYear, mMinMonth, mMinDay;
    private String thisDate, mDate, mGetToDate = "", mGetFromDate = "";
    private String[] mDayOfWeak = {"Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"};

    DbHandler dbHandler;
    ProgressDialog mProgressDialog;
    LinearLayout billLayout;
    private List<SalesMst> mGetBillMaster;
    private List<SalesDet> mGetBillDetails;

    BillListAdapter billListAdapter;

    DecimalFormat df;

    String rs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_report);


        rs = "\u20B9";
        try{
            byte[] utf8 = rs.getBytes("UTF-8");


            rs = new String(utf8, "UTF-8");}
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        billView = (RecyclerView) findViewById(R.id.bill_view);
       // billDetailsView = (RecyclerView) findViewById(R.id.bill_details_view);
      //  noBills = (TextView) findViewById(R.id.no_bills);
      //  noBillDetails = (TextView) findViewById(R.id.no_bill_details);

        selectedBill = (TextView) findViewById(R.id.selected_bill);
        selectedBillDate = (TextView)findViewById(R.id.selected_bill_date);
        tItems = (TextView)findViewById(R.id.tItems);
        tQty =(TextView)findViewById(R.id.tqty);
        tDiscount= (TextView) findViewById(R.id.tDiscount);
        tNetAmount=(TextView) findViewById(R.id.tNetAmt);
         tPaymentStatus = (TextView)findViewById(R.id.tPayment);
        billListView = (ListView) findViewById(R.id.bill_lv);
        billLayout =(LinearLayout) findViewById(R.id.billLayout);
        billLayout.setVisibility(View.INVISIBLE);



        LinearLayout mShowReport = (LinearLayout) findViewById(R.id.showReport);
        LinearLayout mFromDateLayout = (LinearLayout) findViewById(R.id.from_date_layout);
        LinearLayout mToDateLayout = (LinearLayout) findViewById(R.id.to_date_layout);
        mFromDate = (EditText) findViewById(R.id.from_date);
        mToDate = (EditText) findViewById(R.id.to_date);
        mFromDate.setClickable(true);
        mFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(mFromDate, 1);
            }
        });
        mFromDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(mFromDate, 1);
            }
        });
        mToDate.setClickable(true);
        mToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFromDate.getText().toString().length() == 0) {
                    Toast.makeText(BillReportActivity.this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else {
                    selectDate(mToDate, 0);
                }
            }
        });
        mToDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFromDate.getText().toString().length() == 0) {
                    Toast.makeText(BillReportActivity.this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else {
                    selectDate(mToDate, 0);
                }
            }
        });



        mShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGetFromDate.isEmpty() || mGetToDate.isEmpty()) {
                    Toast.makeText(BillReportActivity.this, "Select date range", Toast.LENGTH_SHORT).show();
                } else {


                    mProgressDialog = new ProgressDialog(BillReportActivity.this);
                    mProgressDialog.setMessage("Getting Data...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                  //  getReportData(mDbHandeler.getDateCount(mGetFromDate), mDbHandeler.getDateCount(mGetToDate));

                    dbHandler = new DbHandler(BillReportActivity.this);
                    mGetBillMaster = dbHandler.getAllBills(dbHandler.getDateCount(mGetFromDate),dbHandler.getDateCount(mGetToDate));


                    if(mGetBillMaster == null)
                    {
                        Toast.makeText(BillReportActivity.this,"No Bills found",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    billLayout.setVisibility(View.VISIBLE);

                    int items = 0,qty=0;
                    double discount = 0;
                    double netAm=0;

                    for(int i =0;i<mGetBillMaster.size();i++)
                    {
                        SalesMst bm  = mGetBillMaster.get(i);
                        items = items + bm.getItems();
                        qty = qty +(int) bm.getQty();
                        discount = discount+ bm.getDiscount();
                        netAm = netAm +bm.getNetAmt();

                    }

                    billMasterAdapter = new BillMasterAdapter(BillReportActivity.this, mGetBillMaster);
                    billView.setAdapter(billMasterAdapter);
                    billMasterAdapter.setSelected(0);
                    SalesMst mst = mGetBillMaster.get(0);

                    int billNo = mst.getBillNO();
                    String billDateTime =mst.getDateTime();

                    selectedBill.setText("Bill No:"+String.valueOf(billNo));
                    selectedBillDate.setText(billDateTime);
                    tDiscount.setText("Discount: "+rs+ df.format(discount));
                    tNetAmount.setText("Net Amount: "+rs+ df.format(netAm));
                    tItems.setText("Items: "+String.valueOf(items));
                    tQty.setText("Qty: "+String.valueOf(qty));
                    tPaymentStatus.setText("Payment mode : "+mst.getPaymentMode());

                    getBillDetails(billNo,dbHandler.getDateCount(mGetFromDate),dbHandler.getDateCount(mGetToDate));

                }
            }
        });
      //  setHeader();

        df = new DecimalFormat("0.00");



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BillReportActivity.this);
        billView.setLayoutManager(linearLayoutManager);
//        mGetBillMaster.get(0).setSelected(true);


        billView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), billView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
               billMasterAdapter.setSelected(position);

                SalesMst mst = mGetBillMaster.get(position);

                int billNo = mst.getBillNO();
                String billDateTime =mst.getDateTime();

                selectedBill.setText("Bill No:"+String.valueOf(billNo));
                selectedBillDate.setText(billDateTime);
             //   tDiscount.setText("Discount: "+rs+ df.format(mst.getDiscount()));
              //  tNetAmount.setText("Net Amount: "+rs+ df.format(mst.getNetAmt()));
                tPaymentStatus.setText("Payment mode : "+mst.getPaymentMode());



                getBillDetails(billNo,dbHandler.getDateCount(mGetFromDate),dbHandler.getDateCount(mGetToDate));

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("billDate", billDateTime);
                editor.putInt("billNo", billNo);
                editor.apply();



            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }





    private void selectDate(final EditText editText, final int responce) {
        final Calendar c = Calendar.getInstance(),
                c1 = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(BillReportActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if (responce == 1) {
                    mMinYear = year - mYear;
                    mMinMonth = monthOfYear - mMonth;
                    mMinDay = (dayOfMonth - mDay);
                }
                GregorianCalendar date = new GregorianCalendar(year, monthOfYear, dayOfMonth - 1);
                int dayOfWeek = date.get(GregorianCalendar.DAY_OF_WEEK) - 1;

                if (monthOfYear > 8) {
                    if (dayOfMonth > 9) {
                        thisDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        mDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    } else {
                        thisDate = "0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        mDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                    }
                } else {
                    if (dayOfMonth > 9) {
                        thisDate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                        mDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    } else {
                        thisDate = "0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                        mDate = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                    }
                }

                String text = thisDate + " (" + mDayOfWeak[dayOfWeek].substring(0, 3) + ")";
                editText.setText(text);
                if (responce == 1) {
                    mToDate.setText(text);
                    mGetToDate = mDate;
                    mGetFromDate = mDate;
                } else if (responce == 0) {
                    mGetToDate = mDate;
                }
            }
        }, mYear, mMonth, mDay);

        if (responce == 0) {
            c1.add(Calendar.YEAR, mMinYear);
            c1.add(Calendar.MONTH, mMinMonth);
            c1.add(Calendar.DAY_OF_MONTH, mMinDay);
            dpd.getDatePicker().setMinDate(c1.getTimeInMillis());
            dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
        } else if (responce == 1) {
            c.add(Calendar.DAY_OF_MONTH, -1);
            c.add(Calendar.DAY_OF_MONTH, 1);
            dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
        }
        dpd.show();
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



        billListAdapter = new BillListAdapter(BillReportActivity.this,billItems);
        billListView.setAdapter(billListAdapter);


        mProgressDialog.dismiss();
    }

}




