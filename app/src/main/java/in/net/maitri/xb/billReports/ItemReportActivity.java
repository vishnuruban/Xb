package in.net.maitri.xb.billReports;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cie.btp.CieBluetoothPrinter;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.BillListAdapter;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.ReportData;
import in.net.maitri.xb.db.SalesMst;
import in.net.maitri.xb.itemdetails.RecyclerTouchListener;

/**
 * Created by SYSRAJ4 on 17/02/2018.
 */

public class ItemReportActivity extends AppCompatActivity {

    BillReportDialog brd;

    RecyclerView billView, billDetailsView;
    TextView noBills,noBillDetails;
    TextView selectedBill,selectedBillDate,tItems,tQty,tDiscount,tNetAmount,tPaymentStatus,tBillCount;
    BillMasterAdapter billMasterAdapter;

    private EditText mFromDate, mToDate;
    private int mYear, mMonth, mDay, mMinYear, mMinMonth, mMinDay;
    private String thisDate, mDate, mGetToDate = "", mGetFromDate = "";
    private String[] mDayOfWeak = {"Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"};

    DbHandler dbHandler;
    ProgressDialog mProgressDialog;
    LinearLayout summaryLayout;
    private List<ReportData> mGetItemMaster;

    byte[] excelReport;
    BillItemReportAdapter billItemReportAdapter;
    private List<SalesMst> mGetBillMaster;
    DecimalFormat df;
    String rs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_report);


        mGetBillMaster = new ArrayList<>();
        billView = (RecyclerView) findViewById(R.id.bill_view);
        tItems = (TextView)findViewById(R.id.tItems);
        tQty =(TextView)findViewById(R.id.tqty);
        tDiscount= (TextView) findViewById(R.id.tDiscount);
        tNetAmount=(TextView) findViewById(R.id.tNetAmt);
        // tPaymentStatus = (TextView)findViewById(R.id.t);
        tBillCount = (TextView)findViewById(R.id.tBills);
        mGetItemMaster = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rs = "\u20B9";
        try{
            byte[] utf8 = rs.getBytes("UTF-8");
            rs = new String(utf8, "UTF-8");}
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }


        summaryLayout = (LinearLayout) findViewById(R.id.summaryLayout);
        summaryLayout.setVisibility(View.GONE);
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
                    Toast.makeText(ItemReportActivity.this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else {
                    selectDate(mToDate, 0);
                }
            }
        });
        mToDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFromDate.getText().toString().length() == 0) {
                    Toast.makeText(ItemReportActivity.this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else {
                    selectDate(mToDate, 0);
                }
            }
        });



        mShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGetFromDate.isEmpty() || mGetToDate.isEmpty())
                {
                    Toast.makeText(ItemReportActivity.this, "Select date range", Toast.LENGTH_SHORT).show();
                }
                else {
                    mProgressDialog = new ProgressDialog(ItemReportActivity.this);
                    mProgressDialog.setMessage("Getting Data...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    //  getReportData(mDbHandeler.getDateCount(mGetFromDate), mDbHandeler.getDateCount(mGetToDate));

                    mGetBillMaster.clear();

                    Log.i("mGetFromDate",mGetFromDate);
                    Log.i("mGetToDate",mGetToDate);

                    dbHandler = new DbHandler(ItemReportActivity.this);
                    mGetItemMaster = dbHandler.getTotalItemReport(dbHandler.getDateCount(mGetFromDate),dbHandler.getDateCount(mGetToDate));
                    mGetBillMaster = dbHandler.getAllBills(dbHandler.getDateCount(mGetFromDate),dbHandler.getDateCount(mGetToDate));

                    if(mGetItemMaster.size()==0 || mGetBillMaster.size()==0)
                    {
                        Toast.makeText(ItemReportActivity.this,"No Records found",Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        return;
                    }
                    //  billLayout.setVisibility(View.VISIBLE);
                    summaryLayout.setVisibility(View.VISIBLE);
                    int items = 0,qty=0;
                    double discount = 0;
                    double netAm=0;
                    mProgressDialog.dismiss();
                   for(int i =0;i<mGetBillMaster.size();i++)
                    {
                        SalesMst bm  = mGetBillMaster.get(i);
                        qty = qty +(int) bm.getQty();
                        discount = discount+ bm.getDiscount();
                        netAm = netAm +bm.getNetAmt();
                        items = items + bm.getItems();

                    }

                    billItemReportAdapter = new BillItemReportAdapter(ItemReportActivity.this, mGetItemMaster);
                    billView.setAdapter(billItemReportAdapter);


                   tBillCount.setText("Bills:  "+String.valueOf(mGetBillMaster.size()));
                   tDiscount.setText("Discount:  "+rs+ df.format(discount));
                    tNetAmount.setText("Net Amount:  "+rs+ df.format(netAm));
                    tItems.setText("Items:  "+String.valueOf(items));
                    tQty.setText("Qty:  "+String.valueOf(qty));

                    //  AskPath ask = new AskPath(BillReportActivity.this,excelReport);
                    // ask.show();
                }
            }
        });
        //  setHeader();

        df = new DecimalFormat("0.00");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ItemReportActivity.this);
        billView.setLayoutManager(linearLayoutManager);
//        mGetBillMaster.get(0).setSelected(true);




    }


    private void selectDate(final EditText editText, final int responce) {
        final Calendar c = Calendar.getInstance(),
                c1 = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(ItemReportActivity.this, new DatePickerDialog.OnDateSetListener() {

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


}
