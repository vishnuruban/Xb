package in.net.maitri.xb.billing;

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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.DebugLog;
import com.reginald.editspinner.EditSpinner;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Customer;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.SalesDet;
import in.net.maitri.xb.db.SalesMst;
import in.net.maitri.xb.printing.AppConsts;
import in.net.maitri.xb.printing.CieBluetooth.BillPrint;
import in.net.maitri.xb.printing.FragmentMessageListener;
import in.net.maitri.xb.printing.epson.EpsonBillPrint;
import in.net.maitri.xb.settings.GetSettings;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener, FragmentMessageListener {

    private TextView cNetAmount;
    private TextView tCash;
    private TextView tBalance;
    private String totalProducts, totalPrice, tCustName;
    private RadioGroup cDiscountType;
    private String selectedButton;
    private double netAmt = 0;
    private String rs = "\u20B9";
    private TextView cPrintStatus;
    private EditText et_result, cCash, cDiscount, cCashierName;
    private EditSpinner mEditSpinner;
    private String cDiscountValue = "";
    private DecimalFormat df;
    private GetSettings getSettings;
    private DbHandler dbHandler;
    private SimpleDateFormat dateFormat;
    private SalesMst sm;
    private BillPrint billPrint;
    public static CieBluetoothPrinter mPrinter = CieBluetoothPrinter.INSTANCE;

    public static final String mypreference = "mypref";
    public static final String billNo = "billNo";
    private BillSeries bSeries;
    private Customer chkCustomer;
    private Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven,
            btn_eight, btn_nine, btn_zero, btn_point, btn_clear, cPrint, cSave, cCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        cDiscount = (EditText) findViewById(R.id.cDiscount);
        df = new DecimalFormat("0.00");
        cNetAmount = (TextView) findViewById(R.id.cNetamount);
        ListView listView = (ListView) findViewById(R.id.listview);

        TextView cPrice = (TextView) findViewById(R.id.cTotalPrice);
        TextView cProducts = (TextView) findViewById(R.id.cTotalProducts);
        final TextView cDate = (TextView) findViewById(R.id.date);
        TextView cCustName = (TextView) findViewById(R.id.cCustname);
        cCashierName = (EditText) findViewById(R.id.cCashiername);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        cCashierName.setText(sharedPreferences.getString("current_user", ""));
        TextView cBillNum = (TextView) findViewById(R.id.cBillNo);
        tCash = (TextView) findViewById(R.id.tCash);
        cCash = (EditText) findViewById(R.id.cCash);
        tBalance = (TextView) findViewById(R.id.cBalance);
        cPrintStatus = (TextView) findViewById(R.id.cPrintstatus);
        tBalance.setVisibility(View.INVISIBLE);
        mPrinter.connectToPrinter();
        cDiscountType = (RadioGroup) findViewById(R.id.discount_toggle);
        cCancel = (Button) findViewById(R.id.cCancel);
        billPrint = new BillPrint(CheckoutActivity.this);
        LinearLayout cashierNameLayout = (LinearLayout) findViewById(R.id.cashierLayout);

        dateFormat = new SimpleDateFormat("dd/MM/yy hh.mm a");

        mEditSpinner = (EditSpinner) findViewById(R.id.cPaymentMode);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.payment));
        mEditSpinner.setAdapter(adapter);
        mEditSpinner.setEditable(false);
        mEditSpinner.setText("CASH");
        SharedPreferences sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        getSettings = new GetSettings(CheckoutActivity.this);
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

        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(this, "Bluetooth not connected", Toast.LENGTH_SHORT).show();
            finish();
        }

        try {
            mPrinter.initService(CheckoutActivity.this, mMessenger);
        } catch (Exception e) {
            e.printStackTrace();
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

        BillListAdapter badapter = new BillListAdapter(CheckoutActivity.this, FragmentOne.billList);
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

        netAmt = Double.parseDouble(totalPrice);
        mEditSpinner.addTextChangedListener(paymentMode);
        cDiscount.addTextChangedListener(watch);
        cCash.addTextChangedListener(cash);
        cProducts.setText(totalProducts);
        cPrice.setText(FragmentOne.commaSeperated(Double.parseDouble(totalPrice)));
        cNetAmount.setText(rs + " " + FragmentOne.commaSeperated(Double.parseDouble(totalPrice)));
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
                //et_result = cDiscount;
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
           /*     if (mEditSpinner.getText().toString().equals("Cash")) {
                    et_result = cCash;
                } else {
                    et_result = cDiscount;
                }*/
                return true;
            }
        });


        if (sharedpreferences == null) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(billNo, 0);
            editor.apply();
        }
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


    final Messenger mMessenger = new Messenger(new PrintSrvMsgHandler());
    private String mConnectedDeviceName = "";
    public static final String title_connecting = "connecting...";
    public static final String title_connected_to = "connected: ";
    public static final String title_not_connected = "not connected";

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

    class PrintSrvMsgHandler extends Handler {
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


    public void setStatusMsg(String msg) {
        cPrintStatus.setText(msg);
    }

    public void initializeVars() {
        btn_one = (Button) findViewById(R.id.btn_one);
        btn_two = (Button) findViewById(R.id.btn_two);
        btn_three = (Button) findViewById(R.id.btn_three);
        btn_four = (Button) findViewById(R.id.btn_four);
        btn_five = (Button) findViewById(R.id.btn_five);
        btn_six = (Button) findViewById(R.id.btn_six);
        btn_seven = (Button) findViewById(R.id.btn_seven);
        btn_eight = (Button) findViewById(R.id.btn_eight);
        btn_nine = (Button) findViewById(R.id.btn_nine);
        btn_zero = (Button) findViewById(R.id.btn_zero);
        btn_point = (Button) findViewById(R.id.btn_point);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        cPrint = (Button) findViewById(R.id.cPrint);
        cSave = (Button) findViewById(R.id.cSave);
        cCancel = (Button) findViewById(R.id.cCancel);
        cPrint.setEnabled(false);
        cPrint.setBackgroundColor(ContextCompat.getColor(CheckoutActivity.this, R.color.light_grey));
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_six.setOnClickListener(this);
        btn_seven.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_nine.setOnClickListener(this);
        btn_zero.setOnClickListener(this);
        btn_point.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        cPrint.setOnClickListener(this);
        cSave.setOnClickListener(this);
        cCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (et_result != null) {
            switch (view.getId()) {
                case R.id.btn_one:
                    String one = et_result.getText().toString() + btn_one.getText().toString();
                    et_result.setText(one);
                    break;

                case R.id.btn_two:
                    String two = et_result.getText().toString() + btn_two.getText().toString();
                    et_result.setText(two);
                    break;

                case R.id.btn_three:
                    String three = et_result.getText().toString() + btn_three.getText().toString();
                    et_result.setText(three);
                    break;

                case R.id.btn_four:
                    String four = et_result.getText().toString() + btn_four.getText().toString();
                    et_result.setText(four);
                    break;

                case R.id.btn_five:
                    String five = et_result.getText().toString() + btn_five.getText().toString();
                    et_result.setText(five);
                    break;

                case R.id.btn_six:
                    String six = et_result.getText().toString() + btn_six.getText().toString();
                    et_result.setText(six);
                    break;

                case R.id.btn_seven:
                    String seven = et_result.getText().toString() + btn_seven.getText().toString();
                    et_result.setText(seven);
                    break;

                case R.id.btn_eight:
                    String eight = et_result.getText().toString() + btn_eight.getText().toString();
                    et_result.setText(eight);
                    break;

                case R.id.btn_nine:
                    String nine = et_result.getText().toString() + btn_nine.getText().toString();
                    et_result.setText(nine);
                    break;

                case R.id.btn_zero:
                    String zero = et_result.getText().toString() + btn_zero.getText().toString();
                    et_result.setText(zero);
                    break;

                case R.id.btn_point:
                    String point = et_result.getText().toString() + btn_point.getText().toString();
                    et_result.setText(point);
                    break;

                case R.id.btn_clear:
                    et_result.setText("");
                    break;
                case R.id.cCancel:

                    if (cCancel.getText().toString().equals("NEXT BILL")) {
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
                    saveBill();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (sharedPreferences.getBoolean("isFirstTym", false)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isFirstTym", false);
                        editor.apply();
                        mPrinter.showDeviceList(CheckoutActivity.this);
                    }
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
                                if (printSize.equals("1")) {
                                    billPrint.printTwoInch(mPrinter, FragmentOne.billList,
                                            sm.getNetAmt(), pBillno, totalPrice, df.format(sm.getDiscount()),
                                            sm.getQty(), sm.getDateTime(), sm.getCashName(), tCustName);
                                } else {
                                    billPrint.printThreeInch(mPrinter, FragmentOne.billList,
                                            sm.getNetAmt(), pBillno, totalPrice, df.format(sm.getDiscount()),
                                            sm.getQty(), sm.getDateTime(), sm.getCashName(), tCustName);
                                }
                            }
                            break;

                        case "2":
                            if (getSettings.getPrinterType().equals("2")) {
                                if (printSize.equals("2")) {
                                    new EpsonBillPrint(CheckoutActivity.this, FragmentOne.billList, sm.getNetAmt(),
                                            pBillno, totalPrice, df.format(sm.getDiscount()), sm.getQty(),
                                            sm.getDateTime(), sm.getCashName(), tCustName).runPrintReceiptSequence();
                                    Intent intent = new Intent(CheckoutActivity.this, BillingActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            break;
                    }

                    break;
            }
        }
    }

    public void saveBill() {
        long detInserted = 0, mstInserted;
        int bNo = bSeries.getCurrentBillNo();
        dbHandler.updateBillNo(++bNo);
        int quantity = 0;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String formattedDate = dateFormat.format(new Date()).toString();
        int dateCount = dbHandler.getDateCount(date);
        for (int i = 0; i < FragmentOne.billList.size(); i++) {
            BillItems billItems = FragmentOne.billList.get(i);
            quantity = quantity + billItems.getQty();
            SalesDet sd = new SalesDet(bSeries.getCurrentBillNo(), billItems);
            sd.setDateTime(formattedDate);
            detInserted = dbHandler.addSalesDet(sd);
        }

        sm = new SalesMst();
        sm.setBillNO(bSeries.getCurrentBillNo());
        sm.setCustomerId(0);
        sm.setQty(quantity);
        sm.setNetAmt(netAmt);
        if (cDiscountValue.isEmpty())
            sm.setDiscount(0);
        else
            sm.setDiscount(Double.parseDouble(cDiscountValue));
        sm.setPaymentMode(mEditSpinner.getText().toString());
        sm.setPaymentDet("");
        sm.setCashName(cCashierName.getText().toString());
        sm.setSalesPerson("");
        sm.setStatus("SAVED");
        sm.setPrefix(bSeries.getPrefix());
        sm.setDate(dateCount);
        sm.setDateTime(formattedDate);
        sm.setItems(FragmentOne.billList.size());
        sm.setCustName(tCustName);
        sm.setCustomerId(chkCustomer.getId());
        mstInserted = dbHandler.addSalesMst(sm);
        if (detInserted != -1 && mstInserted != -1) {
            cPrint.setEnabled(true);
            cPrint.setBackgroundColor(ContextCompat.getColor(CheckoutActivity.this, R.color.green));
            cSave.setEnabled(false);
            cSave.setBackgroundColor(ContextCompat.getColor(CheckoutActivity.this, R.color.light_grey));
            cCancel.setVisibility(View.VISIBLE);
            String nextBill = "NEXT BILL";
            cCancel.setText(nextBill);
            Toast.makeText(CheckoutActivity.this, "Bill Saved!", Toast.LENGTH_SHORT).show();

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
                et_result = cCash;
            } else {
                tCash.setText("Amount (" + rs + ")");
                cCash.setText(df.format(netAmt));
                cCash.setFocusable(false);
                cCash.setClickable(false);
                et_result = null;
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
                    if (Double.parseDouble(s.toString()) > netAmt) {
                        tBalance.setVisibility(View.VISIBLE);
                        double balance = Double.parseDouble(s.toString()) - netAmt;
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
                cNetAmount.setText(rs + " " + FragmentOne.commaSeperated(Double.parseDouble(totalPrice)));
                netAmt = Double.parseDouble(totalPrice);
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
                    netAmt = Double.parseDouble(totalPrice) - Double.parseDouble(s.toString().substring(1));
                    cDiscountValue = df.format(Double.parseDouble(s.toString().substring(1)));
                } else {
                    String disPrice = s.toString().substring(1);
                    double discount = (Double.parseDouble(disPrice) / 100.0) * Double.parseDouble(totalPrice);
                    cDiscountValue = df.format(discount);
                    netAmt = Double.parseDouble(totalPrice) - discount;
                }
                if (netAmt <= 0) {
                    Toast.makeText(CheckoutActivity.this, "Please enter valid discount", Toast.LENGTH_SHORT).show();
                    cDiscount.setText("");
                    cDiscountValue = "";
                    return;
                }
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
            finish();
        }
        return true;
    }
}
