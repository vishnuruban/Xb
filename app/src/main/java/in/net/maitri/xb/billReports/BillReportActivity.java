package in.net.maitri.xb.billReports;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/**
 * Created by SYSRAJ4 on 27/11/2017.
 */

public class BillReportActivity extends AppCompatActivity {

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
    private List<SalesMst> mGetBillMaster;

byte[] excelReport;
    BillListAdapter billListAdapter;

    DecimalFormat df;



    String rs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_report);
        mGetBillMaster = new ArrayList<>();

        rs = "\u20B9";
        try{
            byte[] utf8 = rs.getBytes("UTF-8");
            rs = new String(utf8, "UTF-8");}
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        billView = (RecyclerView) findViewById(R.id.bill_view);
         tItems = (TextView)findViewById(R.id.tItems);
         tQty =(TextView)findViewById(R.id.tqty);
         tDiscount= (TextView) findViewById(R.id.tDiscount);
         tNetAmount=(TextView) findViewById(R.id.tNetAmt);
        // tPaymentStatus = (TextView)findViewById(R.id.t);
        tBillCount = (TextView)findViewById(R.id.tBills);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                if (mGetFromDate.isEmpty() || mGetToDate.isEmpty())
                     {
                    Toast.makeText(BillReportActivity.this, "Select date range", Toast.LENGTH_SHORT).show();
                     }
                else {
                    mProgressDialog = new ProgressDialog(BillReportActivity.this);
                    mProgressDialog.setMessage("Getting Data...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                  //  getReportData(mDbHandeler.getDateCount(mGetFromDate), mDbHandeler.getDateCount(mGetToDate));

                    mGetBillMaster.clear();

                    dbHandler = new DbHandler(BillReportActivity.this);
                    mGetBillMaster = dbHandler.getAllBills(dbHandler.getDateCount(mGetFromDate),dbHandler.getDateCount(mGetToDate));

                    if(mGetBillMaster.size()==0)
                    {
                        Toast.makeText(BillReportActivity.this,"No Bills found",Toast.LENGTH_SHORT).show();
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
                        items = items + bm.getItems();
                        qty = qty +(int) bm.getQty();
                        discount = discount+ bm.getDiscount();
                        netAm = netAm +bm.getNetAmt();

                    }

                    billMasterAdapter = new BillMasterAdapter(BillReportActivity.this, mGetBillMaster);
                    billView.setAdapter(billMasterAdapter);


                     tBillCount.setText("Bills:  "+String.valueOf(mGetBillMaster.size()));
                     tDiscount.setText("Discount:  "+rs+ df.format(discount));
                     tNetAmount.setText("Net Amount:  "+rs+ df.format(netAm));
                     tItems.setText("Items:  "+String.valueOf(items));
                     tQty.setText("Qty:  "+String.valueOf(qty));


                    isStoragePermissionGranted(BillReportActivity.this);
                  excelReport = convertToExcel(df.format(discount),df.format(netAm),String.valueOf(items),String.valueOf(qty));

                  //  AskPath ask = new AskPath(BillReportActivity.this,excelReport);
                   // ask.show();
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
                mProgressDialog.show();
                int billNo = mst.getBillNO();
              String billDateTime =mst.getDateTime();
                double discount = mst.getDiscount();
                double netAmt = mst.getNetAmt();
                double subTotal = discount + netAmt;
                double qty =mst.getQty();

                brd = new BillReportDialog(BillReportActivity.this,dbHandler.getDateCount(mGetFromDate),dbHandler.getDateCount(mGetToDate),billNo,billDateTime,mProgressDialog,df.format(discount),netAmt,df.format(subTotal),qty);
                brd.show();


             //   selectedBill.setText("Bill No:"+String.valueOf(billNo));
             //   selectedBillDate.setText(billDateTime);
             //   tDiscount.setText("Discount: "+rs+ df.format(mst.getDiscount()));
              //  tNetAmount.setText("Net Amount: "+rs+ df.format(mst.getNetAmt()));
              //  tPaymentStatus.setText("Payment mode : "+mst.getPaymentMode());

/*
                getBillDetails(billNo,dbHandler.getDateCount(mGetFromDate),dbHandler.getDateCount(mGetToDate));

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("billDate", billDateTime);
                editor.putInt("billNo", billNo);
                editor.apply();
*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }





    public static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                //Log.v(TAG, "Permission is granted");
                return true;
            } else {
                //Toast.makeText(getApplicationContext(), "Permission is revoked",Toast.LENGTH_SHORT).show();
                //Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Toast.makeText(getApplicationContext(), "Permission is revoked",Toast.LENGTH_SHORT).show();
            //Log.v(TAG, "Permission is granted");
            return true;
        }
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


    public  void dismissDialog(){
        brd.dismiss();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excel, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
         //   FilterActivity.fad.clear();
            finish();
        }

        if(item.getItemId() == R.id.action_excel)
        {


            if(excelReport == null)
            {
                Toast.makeText(BillReportActivity.this,"Can't create Excel. No Reports found.",Toast.LENGTH_SHORT).show();
            }
            else {

                isStoragePermissionGranted(BillReportActivity.this);

                new AskPath(BillReportActivity.this,excelReport).show();

                Log.i("Success ", "yes");
            }
        }
        return super.onOptionsItemSelected(item);
    }






    public byte[] convertToExcel(String dis,String netAmt,String items,String qty) {
        String[] headerColumns = {"SNo", "Bill No", "Date", "Items", "Qty", "Discount(" + rs + ")", "Net Amount(" + rs + ")", "Pay mode", "Customer Name", "Cashier Name"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String[] summaryColumns = {"From Date", "To Date", "Total bills", "Total Items", "Total Qty", "Total Discount", "Total NetAmt"};
        File sd = Environment.getExternalStorageDirectory();
      //  String csvFile = "v1.xls";
        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        try {
            // File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(baos,wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Bill Report", 0);
            WritableSheet sheet1 = workbook.createSheet("Summary", 1);
            WritableCellFormat cFormat = new WritableCellFormat();
            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            font.setColour(Colour.BLUE);
            for (int i = 0; i < headerColumns.length; i++) {
                cFormat.setFont(font);
                sheet.addCell(new Label(i, 0, headerColumns[i], cFormat));
            }


            sheet.setColumnView(0, 6);
            sheet.setColumnView(1, 10);
            sheet.setColumnView(2, 17);
            sheet.setColumnView(3, 8);
            sheet.setColumnView(4, 8);
            sheet.setColumnView(5, 12);
            sheet.setColumnView(6, 15);
            sheet.setColumnView(7, 15);
            sheet.setColumnView(8, 15);
            sheet.setColumnView(9, 15);

            cFormat = new WritableCellFormat();
            cFormat.setAlignment(Alignment.RIGHT);


            for (int j = 0; j < mGetBillMaster.size(); j++)
            {
                SalesMst mst = mGetBillMaster.get(j);
                int k = j + 1;
                sheet.addCell(new Label(0, k, String.valueOf(k)));
                sheet.addCell(new Label(1, k, String.valueOf(mst.getBillNO())));
                sheet.addCell(new Label(2, k, mst.getDateTime()));
                sheet.addCell(new Label(3, k, String.valueOf(mst.getItems()), cFormat));
                sheet.addCell(new Label(4, k, String.valueOf((int) mst.getQty()), cFormat));
                sheet.addCell(new Label(5, k, df.format(mst.getDiscount()), cFormat));
                sheet.addCell(new Label(6, k, df.format(mst.getNetAmt()), cFormat));
                sheet.addCell(new Label(7, k, mst.getPaymentMode()));
                sheet.addCell(new Label(8, k, ""));
                sheet.addCell(new Label(9, k, ""));
            }

            WritableCellFormat cFormat1 = new WritableCellFormat();
            WritableFont font1 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            font1.setColour(Colour.RED);

            for (int k = 0; k < summaryColumns.length; k++) {
                //int j = f+k;
                cFormat1.setFont(font1);
                sheet1.addCell(new Label(k, 0, summaryColumns[k], cFormat1));
            }
            sheet1.setColumnView(0, 17);
            sheet1.setColumnView(1, 17);
            sheet1.setColumnView(2, 17);
            sheet1.setColumnView(3, 17);
            sheet1.setColumnView(4, 17);
            sheet1.setColumnView(5, 17);
            sheet1.setColumnView(6, 17);

            sheet1.addCell(new Label(0, 1, mGetFromDate));
            sheet1.addCell(new Label(1, 1, mGetToDate));
            sheet1.addCell(new Label(2, 1, String.valueOf(mGetBillMaster.size())));
            sheet1.addCell(new Label(3, 1, items));
            sheet1.addCell(new Label(4, 1, qty));
            sheet1.addCell(new Label(5, 1, dis));
            sheet1.addCell(new Label(6, 1, netAmt));
            workbook.write();
            workbook.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }



}




