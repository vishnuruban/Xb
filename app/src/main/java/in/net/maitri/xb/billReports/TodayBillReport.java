package in.net.maitri.xb.billReports;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.DebugLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.net.maitri.xb.R;
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

public class TodayBillReport extends AppCompatActivity {

    private BillReportDialog brd;
    private BillMasterAdapter billMasterAdapter;
    private String mGetToDate = "", mGetFromDate = "";
    private DbHandler dbHandler;
    private ProgressDialog mProgressDialog;
    private List<SalesMst> mGetBillMaster;
    byte[] excelReport;
    private DecimalFormat df;
    private String rs;
    public static CieBluetoothPrinter mPrinter = CieBluetoothPrinter.INSTANCE;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_report);
        mGetBillMaster = new ArrayList<>();

        rs = "\u20B9";
        try {
            byte[] utf8 = rs.getBytes("UTF-8");
            rs = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(this, "Bluetooth not connected", Toast.LENGTH_SHORT).show();
            finish();
        }
        try {
            mPrinter.initService(TodayBillReport.this, mMessenger);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RecyclerView billView = (RecyclerView) findViewById(R.id.bill_view);
        TextView tItems = (TextView) findViewById(R.id.tItems);
        TextView tQty = (TextView) findViewById(R.id.tqty);
        TextView tDiscount = (TextView) findViewById(R.id.tDiscount);
        TextView tNetAmount = (TextView) findViewById(R.id.tNetAmt);
        TextView tBillCount = (TextView) findViewById(R.id.tBills);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbHandler = new DbHandler(TodayBillReport.this);
        LinearLayout summaryLayout = (LinearLayout) findViewById(R.id.summaryLayout);
        summaryLayout.setVisibility(View.GONE);
        LinearLayout dateLayout = (LinearLayout) findViewById(R.id.datelayout);
        dateLayout.setVisibility(View.GONE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TextView customerName = (TextView) findViewById(R.id.custName);

        String formattedDate = dateFormat.format(new Date()).toString();
        Log.i("Formatted Date ", formattedDate);
        int dateCount = dbHandler.getDateCount(formattedDate);

        Log.i("dateCount ", String.valueOf(dateCount));

        mProgressDialog = new ProgressDialog(TodayBillReport.this);
        mProgressDialog.setMessage("Getting Data...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mGetBillMaster.clear();
        df = new DecimalFormat("0.00");

        mGetBillMaster = dbHandler.getAllBills(dateCount, dateCount);

        if (mGetBillMaster.size() == 0) {
            Toast.makeText(TodayBillReport.this, "No Bills found", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            return;
        }

        summaryLayout.setVisibility(View.VISIBLE);
        int items = 0, qty = 0;
        double discount = 0;
        double netAm = 0;
        mProgressDialog.dismiss();
        for (int i = 0; i < mGetBillMaster.size(); i++) {
            SalesMst bm = mGetBillMaster.get(i);
            items = items + bm.getItems();
            qty = qty + (int) bm.getQty();
            discount = discount + bm.getDiscount();
            netAm = netAm + bm.getNetAmt();
        }
        billMasterAdapter = new BillMasterAdapter(TodayBillReport.this, mGetBillMaster);
        billView.setAdapter(billMasterAdapter);
        tBillCount.setText("Bills:  " + String.valueOf(mGetBillMaster.size()));
        tDiscount.setText("Discount:  " + rs + df.format(discount));
        tNetAmount.setText("Net Amount:  " + rs + df.format(netAm));
        tItems.setText("Items:  " + String.valueOf(items));
        tQty.setText("Qty:  " + String.valueOf(qty));

        isStoragePermissionGranted(TodayBillReport.this);
        excelReport = convertToExcel(df.format(discount), df.format(netAm), String.valueOf(items), String.valueOf(qty));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TodayBillReport.this);
        billView.setLayoutManager(linearLayoutManager);

        billView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), billView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                billMasterAdapter.setSelected(position);
                SalesMst mst = mGetBillMaster.get(position);
                mProgressDialog.show();
                int billNo = mst.getBillNO();
                String billDateTime = mst.getDateTime();
                int internalBillNo = mst.getInternalBillNo();
                double discount = mst.getDiscount();
                double netAmt = mst.getNetAmt();
                double subTotal = discount + netAmt;
                String custName = mst.getCustName();
                String cashName = mst.getCashName();
                double qty = mst.getQty();
                brd = new BillReportDialog(TodayBillReport.this, dbHandler.getDateCount(mGetFromDate), dbHandler.getDateCount(mGetToDate), billNo, billDateTime, mProgressDialog, df.format(discount), netAmt, df.format(subTotal), qty, internalBillNo, custName, cashName, mMessenger, mPrinter);
                brd.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    public static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
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
            finish();
        }

        if (item.getItemId() == R.id.action_excel) {

            if (excelReport == null) {
                Toast.makeText(TodayBillReport.this, "Can't create Excel. No Reports found.", Toast.LENGTH_SHORT).show();
            } else {
                isStoragePermissionGranted(TodayBillReport.this);
                new AskPath(TodayBillReport.this, excelReport).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPrinter.onActivityResult(requestCode, resultCode, this);
    }

    @Override
    protected void onResume() {
        mPrinter.onActivityResume();
        super.onResume();
    }


    @Override
    protected void onPause() {
        mPrinter.onActivityPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPrinter.onActivityDestroy();
        super.onDestroy();
    }


    final Messenger mMessenger = new Messenger(new PrintSrvMsgHandler());
    private String mConnectedDeviceName = "";
    public static final String title_connecting = "connecting...";
    public static final String title_connected_to = "connected: ";
    public static final String title_not_connected = "not connected";


    class PrintSrvMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CieBluetoothPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case CieBluetoothPrinter.STATE_CONNECTED:
                            //  setStatusMsg("Printer Status :"+title_connected_to + mConnectedDeviceName);
                            Toast.makeText(TodayBillReport.this, title_connected_to + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                            //    tbPrinter.setText("ON");
                            //  tbPrinter.setChecked(true);
                            break;
                        case CieBluetoothPrinter.STATE_CONNECTING:
                            //  setStatusMsg("Printer Status :"+title_connected_to + title_connecting);
                            Toast.makeText(TodayBillReport.this, title_connected_to + title_connecting, Toast.LENGTH_SHORT).show();
                            try {
                                //    tbPrinter.setText("...");
                                //   tbPrinter.setChecked(false);
                            } catch (NullPointerException e) {
                                DebugLog.logTrace("Fragment creating");
                            }
                            break;
                        case CieBluetoothPrinter.STATE_LISTEN:
                            // setStatusMsg("Printer Status :"+title_connected_to + title_connecting);
                            //  Toast.makeText(CheckoutActivity.this,title_connected_to + title_connecting,Toast.LENGTH_SHORT).show();

                        case CieBluetoothPrinter.STATE_NONE:
                            //    setStatusMsg("Printer Status :"+title_not_connected);
                            //   Toast.makeText(CheckoutActivity.this, title_not_connected, Toast.LENGTH_SHORT).show();
                            try {
                                // tbPrinter.setText("OFF");
                                // tbPrinter.setChecked(false);
                            } catch (NullPointerException n) {
                                DebugLog.logTrace("Fragment creating");
                            }
                            break;
                    }
                    break;
                case CieBluetoothPrinter.MESSAGE_DEVICE_NAME:
                    //  mConnectedDeviceName = msg.getData().getString(
                    //    CieBluetoothPrinter.DEVICE_NAME);
                    break;
                case CieBluetoothPrinter.MESSAGE_STATUS:
                    DebugLog.logTrace("Message Status Received");
                    Toast.makeText(TodayBillReport.this, msg.getData().getString(
                            CieBluetoothPrinter.STATUS_TEXT), Toast.LENGTH_SHORT).show();
                    //   setStatusMsg(msg.getData().getString(CieBluetoothPrinter.STATUS_TEXT));
                    break;
                case CieBluetoothPrinter.PRINT_COMPLETE:
                    Toast.makeText(TodayBillReport.this, "PRINT OK", Toast.LENGTH_SHORT).show();
                    //setStatusMsg("PRINT OK");
                    break;
                case CieBluetoothPrinter.PRINTER_CONNECTION_CLOSED:
                    // setStatusMsg("PRINT CONN CLOSED");
                    Toast.makeText(TodayBillReport.this, "PRINT CONN CLOSED", Toast.LENGTH_SHORT).show();
                    break;
                case CieBluetoothPrinter.PRINTER_DISCONNECTED:
                    //  setStatusMsg("PRINT CONN FAILED");
                    Toast.makeText(TodayBillReport.this, "PRINT CONN FAILED", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    DebugLog.logTrace("Some un handled message : " + msg.what);
                    super.handleMessage(msg);
            }
        }
    }


    public byte[] convertToExcel(String dis, String netAmt, String items, String qty) {
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
            workbook = Workbook.createWorkbook(baos, wbSettings);
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


            for (int j = 0; j < mGetBillMaster.size(); j++) {
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
                sheet.addCell(new Label(8, k, mst.getCustName()));
                sheet.addCell(new Label(9, k, mst.getCashName()));
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
