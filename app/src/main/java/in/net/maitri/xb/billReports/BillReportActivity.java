package in.net.maitri.xb.billReports;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.DebugLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.CheckoutActivity;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.SalesMst;
import in.net.maitri.xb.itemdetails.RecyclerTouchListener;

import in.net.maitri.xb.settings.GetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static in.net.maitri.xb.printing.epson.EpsonConnection.initializeEpson;

public class BillReportActivity extends AppCompatActivity {

    private BillReportDialog brd;
    private RecyclerView billView;
    private TextView tItems, tQty, tDiscount, tNetAmount, tBillCount;
    private BillMasterAdapter billMasterAdapter;
    private EditText mFromDate, mToDate;
    private int mYear, mMonth, mDay, mMinYear, mMinMonth, mMinDay;
    private String thisDate, mDate, mGetToDate = "", mGetFromDate = "";
    private String[] mDayOfWeak = {"Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"};

    private DbHandler dbHandler;
    private ProgressDialog mProgressDialog;
    private LinearLayout summaryLayout;
    private List<SalesMst> mGetBillMaster;
    private byte[] excelReport;
    private DecimalFormat df;
    public static CieBluetoothPrinter mPrinter = CieBluetoothPrinter.INSTANCE;
    private String rs;
    private Messenger mMessenger;
    private String mConnectedDeviceName = "";
    public static final String title_connecting = "connecting...";
    public static final String title_connected_to = "connected: ";
    public static final String title_not_connected = "not connected";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_report);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mGetBillMaster = new ArrayList<>();

        rs = "\u20B9";
        try {
            byte[] utf8 = rs.getBytes("UTF-8");
            rs = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            rs = "Rs.";
        }


        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(this, "Bluetooth not connected", Toast.LENGTH_SHORT).show();
            finish();
        }


        initializePrinter();

        billView = findViewById(R.id.bill_view);
        tItems = findViewById(R.id.tItems);
        tQty = findViewById(R.id.tqty);
        tDiscount = findViewById(R.id.tDiscount);
        tNetAmount = findViewById(R.id.tNetAmt);
        // tPaymentStatus = (TextView)findViewById(R.id.t);
        tBillCount = findViewById(R.id.tBills);
        summaryLayout = findViewById(R.id.summaryLayout);
        summaryLayout.setVisibility(View.GONE);
        LinearLayout mShowReport = findViewById(R.id.showReport);
        LinearLayout mFromDateLayout = findViewById(R.id.from_date_layout);
        LinearLayout mToDateLayout = findViewById(R.id.to_date_layout);
        mFromDate = findViewById(R.id.from_date);
        mToDate = findViewById(R.id.to_date);
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
                summaryLayout.setVisibility(View.GONE);
                if (mGetFromDate.isEmpty() || mGetToDate.isEmpty()) {
                    Toast.makeText(BillReportActivity.this, "Select date range", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressDialog = new ProgressDialog(BillReportActivity.this);
                    mProgressDialog.setMessage("Getting Data...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    mGetBillMaster.clear();
                    dbHandler = new DbHandler(BillReportActivity.this);
                    List<SalesMst> list = dbHandler.getAllBills(dbHandler.getDateCount(mGetFromDate), dbHandler.getDateCount(mGetToDate));
                    mGetBillMaster.addAll(list);
                    billMasterAdapter.notifyDataSetChanged();

                    if (mGetBillMaster.size() == 0) {
                        Toast.makeText(BillReportActivity.this, "No Bills found", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        return;
                    }
                    summaryLayout.setVisibility(View.VISIBLE);
                    int items = 0, qty = 0;
                    float discount = 0;
                    float netAm = 0;
                    mProgressDialog.dismiss();
                    for (int i = 0; i < mGetBillMaster.size(); i++) {
                        SalesMst bm = mGetBillMaster.get(i);
                        items = items + bm.getItems();
                        qty = qty + (int) bm.getQty();
                        discount = discount + bm.getDiscount();
                        netAm = netAm + bm.getNetAmt();

                    }

                    String text = "Bills:  " + String.valueOf(mGetBillMaster.size());
                    tBillCount.setText(text);
                    tDiscount.setText("Discount:  " + rs + df.format(discount));
                    tNetAmount.setText("Net Amount:  " + rs + df.format(netAm));
                    text = "Items:  " + String.valueOf(items);
                    tItems.setText(text);
                    text = "Qty:  " + String.valueOf(qty);
                    tQty.setText(text);

                    isStoragePermissionGranted(BillReportActivity.this);
                    excelReport = convertToExcel(df.format(discount), df.format(netAm), String.valueOf(items), String.valueOf(qty));

                }
            }
        });

        df = new DecimalFormat("0.00");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BillReportActivity.this);
        billView.setLayoutManager(linearLayoutManager);
        billMasterAdapter = new BillMasterAdapter(BillReportActivity.this, mGetBillMaster);
        billView.setAdapter(billMasterAdapter);
        billView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), billView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                billMasterAdapter.setSelected(position);
                SalesMst mst = mGetBillMaster.get(position);
                mProgressDialog.show();
                int billNo = mst.getBillNO();
                String custName = mst.getCustName();
                String cashName = mst.getCashName();
                int internalBillNo = mst.getInternalBillNo();
                String billDateTime = mst.getDateTime();
                float discount = mst.getDiscount();
                float netAmt = mst.getNetAmt();
                float subTotal = discount + netAmt;
                float qty = mst.getQty();

                brd = new BillReportDialog(BillReportActivity.this, dbHandler.getDateCount(mGetFromDate), dbHandler.getDateCount(mGetToDate), billNo, billDateTime, mProgressDialog, df.format(discount), netAmt, df.format(subTotal), qty, internalBillNo, custName, cashName, mMessenger, mPrinter);
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

    private void initializePrinter() {
        GetSettings getSettings = new GetSettings(BillReportActivity.this);
        switch (getSettings.getPrinterName()) {
            case "1":
                if (getSettings.getPrinterType().equals("1")) {
                    mMessenger = new Messenger(new BillReportActivity.PrintSrvMsgHandler());
                }
                try {
                    mPrinter.initService(BillReportActivity.this, mMessenger);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "2":
                if (getSettings.getPrinterType().equals("2")) {
                    initializeEpson();
                }
                break;
        }

    }


    public void dismissDialog() {
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
            finish();
        }

        if (item.getItemId() == R.id.action_excel) {
            if (excelReport == null) {
                Toast.makeText(BillReportActivity.this, "Can't create Excel. No Reports found.", Toast.LENGTH_SHORT).show();
            } else {
                isStoragePermissionGranted(BillReportActivity.this);
                new AskPath(BillReportActivity.this, excelReport).show();
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


    class PrintSrvMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CieBluetoothPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case CieBluetoothPrinter.STATE_CONNECTED:

                            Toast.makeText(BillReportActivity.this,
                                    title_connected_to + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                            break;
                        case CieBluetoothPrinter.STATE_CONNECTING:
                            Toast.makeText(BillReportActivity.this,
                                    title_connected_to + title_connecting, Toast.LENGTH_SHORT).show();
                            break;
                        case CieBluetoothPrinter.STATE_LISTEN:
                            break;

                        case CieBluetoothPrinter.STATE_NONE:
                            break;
                    }
                    break;
                case CieBluetoothPrinter.MESSAGE_DEVICE_NAME:

                    break;
                case CieBluetoothPrinter.MESSAGE_STATUS:
                    DebugLog.logTrace("Message Status Received");
                    Toast.makeText(BillReportActivity.this, msg.getData().getString(
                            CieBluetoothPrinter.STATUS_TEXT), Toast.LENGTH_SHORT).show();
                    break;
                case CieBluetoothPrinter.PRINT_COMPLETE:
                    Toast.makeText(BillReportActivity.this, "PRINT OK", Toast.LENGTH_SHORT).show();
                    break;
                case CieBluetoothPrinter.PRINTER_CONNECTION_CLOSED:
                    Toast.makeText(BillReportActivity.this, "PRINT CONN CLOSED", Toast.LENGTH_SHORT).show();
                    break;
                case CieBluetoothPrinter.PRINTER_DISCONNECTED:
                    Toast.makeText(BillReportActivity.this, "PRINT CONN FAILED", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    DebugLog.logTrace("Some un handled message : " + msg.what);
                    super.handleMessage(msg);
            }
        }
    }


    public byte[] convertToExcel(String dis, String netAmt, String items, String qty) {
        String[] headerColumns = {"SNo", "Bill No", "Date", "Items", "Qty", "Discount(" + rs + ")",
                "Net Amount(" + rs + ")", "Pay mode", "Customer Name", "Cashier Name"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String[] summaryColumns = {"From Date", "To Date", "Total bills", "Total Items", "Total Qty",
                "Total Discount", "Total NetAmt"};
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