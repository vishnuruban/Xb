package in.net.maitri.xb.billReports;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.BillingActivity;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.ReportData;
import in.net.maitri.xb.db.SalesMst;

public class ItemReportActivity extends AppCompatActivity {

    private TextView tItems, tQty, tDiscount, tNetAmount, tBillCount;
    private EditText mFromDate, mToDate;
    private int mYear, mMonth, mDay, mMinYear, mMinMonth, mMinDay;
    private String thisDate, mDate, mGetToDate = "", mGetFromDate = "";
    private String[] mDayOfWeak = {"Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"};
    private DbHandler dbHandler;
    private ProgressDialog mProgressDialog;
    private LinearLayout summaryLayout;
    private List<ReportData> mGetItemMaster;
    private BillItemReportAdapter billItemReportAdapter;
    private List<SalesMst> mGetBillMaster;
    private DecimalFormat df;
    private String rs, mFilterQuery;
    private HashMap<String, ArrayList<Integer>> mSelectedFilterValue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_report);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mFilterQuery = "";
        mSelectedFilterValue = new HashMap<>();
        mSelectedFilterValue.put("Category", new ArrayList<Integer>());
        mSelectedFilterValue.put("Item", new ArrayList<Integer>());

        mGetBillMaster = new ArrayList<>();
        RecyclerView billView = (RecyclerView) findViewById(R.id.bill_view);
        tItems = (TextView) findViewById(R.id.tItems);
        tQty = (TextView) findViewById(R.id.tqty);
        tDiscount = (TextView) findViewById(R.id.tDiscount);
        tNetAmount = (TextView) findViewById(R.id.tNetAmt);
        tBillCount = (TextView) findViewById(R.id.tBills);
        mGetItemMaster = new ArrayList<>();
        rs = "\u20B9";
        try {
            byte[] utf8 = rs.getBytes("UTF-8");
            rs = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            rs = "Rs.";
        }
        summaryLayout = (LinearLayout) findViewById(R.id.summaryLayout);
        summaryLayout.setVisibility(View.GONE);
        LinearLayout mShowReport = (LinearLayout) findViewById(R.id.showReport);
        LinearLayout mFromDateLayout = (LinearLayout) findViewById(R.id.from_date_layout);
        LinearLayout mToDateLayout = (LinearLayout) findViewById(R.id.to_date_layout);
        LinearLayout mFilter = (LinearLayout) findViewById(R.id.filter);
        mFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ItemReportActivity.this, FilterActivity.class)
                        .putExtra("filter", mSelectedFilterValue)
                        .putExtra("filter", mSelectedFilterValue),1);
            }
        });
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
                summaryLayout.setVisibility(View.GONE);
                if (mGetFromDate.isEmpty() || mGetToDate.isEmpty()) {
                    Toast.makeText(ItemReportActivity.this, "Select date range", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressDialog = new ProgressDialog(ItemReportActivity.this);
                    mProgressDialog.setMessage("Getting Data...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();

                    mGetBillMaster.clear();
                    mGetItemMaster.clear();

                    dbHandler = new DbHandler(ItemReportActivity.this);
                    List<ReportData> list = dbHandler.getTotalItemReport(dbHandler.getDateCount(mGetFromDate), dbHandler.getDateCount(mGetToDate), mFilterQuery);
                    mGetItemMaster.addAll(list);
                    billItemReportAdapter.notifyDataSetChanged();
                    mGetBillMaster = dbHandler.getAllBills(dbHandler.getDateCount(mGetFromDate), dbHandler.getDateCount(mGetToDate));

                    if (mGetItemMaster.size() == 0 || mGetBillMaster.size() == 0) {
                        Toast.makeText(ItemReportActivity.this, "No Records found", Toast.LENGTH_SHORT).show();
                    } else {
                        summaryLayout.setVisibility(View.VISIBLE);
                        int items = 0, qty = 0;
                        double discount = 0;
                        double netAm = 0;
                        for (int i = 0; i < mGetBillMaster.size(); i++) {
                            SalesMst bm = mGetBillMaster.get(i);
                            qty = qty + (int) bm.getQty();
                            discount = discount + bm.getDiscount();
                            netAm = netAm + bm.getNetAmt();
                            items = items + bm.getItems();
                        }

                       /* billItemReportAdapter = new BillItemReportAdapter(mGetItemMaster);
                        billView.setAdapter(billItemReportAdapter);*/

                        String text = "Bills:  " + String.valueOf(mGetBillMaster.size());
                        tBillCount.setText(text);
                        text = "Discount:  " + rs + df.format(discount);
                        tDiscount.setText(text);
                        text = "Net Amount:  " + rs + df.format(netAm);
                        tNetAmount.setText(text);
                        text = "Items:  " + String.valueOf(items);
                        tItems.setText(text);
                        text = "Qty:  " + String.valueOf(qty);
                        tQty.setText(text);
                    }
                    mProgressDialog.dismiss();
                }
            }
        });
        df = new DecimalFormat("0.00");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ItemReportActivity.this);
        billView.setLayoutManager(linearLayoutManager);
        billItemReportAdapter = new BillItemReportAdapter(mGetItemMaster);
        billView.setAdapter(billItemReportAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                mSelectedFilterValue = (HashMap<String, ArrayList<Integer>>) data.getSerializableExtra("filter");
                mFilterQuery  = data.getStringExtra("query");
            }
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}