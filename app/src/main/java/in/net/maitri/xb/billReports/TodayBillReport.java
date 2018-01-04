package in.net.maitri.xb.billReports;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.BillListAdapter;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.SalesMst;
import in.net.maitri.xb.itemdetails.RecyclerTouchListener;
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
 * Created by SYSRAJ4 on 28/12/2017.
 */

public class TodayBillReport extends AppCompatActivity {


    BillReportDialog brd;

    RecyclerView billView, billDetailsView;
    TextView noBills,noBillDetails,customerName;
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
    LinearLayout dateLayout;
    private List<SalesMst> mGetBillMaster;
    byte[] excelReport;
    BillListAdapter billListAdapter;
    DecimalFormat df;
    SimpleDateFormat dateFormat;
    String formattedDate;
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
        tBillCount = (TextView)findViewById(R.id.tBills);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbHandler = new DbHandler(TodayBillReport.this);
        summaryLayout = (LinearLayout) findViewById(R.id.summaryLayout);
        summaryLayout.setVisibility(View.GONE);
        dateLayout = (LinearLayout) findViewById(R.id.datelayout);
        dateLayout.setVisibility(View.GONE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        customerName =(TextView)findViewById(R.id.custName);

        formattedDate = dateFormat.format(new Date()).toString();
        Log.i("Formatted Date ",formattedDate);
        int dateCount = dbHandler.getDateCount(formattedDate);

        Log.i("dateCount ",String.valueOf(dateCount));

        mProgressDialog = new ProgressDialog(TodayBillReport.this);
        mProgressDialog.setMessage("Getting Data...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mGetBillMaster.clear();
        df = new DecimalFormat("0.00");

        mGetBillMaster = dbHandler.getAllBills(dateCount,dateCount);

        if(mGetBillMaster.size()==0)
        {
            Toast.makeText(TodayBillReport.this,"No Bills found",Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            return;
        }

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
        billMasterAdapter = new BillMasterAdapter(TodayBillReport.this, mGetBillMaster);
        billView.setAdapter(billMasterAdapter);
        tBillCount.setText("Bills:  "+String.valueOf(mGetBillMaster.size()));
        tDiscount.setText("Discount:  "+rs+ df.format(discount));
        tNetAmount.setText("Net Amount:  "+rs+ df.format(netAm));
        tItems.setText("Items:  "+String.valueOf(items));
        tQty.setText("Qty:  "+String.valueOf(qty));

        isStoragePermissionGranted(TodayBillReport.this);
        excelReport = convertToExcel(df.format(discount),df.format(netAm),String.valueOf(items),String.valueOf(qty));

        //  setHeader();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TodayBillReport.this);
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
                int internalBillNo = mst.getInternalBillNo();
                double discount = mst.getDiscount();
                double netAmt = mst.getNetAmt();
                double subTotal = discount + netAmt;
                String custName = mst.getCustName();
                String cashName = mst.getCashName();
                double qty =mst.getQty();
                brd = new BillReportDialog(TodayBillReport.this,dbHandler.getDateCount(mGetFromDate),dbHandler.getDateCount(mGetToDate),billNo,billDateTime,mProgressDialog,df.format(discount),netAmt,df.format(subTotal),qty,internalBillNo,custName,cashName);
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
                Toast.makeText(TodayBillReport.this,"Can't create Excel. No Reports found.",Toast.LENGTH_SHORT).show();
            }
            else {

                isStoragePermissionGranted(TodayBillReport.this);

                new AskPath(TodayBillReport.this,excelReport).show();

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
