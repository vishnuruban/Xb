package in.net.maitri.xb.billing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.DebugLog;
import com.epson.epos2.printer.Printer;
import com.reginald.editspinner.EditSpinner;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Customer;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.SalesDet;
import in.net.maitri.xb.db.SalesMst;
import in.net.maitri.xb.printing.AppConsts;
import in.net.maitri.xb.printing.CieBluetooth.BillPrint;
import in.net.maitri.xb.printing.FragmentMessageListener;
import in.net.maitri.xb.printing.epson.EpsonBillPrint;
import in.net.maitri.xb.printing.epson.ShowMsg;
import in.net.maitri.xb.printing.sunmi.Bluetooth;
import in.net.maitri.xb.printing.sunmi.BluetoothService;
import in.net.maitri.xb.printing.sunmi.SunmiPrint;
import in.net.maitri.xb.settings.GetSettings;
import in.net.maitri.xb.util.Calculation;
import in.net.maitri.xb.util.CheckDeviceType;

import static in.net.maitri.xb.printing.epson.EpsonConnection.initializeEpson;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener, FragmentMessageListener {

    private TextView cNetAmount;
    private TextView tCash;
    private TextView tBalance;
    private TextView mRoundOffAmtView, mDiscountText, mPaymentModeText, mTotalCashText;
    private LinearLayout mRoundOffLayout;
    private String totalProducts, totalPrice, tCustName;
    private RadioGroup cDiscountType;
    private String selectedButton;
    private float netAmt = 0, mRoundOffAmt = 0;
    private String rs = "\u20B9";
    private static TextView mPrinterStatusText;
    private EditText et_result, cCash, cDiscount, cCashierName;
    private EditSpinner mEditSpinner;
    private String cDiscountValue = "";
    private DecimalFormat df;
    private GetSettings getSettings;
    private DbHandler dbHandler;
    private SimpleDateFormat dateFormat;
    private SalesMst sm;
    private BillPrint billPrint;
    private GridLayout mKeyPad;
    public static CieBluetoothPrinter mPrinter = CieBluetoothPrinter.INSTANCE;
    private static Printer mEpsonConnection;
    private boolean isBillSaved = false;
    private ArrayList<BillItems> billList;

    public static final String mypreference = "mypref";
    public static final String billNo = "billNo";
    private BillSeries bSeries;
    private Customer chkCustomer;
    private Button mButtonOne, mButtonTwo, mButtonThree, mButtonFour, mButtonFive, mButtonSix, mButtonSeven,
            mButtonEight, mButtonNine, mButtonZero, mButtonPoint, mButtonClear, mButtonPrint, mButtonSave, mButtonCancel;

    Messenger mMessenger;
    private static String mConnectedDeviceName = "";
    public static final String title_connecting = "connecting...";
    public static final String title_connected_to = "connected: ";
    public static final String title_not_connected = "not connected";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mPaymentModeText = findViewById(R.id.cPaymentMode_text);
        mTotalCashText = findViewById(R.id.cCash_text);
        mRoundOffAmtView =  findViewById(R.id.roundOffValue);
        mRoundOffLayout = findViewById(R.id.roundOffLayout);
        mDiscountText = findViewById(R.id.discount_text);
        billList = FragmentOne.billList;
        getSettings = new GetSettings(CheckoutActivity.this);
        mPrinterStatusText = findViewById(R.id.cPrintstatus);
        cDiscount = findViewById(R.id.cDiscount);
        df = new DecimalFormat("0.00");
        cNetAmount = findViewById(R.id.cNetamount);
        ListView listView = findViewById(R.id.listview);
        mKeyPad = findViewById(R.id.keypad);
        TextView cPrice = findViewById(R.id.cTotalPrice);
        TextView cProducts = findViewById(R.id.cTotalProducts);
        final TextView cDate = findViewById(R.id.date);
        TextView cCustName = findViewById(R.id.cCustname);
        cCashierName = findViewById(R.id.cCashiername);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        cCashierName.setText(sharedPreferences.getString("current_user", ""));
        TextView cBillNum = findViewById(R.id.cBillNo);
        tCash = findViewById(R.id.tCash);
        cCash = findViewById(R.id.cCash);
        tBalance = findViewById(R.id.cBalance);
        tBalance.setVisibility(View.INVISIBLE);
        mPrinter.connectToPrinter();
        cDiscountType = findViewById(R.id.discount_toggle);
        mButtonCancel = findViewById(R.id.cCancel);
        billPrint = new BillPrint(CheckoutActivity.this);
        LinearLayout cashierNameLayout = findViewById(R.id.cashierLayout);

        dateFormat = new SimpleDateFormat("dd/MM/yy hh.mm a");

        mEditSpinner = findViewById(R.id.cPaymentMode);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.payment));
        mEditSpinner.setAdapter(adapter);
        mEditSpinner.setEditable(false);
        mEditSpinner.setText("CASH");
        SharedPreferences sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        dbHandler = new DbHandler(CheckoutActivity.this);

        bSeries = dbHandler.getBillSeries(1);


        if (bSeries.getCashierSelection().equals("NO")) {
            cashierNameLayout.setVisibility(View.GONE);
            cCashierName.setText("");
        }


        String bPrefix = String.valueOf(bSeries.getPrefix());
        if (bPrefix.isEmpty()) {
            cBillNum.setText(String.valueOf(bSeries.getCurrentBillNo()));
        } else {
            cBillNum.setText(bPrefix + String.valueOf(bSeries.getCurrentBillNo()));
        }


        try {
            byte[] utf8 = rs.getBytes("UTF-8");
            rs = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            rs = "Rs.";
        }
        initializeVars();
        int selectedId = cDiscountType.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
        selectedButton = selectedRadioButton.getText().toString();
        cDiscount.setText(selectedButton);
        Selection.setSelection(cDiscount.getText(), cDiscount.getText().length());

        BillListAdapter badapter = new BillListAdapter(CheckoutActivity.this, billList);
        listView.setAdapter(badapter);

        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        cDate.setText(date);
        cCustName.setText("");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            totalProducts = bundle.getString("products");
            totalPrice = bundle.getString("price");
        }
        chkCustomer = (Customer) getIntent().getSerializableExtra("customer");

        tCustName = chkCustomer.getName();
        if (tCustName.isEmpty()) {
            cCustName.setText("-");
        } else {
            cCustName.setText(tCustName);
        }

        netAmt = Float.parseFloat(totalPrice);
        roundOffCalculation();
        mEditSpinner.addTextChangedListener(paymentMode);
        cDiscount.addTextChangedListener(watch);
        cCash.addTextChangedListener(cash);
        cProducts.setText(totalProducts);
        cPrice.setText(FragmentOne.commaSeperated(Float.parseFloat(totalPrice)));
        cNetAmount.setText(rs + " " + FragmentOne.commaSeperated(netAmt));
        cCash.setText(df.format(netAmt));
        cDiscountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = cDiscountType.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
                selectedButton = selectedRadioButton.getText().toString();
                cDiscount.setText(selectedButton);
                Selection.setSelection(cDiscount.getText(), cDiscount.getText().length());
                getWindow().getDecorView().clearFocus();
                cDiscount.requestFocus();
                et_result = cDiscount;
            }
        });

        et_result = cDiscount;

        cDiscount.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                et_result = cDiscount;
                return true;
            }
        });


        cCash.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                et_result = cCash;
                return true;
            }
        });


        if (sharedpreferences == null) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(billNo, 0);
            editor.apply();
        }

        initializePrinter();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPrinter.onActivityResult(requestCode, resultCode, this);
    }

    @Override
    protected void onResume() {
        mPrinter.onActivityResume();
        //  printerSelection();
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


    public static void setStatusMsg(String msg) {
        mPrinterStatusText.setText(msg);
    }

    public void initializeVars() {
        mButtonOne = findViewById(R.id.btn_one);
        mButtonTwo = findViewById(R.id.btn_two);
        mButtonThree = findViewById(R.id.btn_three);
        mButtonFour = findViewById(R.id.btn_four);
        mButtonFive = findViewById(R.id.btn_five);
        mButtonSix = findViewById(R.id.btn_six);
        mButtonSeven = findViewById(R.id.btn_seven);
        mButtonEight = findViewById(R.id.btn_eight);
        mButtonNine = findViewById(R.id.btn_nine);
        mButtonZero = findViewById(R.id.btn_zero);
        mButtonPoint = findViewById(R.id.btn_point);
        mButtonClear = findViewById(R.id.btn_clear);
        mButtonPrint = findViewById(R.id.cPrint);
        mButtonSave = findViewById(R.id.cSave);
        mButtonCancel = findViewById(R.id.cCancel);
        mButtonPrint.setEnabled(false);
        mButtonPrint.setBackgroundColor(ContextCompat.getColor(CheckoutActivity.this, R.color.light_grey));
        mButtonOne.setOnClickListener(this);
        mButtonTwo.setOnClickListener(this);
        mButtonThree.setOnClickListener(this);
        mButtonFour.setOnClickListener(this);
        mButtonFive.setOnClickListener(this);
        mButtonSix.setOnClickListener(this);
        mButtonSeven.setOnClickListener(this);
        mButtonEight.setOnClickListener(this);
        mButtonNine.setOnClickListener(this);
        mButtonZero.setOnClickListener(this);
        mButtonPoint.setOnClickListener(this);
        mButtonClear.setOnClickListener(this);
        mButtonPrint.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (et_result != null) {
            switch (view.getId()) {
                case R.id.btn_one:
                    String one = et_result.getText().toString() + mButtonOne.getText().toString();
                    et_result.setText(one);
                    break;

                case R.id.btn_two:
                    String two = et_result.getText().toString() + mButtonTwo.getText().toString();
                    et_result.setText(two);
                    break;

                case R.id.btn_three:
                    String three = et_result.getText().toString() + mButtonThree.getText().toString();
                    et_result.setText(three);
                    break;

                case R.id.btn_four:
                    String four = et_result.getText().toString() + mButtonFour.getText().toString();
                    et_result.setText(four);
                    break;

                case R.id.btn_five:
                    String five = et_result.getText().toString() + mButtonFive.getText().toString();
                    et_result.setText(five);
                    break;

                case R.id.btn_six:
                    String six = et_result.getText().toString() + mButtonSix.getText().toString();
                    et_result.setText(six);
                    break;

                case R.id.btn_seven:
                    String seven = et_result.getText().toString() + mButtonSeven.getText().toString();
                    et_result.setText(seven);
                    break;

                case R.id.btn_eight:
                    String eight = et_result.getText().toString() + mButtonEight.getText().toString();
                    et_result.setText(eight);
                    break;

                case R.id.btn_nine:
                    String nine = et_result.getText().toString() + mButtonNine.getText().toString();
                    et_result.setText(nine);
                    break;

                case R.id.btn_zero:
                    String zero = et_result.getText().toString() + mButtonZero.getText().toString();
                    et_result.setText(zero);
                    break;

                case R.id.btn_point:
                    String point = et_result.getText().toString() + mButtonPoint.getText().toString();
                    et_result.setText(point);
                    break;

                case R.id.btn_clear:
                    et_result.setText("");
                    break;
                case R.id.cCancel:

                    if (mButtonCancel.getText().toString().equals("NEXT BILL")) {
                        Intent intent = new Intent(CheckoutActivity.this, BillingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                        builder.setTitle("Are you sure you want to cancel the bill?");
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(CheckoutActivity.this, BillingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });

                        builder.show();
                    }
                    break;

                case R.id.cSave:
                    if (cDiscountValue.isEmpty()) {
                        saveBill();
                    } else {
                        Float discountPerUnit = Float.parseFloat(cDiscountValue)/Float.parseFloat(totalPrice);
                        for (int i = 0; i < billList.size(); i++) {
                            BillItems billItems = billList.get(i);
                           new Calculation(CheckoutActivity.this).calculateInclusiveGst(billItems, billItems.getQty(), discountPerUnit);
                        }
                        saveBill();
                    }
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (sharedPreferences.getBoolean("isFirstTym", false)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isFirstTym", false);
                        editor.apply();
//                        mPrinter.showDeviceList(CheckoutActivity.this);
                    }
                    
                    isBillSaved = true;
                    mEditSpinner.setEnabled(false);
                    mEditSpinner.setClickable(false);
                    mKeyPad.setVisibility(View.GONE);
                    cDiscountType.setVisibility(View.INVISIBLE);
                    cDiscount.setVisibility(View.GONE);
                    mDiscountText.setVisibility(View.VISIBLE);
                    mDiscountText.setText("-" + df.format(sm.getDiscount()));
                    cCash.setVisibility(View.GONE);
                    mEditSpinner.setVisibility(View.GONE);
                    mPaymentModeText.setVisibility(View.VISIBLE);
                    mPaymentModeText.setText(mEditSpinner.getText().toString());
                    mTotalCashText.setVisibility(View.VISIBLE);
                    mTotalCashText.setText(cCash.getText().toString());
                    if (sm.getDiscount() ==0){
                        findViewById(R.id.discount_Layout).setVisibility(View.GONE);
                    }
                    if (!new CheckDeviceType(CheckoutActivity.this).isTablet()){
                        findViewById(R.id.bill_list_card).setVisibility(View.VISIBLE);
                        findViewById(R.id.linearL2).setVisibility(View.VISIBLE);
                    }
                   /* cDiscount.setEnabled(false);
                    cDiscount.setClickable(false);*/
                    break;
                case R.id.cPrint:

                    String printSize = getSettings.getPrintingPaperSize();
                    String pBillno;
                    if (sm.getPrefix().isEmpty()) {
                        pBillno = String.valueOf(sm.getBillNO());
                    } else {
                        pBillno = sm.getPrefix() + String.valueOf(sm.getBillNO());
                    }

                    switch (getSettings.getPrinterName()) {

                        case "1":
                            if (getSettings.getPrinterType().equals("1")) {
                                BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
                                if (mAdapter == null) {
                                    Toast.makeText(this, "Bluetooth not connected", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {

                                    try {
                                        mPrinter.initService(CheckoutActivity.this, mMessenger);
                                        if (printSize.equals("1")) {
                                            billPrint.printTwoInch(mPrinter, billList,
                                                    sm.getNetAmt(), pBillno, totalPrice, df.format(sm.getDiscount()),
                                                    sm.getQty(), sm.getDateTime(), sm.getCashName(), tCustName);
                                        } else {
                                            billPrint.printThreeInch(mPrinter, billList,
                                                    sm.getNetAmt(), pBillno, totalPrice, df.format(sm.getDiscount()),
                                                    sm.getQty(), sm.getDateTime(), sm.getCashName(), tCustName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            break;

                        case "2":
                            if (getSettings.getPrinterType().equals("2")) {
                                if (printSize.equals("2")) {
                                    new EpsonBillPrint(CheckoutActivity.this, billList, sm.getNetAmt(),
                                            pBillno, totalPrice, df.format(sm.getDiscount()), sm.getQty(),
                                            sm.getDateTime(), sm.getCashName(), tCustName).runPrintReceiptSequence();
                                  /*  Intent intent = new Intent(CheckoutActivity.this, BillingActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();*/
                                }
                            }
                            break;

                        case "3":
                            String regdType = new GetSettings(CheckoutActivity.this).getCompanyRegistrationType();
                            switch (regdType) {
                                case "1":
                                    if (getSettings.getPrinterType().equals("1")) {
                                        if (printSize.equals("1")) {
                                            if (Bluetooth.isPrinterConnected(CheckoutActivity.this, CheckoutActivity.this)) {
                                                BluetoothService mService = Bluetooth.getServiceInstance();
                                                if (!mService.isAvailable()) {
                                                    Toast.makeText(CheckoutActivity.this, "Unable to connect printer", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(CheckoutActivity.this, "Printing", Toast.LENGTH_SHORT).show();
                                                    new SunmiPrint(CheckoutActivity.this, mService, billList, sm.getNetAmt(),
                                                            pBillno, totalPrice, df.format(sm.getDiscount()), sm.getQty(),
                                                            sm.getDateTime(), sm.getCashName(), tCustName, mRoundOffAmt).doPrintForInclusiveGst();
                                                }

                                            } else {
                                                Toast.makeText(CheckoutActivity.this, "No printer found. Add printer in settings.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    break;
                                case "2":
                                    break;
                                case "3":
                                    if (getSettings.getPrinterType().equals("1")) {
                                        if (printSize.equals("1")) {
                                            if (Bluetooth.isPrinterConnected(CheckoutActivity.this, CheckoutActivity.this)) {
                                                BluetoothService mService = Bluetooth.getServiceInstance();
                                                if (!mService.isAvailable()) {
                                                    Toast.makeText(CheckoutActivity.this, "Unable to connect printer", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(CheckoutActivity.this, "Printing", Toast.LENGTH_SHORT).show();
                                                    new SunmiPrint(CheckoutActivity.this, mService, billList, sm.getNetAmt(),
                                                            pBillno, totalPrice, df.format(sm.getDiscount()), sm.getQty(),
                                                            sm.getDateTime(), sm.getCashName(), tCustName, mRoundOffAmt).doPrint();
                                                }

                                            } else {
                                                Toast.makeText(CheckoutActivity.this, "No printer found. Add printer in settings.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    break;
                            }

                            break;
                    }

                    break;
            }
        }
    }

    private void initializePrinter() {
        setStatusMsg("Connecting printer...");
        switch (getSettings.getPrinterName()) {
            case "1":
                if (getSettings.getPrinterType().equals("1")) {
                    mMessenger = new Messenger(new PrintSrvMsgHandler());
                }
                break;

            case "2":
                if (getSettings.getPrinterType().equals("2")) {
                    mEpsonConnection = initializeEpson();
                    setStatusMsg("Printer connected");
                }
                break;
            case "3":
                checkPrinter();
                break;
        }

    }

    public void saveBill() {
        long detInserted = 0, mstInserted;
        int bNo = bSeries.getCurrentBillNo();
        dbHandler.updateBillNo(++bNo);
        float quantity = 0;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String formattedDate = dateFormat.format(new Date()).toString();
        int dateCount = dbHandler.getDateCount(date);
        BigDecimal totCgst = BigDecimal.valueOf(0), totSgst = BigDecimal.valueOf(0), totAmt = BigDecimal.valueOf(0);
        for (int i = 0; i < billList.size(); i++) {
            BillItems billItems = billList.get(i);
            quantity = quantity + billItems.getQty();
            SalesDet sd = new SalesDet(bSeries.getCurrentBillNo(), billItems);
            sd.setDateTime(formattedDate);
            detInserted = dbHandler.addSalesDet(sd);
            totCgst = totCgst.add(BigDecimal.valueOf(billItems.getTaxAmt1()))
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN);
            totSgst = totSgst.add(BigDecimal.valueOf(billItems.getTaxAmt2()))
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN);
            totAmt = totAmt.add(BigDecimal.valueOf(billItems.getTaxSaleAmt()))
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }

        sm = new SalesMst();
        sm.setBillNO(bSeries.getCurrentBillNo());
        sm.setCustomerId(0);
        sm.setQty(quantity);
        sm.setNetAmt(netAmt);
        if (cDiscountValue.isEmpty())
            sm.setDiscount(0);
        else
            sm.setDiscount(Float.parseFloat(cDiscountValue));
        sm.setPaymentMode(mEditSpinner.getText().toString());
        sm.setPaymentDet("");
        sm.setCashName(cCashierName.getText().toString());
        sm.setSalesPerson("");
        sm.setStatus("SAVED");
        sm.setPrefix(bSeries.getPrefix());
        sm.setDate(dateCount);
        sm.setDateTime(formattedDate);
        sm.setItems(billList.size());
        sm.setCustName(tCustName);
        sm.setCustomerId(chkCustomer.getId());
        sm.setRoundOff(mRoundOffAmt);
        sm.setTaxAmt1(totCgst.floatValue());
        sm.setTaxAmt2(totSgst.floatValue());
        sm.setGstNetAmt(totAmt.floatValue());
        String regdType = new GetSettings(CheckoutActivity.this).getCompanyRegistrationType();
        switch (regdType){
            case "1":
                sm.setTaxType("GS");
                break;
            case "2":
                break;
            case "3":
                sm.setTaxType("");
                break;
        }
        mstInserted = dbHandler.addSalesMst(sm);
        if (detInserted != -1 && mstInserted != -1) {
            mButtonPrint.setEnabled(true);
            mButtonPrint.setBackgroundColor(ContextCompat.getColor(CheckoutActivity.this, R.color.green));
            mButtonSave.setVisibility(View.GONE);
            mButtonSave.setBackgroundColor(ContextCompat.getColor(CheckoutActivity.this, R.color.light_grey));
            mButtonCancel.setVisibility(View.VISIBLE);
            mButtonCancel.setBackgroundColor(ContextCompat.getColor(CheckoutActivity.this, R.color.colorPrimary));
            String nextBill = "NEXT BILL";
            mButtonCancel.setText(nextBill);
            Toast.makeText(CheckoutActivity.this, "Bill Saved Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Intent intent = new Intent(CheckoutActivity.this, BillingActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(CheckoutActivity.this, "Error saving bill", Toast.LENGTH_SHORT).show();
        }
    }

    TextWatcher paymentMode = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().equals("CASH")) {
                tCash.setText("Cash (" + rs + ")");
                cCash.setText(df.format(netAmt));
                cCash.selectAll();
                cCash.setFocusable(true);
                cCash.setFocusableInTouchMode(true);
                cCash.setClickable(true);
//                et_result = cCash;
            } else {
                tCash.setText("Amount (" + rs + ")");
                cCash.setText(df.format(netAmt));
                cCash.setFocusable(false);
                cCash.setClickable(false);
//                et_result = null;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
        }
    };


    TextWatcher cash = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            String searchString = s.toString();
            int textLength = searchString.length();
            cCash.setSelection(textLength);
            if (s.toString().equals("")) {
                tBalance.setVisibility(View.INVISIBLE);
            } else {
                if (mEditSpinner.getText().toString().equals("CASH")) {
                    if (Float.parseFloat(s.toString()) > netAmt) {
                        tBalance.setVisibility(View.VISIBLE);
                        float balance = Float.parseFloat(s.toString()) - netAmt;
                        tBalance.setText("Balance  " + rs + " " + FragmentOne.commaSeperated(balance));
                    } else {
                        tBalance.setVisibility(View.INVISIBLE);
                    }
                } else {
                    tBalance.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
        }
    };

    public void roundOffCalculation(){
        GetSettings mGetSettings = new GetSettings(CheckoutActivity.this);
        int roundOffUpto = Integer.parseInt(mGetSettings.getRoundOffUpto());
        if (!mGetSettings.getRoundOffDirection().equals("3")) {
            if (roundOffUpto != 0) {
                float roundOffInRupees = BigDecimal.valueOf(roundOffUpto / 100)
                        .setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
                Log.d("roundOffInRupees", String.valueOf(roundOffInRupees));
                float extraAmt = BigDecimal.valueOf(netAmt % roundOffInRupees)
                        .setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
                Log.d("extraAmt", String.valueOf(extraAmt));
                switch (mGetSettings.getRoundOffDirection()) {
                    case "1":
                        if (extraAmt != 0) {
                            float amountToBeAdd = BigDecimal.valueOf(roundOffInRupees - extraAmt)
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
                            netAmt += amountToBeAdd;
                            mRoundOffAmt = amountToBeAdd;
                        } else {
                            mRoundOffAmt = 0;
                        }
                        break;
                    case "2":
                        netAmt -= extraAmt;
                        mRoundOffAmt = 0 - extraAmt;
                        break;
                }
            }
            if (mRoundOffAmt == 0) {
                mRoundOffLayout.setVisibility(View.GONE);
                mRoundOffAmtView.setText(String.valueOf(df.format(mRoundOffAmt)));
            } else {
                mRoundOffLayout.setVisibility(View.VISIBLE);
                mRoundOffAmtView.setText(String.valueOf(df.format(mRoundOffAmt)));
            }
            String amtText = rs + " " + FragmentOne.commaSeperated(netAmt);
            cNetAmount.setText(amtText);
        } else {
            mRoundOffLayout.setVisibility(View.GONE);
            mRoundOffAmtView.setText(String.valueOf(df.format(mRoundOffAmt)));
        }
    }

    TextWatcher watch = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            String searchString = s.toString();
            int textLength = searchString.length();
            cDiscount.setSelection(textLength);
            if (!s.toString().startsWith(selectedButton)) {
                cDiscount.setText(selectedButton);
                Selection.setSelection(cDiscount.getText(), cDiscount.getText().length());
                cDiscountValue = "";
            }
            if (s.toString().equals("") || s.toString().equals(rs) || s.toString().equals("%")) {
                cNetAmount.setText(rs + " " + FragmentOne.commaSeperated(Float.parseFloat(totalPrice)));
                netAmt = Float.parseFloat(totalPrice);
                roundOffCalculation();
                cCash.setText(String.valueOf(netAmt));
                cDiscountValue = "";
            } else {
                char disSymbl = s.charAt(0);
                String ss = Character.toString(disSymbl);
                if (ss.equals(rs)) {
                    System.out.println("S " + s);
                    if (s.toString().substring(1).startsWith(".")) {
                        Toast.makeText(CheckoutActivity.this, "Enter valid discount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.out.println("SS " + s.toString().substring(1));
                    netAmt = Float.parseFloat(totalPrice) - Float.parseFloat(s.toString().substring(1));
                    cDiscountValue = df.format(Float.parseFloat(s.toString().substring(1)));
                    
                } else {
                    String disPrice = s.toString().substring(1);
                    float discount =  BigDecimal.valueOf((Float.parseFloat(disPrice) / 100.0) * Float.parseFloat(totalPrice))
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
                    cDiscountValue = df.format(discount);
                    netAmt = Float.parseFloat(totalPrice) - discount;
                    
                }
                if (netAmt <= 0) {
                    Toast.makeText(CheckoutActivity.this, "Please enter valid discount", Toast.LENGTH_SHORT).show();
                    cDiscount.setText("");
                    cDiscountValue = "";
                    return;
                }
                roundOffCalculation();
                cNetAmount.setText(rs + " " + FragmentOne.commaSeperated(netAmt));
                tCash.setText("Cash (" + rs + ")");
                cCash.setText(String.valueOf(netAmt));
                String cash = "CASH";
                mEditSpinner.setText(cash);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!isBillSaved) {
                finish();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!isBillSaved) {
            finish();
        }
    }

    @Override
    public void onAppSignal(int iAppSignal) {
        switch (iAppSignal) {
            case AppConsts.CLEAR_PREFERRED_PRINTER:
                mPrinter.clearPreferredPrinter();
                break;

        }
    }

    @Override
    public void onAppSignal(int iAppSignal, String data) {
    }

    @Override
    public void onAppSignal(int iAppSignal, boolean data) {
    }

    @Override
    public void onAppSignal(int iAppSignal, byte[] data) {

    }

    private static class PrintSrvMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CieBluetoothPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case CieBluetoothPrinter.STATE_CONNECTED:
                            setStatusMsg("Printer Status :" + title_connected_to + mConnectedDeviceName);
                            break;
                        case CieBluetoothPrinter.STATE_CONNECTING:
                            setStatusMsg("Printer Status :" + title_connected_to + title_connecting);
                            break;
                        case CieBluetoothPrinter.STATE_LISTEN:
                            setStatusMsg("Printer Status :" + title_connected_to + title_connecting);

                        case CieBluetoothPrinter.STATE_NONE:
                            setStatusMsg("Printer Status :" + title_not_connected);
                    }
                    break;
                case CieBluetoothPrinter.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(
                            CieBluetoothPrinter.DEVICE_NAME);
                    break;
                case CieBluetoothPrinter.MESSAGE_STATUS:
                    DebugLog.logTrace("Message Status Received");
                    setStatusMsg(msg.getData().getString(
                            CieBluetoothPrinter.STATUS_TEXT));
                    break;
                case CieBluetoothPrinter.PRINT_COMPLETE:
                    setStatusMsg("PRINT OK");
                    break;
                case CieBluetoothPrinter.PRINTER_CONNECTION_CLOSED:
                    setStatusMsg("PRINT CONN CLOSED");
                    break;
                case CieBluetoothPrinter.PRINTER_DISCONNECTED:
                    setStatusMsg("PRINT CONN FAILED");
                    break;
                default:
                    DebugLog.logTrace("Some un handled message : " + msg.what);
                    super.handleMessage(msg);
            }
        }
    }

    private void checkPrinter() {
        GetSettings mGetSettings = new GetSettings(CheckoutActivity.this);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            setStatusMsg("Device does not support bluetooth");
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                if (!mGetSettings.getBluetoothPairedDevice().isEmpty()) {
                    Bluetooth.pairedPrinterAddress(getApplicationContext(), CheckoutActivity.this, mGetSettings.getBluetoothPairedDevice());
                    setStatusMsg("Printer connected.");
                } else {
                    setStatusMsg("No printer found. Add printer in settings.");
                }
            } else {
                switchBluetooth(mBluetoothAdapter);
            }
        }
    }

    private void switchBluetooth(final BluetoothAdapter mBluetoothAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
        builder.setMessage("Appplication need to turn on Bluetooth?");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mBluetoothAdapter.enable();
                checkPrinter();
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
