package in.net.maitri.xb.reports;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.customer.CustomerDetail;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.ReportData;
import in.net.maitri.xb.itemdetails.AddItemCategory;
import in.net.maitri.xb.settings.SettingsActivity;
import in.net.maitri.xb.util.CheckDeviceType;

public class TotalSales extends AppCompatActivity {

    private ExpandableListView mExpandableListView;
    private EditText mFromDate, mToDate;
    private int mYear, mMonth, mDay, mMinYear, mMinMonth, mMinDay;
    private String thisDate, mDate, mGetToDate = "", mGetFromDate = "";
    private String[] mDayOfWeak = {"Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"};
    private DbHandler mDbHandeler;
    private HashMap<String, List<ReportData>> mItemLevelData;
    private List<ReportData> mCategoryLevelData;
    private TotalSalesAdapter mTotalSalesAdapter;
    private LinearLayout mHeader1, mHeader2;
    private ProgressDialog mProgressDialog;
    private boolean isAdapterEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_sales);
        mItemLevelData = new HashMap<>();
        mCategoryLevelData = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 21 && getSupportActionBar() != null) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(TotalSales.this, R.color.darkpink));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat
                    .getColor(TotalSales.this, R.color.lightpink)));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mDbHandeler = new DbHandler(TotalSales.this);
        mExpandableListView = (ExpandableListView) findViewById(R.id.mainList);
        mHeader1 = (LinearLayout) findViewById(R.id.header1);
        mHeader2 = (LinearLayout) findViewById(R.id.header2);
        mHeader1.setVisibility(View.INVISIBLE);
        mHeader2.setVisibility(View.INVISIBLE);
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
                    Toast.makeText(TotalSales.this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else {
                    selectDate(mToDate, 0);
                }
            }
        });
        mToDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFromDate.getText().toString().length() == 0) {
                    Toast.makeText(TotalSales.this, "Select From Date", Toast.LENGTH_SHORT).show();
                } else {
                    selectDate(mToDate, 0);
                }
            }
        });
        mShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGetFromDate.isEmpty() || mGetToDate.isEmpty()) {
                    Toast.makeText(TotalSales.this, "Select date range", Toast.LENGTH_SHORT).show();
                } else {
                    mHeader1.setVisibility(View.INVISIBLE);
                    mHeader2.setVisibility(View.INVISIBLE);
                    mExpandableListView.setVisibility(View.INVISIBLE);

                    mProgressDialog = new ProgressDialog(TotalSales.this);
                    mProgressDialog.setMessage("Getting Data...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    getReportData(mDbHandeler.getDateCount(mGetFromDate), mDbHandeler.getDateCount(mGetToDate));
                }
            }
        });
        setHeader();
    }

    private void selectDate(final EditText editText, final int responce) {
        final Calendar c = Calendar.getInstance(),
                c1 = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(TotalSales.this, new DatePickerDialog.OnDateSetListener() {

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

    private void getReportData(int fromDate, int toDate) {
        mCategoryLevelData.clear();
        mCategoryLevelData = mDbHandeler.getTotalCategoryReport(fromDate, toDate);
        if (mCategoryLevelData.size() == 0) {
            Toast.makeText(TotalSales.this, "No record found", Toast.LENGTH_SHORT).show();
            mProgressDialog.cancel();
        } else {
            List<ReportData> itmData = mDbHandeler.getTotalItemReport(fromDate, toDate);
            createItemData(mCategoryLevelData, itmData);
            if (isAdapterEnabled) {
                mTotalSalesAdapter.notifyDataSetChanged();
            } else {
                createAdapter();
                isAdapterEnabled = true;
            }
            prepareData( mDbHandeler.getTotalReport(fromDate, toDate),  mDbHandeler.getTotalReport1(fromDate, toDate));
        }
    }

    private void createItemData(List<ReportData> catData, List<ReportData> itmData) {
        mItemLevelData.clear();
        List<ReportData> list;
        for (int i = 0; i < catData.size(); i++) {
            list = new ArrayList<>();
            String cat = catData.get(i).getrCategory();
            Log.d("Category", cat);
            for (int j = 0; j < itmData.size(); j++) {
                if (itmData.get(j).getrCategory().equals(cat)) {
                    list.add(itmData.get(j));
                }
            }
            mItemLevelData.put(String.valueOf(i), list);
        }
    }

    private void prepareData(List<ReportData> totalData, List<ReportData> totalData1 ) {
        mHeader1.setVisibility(View.VISIBLE);
        mHeader2.setVisibility(View.VISIBLE);
        mExpandableListView.setVisibility(View.VISIBLE);

        Resources r = getResources();
        float px;
        if (new CheckDeviceType(TotalSales.this).isTablet()) {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, r.getDisplayMetrics());
        } else {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, r.getDisplayMetrics());
        }
        mHeader2.removeAllViews();
        ReportData data = totalData.get(0);
        ReportData data1 = totalData1.get(0);
        TextView textView = new TextView(this);
        textView.setText(data.getrDescription());
        textView.setGravity(Gravity.CENTER);
        textView.setWidth((int) px);
        textView.setPadding(64, 0, 4, 0);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(ContextCompat.getColor(TotalSales.this, R.color.colorBlack));
        mHeader2.addView(textView);

        textView = new TextView(this);
        textView.setText(data.getrQty());
        textView.setWidth((int) px);
        textView.setGravity(Gravity.END);
        textView.setPadding(4, 0, 4, 0);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(ContextCompat.getColor(TotalSales.this, R.color.colorBlack));
        mHeader2.addView(textView);

        textView = new TextView(this);
        textView.setText(data.getrNetSales());
        textView.setWidth((int) px);
        textView.setGravity(Gravity.END);
        textView.setPadding(4, 0, 4, 0);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(ContextCompat.getColor(TotalSales.this, R.color.colorBlack));
        mHeader2.addView(textView);


        textView = new TextView(this);
        textView.setText(data1.getrDiscount());
        textView.setWidth((int) px);
        textView.setGravity(Gravity.END);
        textView.setPadding(4, 0, 4, 0);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(ContextCompat.getColor(TotalSales.this, R.color.colorBlack));
        mHeader2.addView(textView);

        textView = new TextView(this);
        textView.setText(data1.getrNetSales());
        textView.setWidth((int) px);
        textView.setGravity(Gravity.END);
        textView.setPadding(4, 0, 4, 0);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(ContextCompat.getColor(TotalSales.this, R.color.colorBlack));
        mHeader2.addView(textView);
        mProgressDialog.cancel();
    }

    private void createAdapter(){
        mTotalSalesAdapter = new TotalSalesAdapter(TotalSales.this, mCategoryLevelData, mItemLevelData);
        mExpandableListView.setAdapter(mTotalSalesAdapter);
    }

    private void setHeader(){
        String[] header = {"Description", "Total Qty", "Total Amount", "Total Discount", "Net Sales"};
        Resources r = getResources();
        float px;
        if (new CheckDeviceType(TotalSales.this).isTablet()) {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, r.getDisplayMetrics());
        } else {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, r.getDisplayMetrics());
        }
        mHeader1.removeAllViews();
        for (int i = 0; i < header.length; i++) {
            TextView textView = new TextView(this);
            if ( i == 0) {
                textView.setText(header[0]);
                textView.setGravity(Gravity.CENTER);
                textView.setWidth((int) px);
                textView.setPadding(64, 0, 4, 0);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextColor(ContextCompat.getColor(TotalSales.this, R.color.colorBlack));
                mHeader1.addView(textView);
            }  else {
                textView = new TextView(this);
                textView.setText(header[i]);
                textView.setWidth((int) px);
                textView.setGravity(Gravity.END);
                textView.setPadding(4, 0, 4, 0);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextColor(ContextCompat.getColor(TotalSales.this, R.color.colorBlack));
                mHeader1.addView(textView);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}