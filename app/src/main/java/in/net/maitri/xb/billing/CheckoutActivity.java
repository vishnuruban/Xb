package in.net.maitri.xb.billing;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cie.btp.BtpConsts;
import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.DebugLog;
import com.cie.btp.PrinterWidth;
import com.reginald.editspinner.EditSpinner;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.SalesDet;
import in.net.maitri.xb.db.SalesMst;
import in.net.maitri.xb.printing.AppConsts;
import in.net.maitri.xb.printing.FragmentMessageListener;
import in.net.maitri.xb.settings.GetSettings;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener, FragmentMessageListener {

    ListView listView;
    BillListAdapter badapter;
    TextView cProducts, cPrice, cDate, cNetAmount, cPayment, tCash, tBalance;
    EditText cDiscount;
    String totalProducts, totalPrice;
    LinearLayout lNetAmt;
    RadioGroup cDiscountType;
    String selectedButton;
    double netAmt = 0;
    String rs = "\u20B9";
    TextView cPrintStatus;
    EditText et_result, cCash;
    EditSpinner mEditSpinner;
    String cDiscountValue = "";
    DecimalFormat df;
    GetSettings getSettings;
    DbHandler dbHandler;
    SimpleDateFormat dateFormat;
    String formattedDate;
    SalesDet sd;
    SalesMst sm;

    public static CieBluetoothPrinter mPrinter = CieBluetoothPrinter.INSTANCE;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String billNo = "billNo";


    String[] pModes = {"CASH", "DEBIT CARD", "CREDIT CARD", "WALLET"};
    Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven,
            btn_eight, btn_nine, btn_zero, btn_point, btn_ok, btn_cancel, btn_clear, btn_100, btn_500, btn_2000, btn_cash, btn_dc, btn_cc, btn_wallet, cPrint, cSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity);

        cDiscount = (EditText) findViewById(R.id.cDiscount);
        //   cDiscount.setInputType(InputType.TYPE_NULL);

        df = new DecimalFormat("0.00");
        cNetAmount = (TextView) findViewById(R.id.cNetamount);
        listView = (ListView) findViewById(R.id.listview);

        cPrice = (TextView) findViewById(R.id.cTotalPrice);
        cProducts = (TextView) findViewById(R.id.cTotalProducts);
        cDate = (TextView) findViewById(R.id.date);
        cPayment = (TextView) findViewById(R.id.cPayment);
        lNetAmt = (LinearLayout) findViewById(R.id.layout_Net);
        tCash = (TextView) findViewById(R.id.tCash);
        cCash = (EditText) findViewById(R.id.cCash);
        tBalance = (TextView) findViewById(R.id.cBalance);
        cPrintStatus = (TextView) findViewById(R.id.cPrintstatus);
        tBalance.setVisibility(View.INVISIBLE);
        mPrinter.connectToPrinter();
        cDiscountType = (RadioGroup) findViewById(R.id.discount_toggle);

        dateFormat = new SimpleDateFormat("dd/MM/yy hh.mm a");

        mEditSpinner = (EditSpinner) findViewById(R.id.cPaymentMode);
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.payment));
        mEditSpinner.setAdapter(adapter);
        mEditSpinner.setEditable(false);
        mEditSpinner.setText("CASH");
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        getSettings = new GetSettings(CheckoutActivity.this);
        dbHandler = new DbHandler(CheckoutActivity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(this, "Bluetooth not connected", Toast.LENGTH_SHORT).show();
            finish();
        }

        //un comment the line below to debug the print service
        //mPrinter.setDebugService(BuildConfig.DEBUG);
        try {
            mPrinter.initService(CheckoutActivity.this, mMessenger);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            byte[] utf8 = rs.getBytes("UTF-8");

            rs = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        initializeVars();
        int selectedId = cDiscountType.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
        selectedButton = selectedRadioButton.getText().toString();
        cDiscount.setText(selectedButton);
        Selection.setSelection(cDiscount.getText(), cDiscount.getText().length());

        GradientDrawable bgShape = (GradientDrawable) lNetAmt.getBackground();
        bgShape.setColor(getResources().getColor(R.color.green));
        badapter = new BillListAdapter(CheckoutActivity.this, FragmentOne.billList);
        listView.setAdapter(badapter);

        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        cDate.setText(date);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            totalProducts = bundle.getString("products");
            totalPrice = bundle.getString("price");
        }
        netAmt = Double.parseDouble(totalPrice);
        mEditSpinner.addTextChangedListener(paymentMode);
        cDiscount.addTextChangedListener(watch);
        cCash.addTextChangedListener(cash);
        cProducts.setText(totalProducts);
        cPrice.setText(FragmentOne.commaSeperated(Double.parseDouble(totalPrice)));
        cNetAmount.setText(rs + " " + FragmentOne.commaSeperated(Double.parseDouble(totalPrice)));
        cPayment.setText("PAYMENT - " + rs + " " + FragmentOne.commaSeperated(Double.parseDouble(totalPrice)));

        cDiscountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                int selectedId = cDiscountType.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
                selectedButton = selectedRadioButton.getText().toString();
                cDiscount.setText(selectedButton);
                Selection.setSelection(cDiscount.getText(), cDiscount.getText().length());
                cDiscount.requestFocus();
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
            editor.putInt(billNo, 1);
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
                // tbPrinter.setText("OFF");
                //   tbPrinter.setChecked(false);
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
                            setStatusMsg(title_connected_to + mConnectedDeviceName);
                            //Toast.makeText(CheckoutActivity.this,title_connected_to + mConnectedDeviceName,Toast.LENGTH_SHORT).show();
                            //    tbPrinter.setText("ON");
                            //  tbPrinter.setChecked(true);
                            break;
                        case CieBluetoothPrinter.STATE_CONNECTING:
                            setStatusMsg(title_connected_to + title_connecting);
                            //Toast.makeText(CheckoutActivity.this,title_connected_to + title_connecting,Toast.LENGTH_SHORT).show();
                            try {
                                //    tbPrinter.setText("...");
                                //   tbPrinter.setChecked(false);
                            } catch (NullPointerException e) {
                                DebugLog.logTrace("Fragment creating");
                            }
                            break;
                        case CieBluetoothPrinter.STATE_LISTEN:
                            setStatusMsg(title_connected_to + title_connecting);
                            //  Toast.makeText(CheckoutActivity.this,title_connected_to + title_connecting,Toast.LENGTH_SHORT).show();

                        case CieBluetoothPrinter.STATE_NONE:
                            setStatusMsg(title_not_connected);
                            Toast.makeText(CheckoutActivity.this, title_not_connected, Toast.LENGTH_SHORT).show();

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
                    mConnectedDeviceName = msg.getData().getString(
                            CieBluetoothPrinter.DEVICE_NAME);
                    break;
                case CieBluetoothPrinter.MESSAGE_STATUS:
                    DebugLog.logTrace("Message Status Received");
                    //      Toast.makeText(CheckoutActivity.this,msg.getData().getString(
                    //   CieBluetoothPrinter.STATUS_TEXT),Toast.LENGTH_SHORT).show();
                    setStatusMsg(msg.getData().getString(
                            CieBluetoothPrinter.STATUS_TEXT));
                    break;
                case CieBluetoothPrinter.PRINT_COMPLETE:
                    //Toast.makeText(CheckoutActivity.this,"PRINT OK",Toast.LENGTH_SHORT).show();
                    setStatusMsg("PRINT OK");
                    break;
                case CieBluetoothPrinter.PRINTER_CONNECTION_CLOSED:
                    setStatusMsg("PRINT CONN CLOSED");
                    // Toast.makeText(CheckoutActivity.this,"PRINT CONN CLOSED",Toast.LENGTH_SHORT).show();
                    break;
                case CieBluetoothPrinter.PRINTER_DISCONNECTED:
                    setStatusMsg("PRINT CONN FAILED");
                    //    Toast.makeText(CheckoutActivity.this,"PRINT CONN FAILED",Toast.LENGTH_SHORT).show();
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
        btn_100 = (Button) findViewById(R.id.btn_100);
        btn_500 = (Button) findViewById(R.id.btn_500);
        btn_2000 = (Button) findViewById(R.id.btn_2000);
        btn_cash = (Button) findViewById(R.id.btn_cash);
        btn_dc = (Button) findViewById(R.id.btn_dc);
        btn_cc = (Button) findViewById(R.id.btn_cc);
        btn_wallet = (Button) findViewById(R.id.btn_wallet);
//        cPrint = (Button) findViewById(R.id.cPrint);
        cSave = (Button) findViewById(R.id.cSave);

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
        btn_100.setOnClickListener(this);
        btn_500.setOnClickListener(this);
        btn_2000.setOnClickListener(this);
        btn_cash.setOnClickListener(this);
        btn_dc.setOnClickListener(this);
        btn_cc.setOnClickListener(this);
        btn_wallet.setOnClickListener(this);
//        cPrint.setOnClickListener(this);
        cSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_one:
                et_result.setText(et_result.getText().toString() + btn_one.getText().toString());
                break;

            case R.id.btn_two:
                et_result.setText(et_result.getText().toString() + btn_two.getText().toString());
                break;

            case R.id.btn_three:
                et_result.setText(et_result.getText().toString() + btn_three.getText().toString());
                break;

            case R.id.btn_four:
                et_result.setText(et_result.getText().toString() + btn_four.getText().toString());
                break;

            case R.id.btn_five:
                et_result.setText(et_result.getText().toString() + btn_five.getText().toString());
                break;

            case R.id.btn_six:
                et_result.setText(et_result.getText().toString() + btn_six.getText().toString());
                break;

            case R.id.btn_seven:
                et_result.setText(et_result.getText().toString() + btn_seven.getText().toString());
                break;

            case R.id.btn_eight:
                et_result.setText(et_result.getText().toString() + btn_eight.getText().toString());
                break;

            case R.id.btn_nine:
                et_result.setText(et_result.getText().toString() + btn_nine.getText().toString());
                break;

            case R.id.btn_zero:
                et_result.setText(et_result.getText().toString() + btn_zero.getText().toString());
                break;

            case R.id.btn_point:
                et_result.setText(et_result.getText().toString() + btn_point.getText().toString());
                break;
            case R.id.btn_100:

                et_result.setText(et_result.getText().toString() + btn_100.getText().toString());
                break;
            case R.id.btn_500:
                et_result.setText("");
                et_result.setText(et_result.getText().toString() + btn_500.getText().toString());
                break;
            case R.id.btn_2000:
                et_result.setText("");
                et_result.setText(et_result.getText().toString() + btn_2000.getText().toString());
                break;
            case R.id.btn_clear:
                et_result.setText("");
                break;
            case R.id.btn_cash:
                mEditSpinner.setText("CASH");
                break;
            case R.id.btn_dc:
                mEditSpinner.setText("DEBIT CARD");
                break;
            case R.id.btn_cc:
                mEditSpinner.setText("CREDIT CARD");
                break;
            case R.id.btn_wallet:
                mEditSpinner.setText("WALLET");
                break;
            case R.id.cSave:
                saveBill();
                //mPrinter.showDeviceList(CheckoutActivity.this);
                break;
        }
    }

    public void saveBill() {
        long detInserted = 0, mstInserted = 0;
        int billNum = sharedpreferences.getInt(billNo, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(billNo, ++billNum);
        editor.apply();
        String billno = String.valueOf(++billNum);
        Log.i("BILL NO", billno);
        int quantity = 0;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        formattedDate = dateFormat.format(new Date()).toString();
        int dateCount = dbHandler.getDateCount(date);

        System.out.println("DATE " + formattedDate);

        for (int i = 0; i < FragmentOne.billList.size(); i++) {


            BillItems billItems = FragmentOne.billList.get(i);
            quantity = quantity + billItems.getQty();
            sd = new SalesDet(Integer.parseInt(billno), billItems);
            detInserted = dbHandler.addSalesDet(sd);
        }

        sm = new SalesMst();
        sm.setBillNO(Integer.parseInt(billno));
        sm.setCustomerId(0);
        sm.setQty(quantity);
        sm.setNetAmt(netAmt);
        if (cDiscountValue.isEmpty())
            sm.setDiscount(0);
        else
            sm.setDiscount(Double.parseDouble(cDiscountValue));
        sm.setPaymentMode(mEditSpinner.getText().toString());
        sm.setPaymentDet("");
        sm.setSalesPerson("");
        sm.setStatus("SAVED");
        sm.setDate(dateCount);
        sm.setDateTime(formattedDate);

        mstInserted = dbHandler.addSalesMst(sm);

        Log.i("MST", String.valueOf(mstInserted));
        Log.i("DET", String.valueOf(detInserted));

        if (detInserted != -1 && mstInserted != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Bill saved. Do you want to print? ");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String printSize = getSettings.getPrintingPaperSize();
                            // String printSize1 = getResources().getString((R.array.paper_size_name)[printSize]);
                            if (printSize.equals("1")) {
                                printTwoInch();
                            } else {
                                printThreeInch();
                            }
                            Intent intent = new Intent(CheckoutActivity.this, BillingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }


                    }
            );
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(CheckoutActivity.this, BillingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
            );
            builder.show();

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
                cCash.setText("");
                cCash.setFocusable(true);
                cCash.setFocusableInTouchMode(true);
                cCash.setClickable(true);

            } else {
                tCash.setText("Amount (" + rs + ")");
                cCash.setText(FragmentOne.commaSeperated(netAmt));
                cCash.setFocusable(false);
                cCash.setClickable(false);
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
                cPayment.setText("PAYMENT - " + rs + " " + FragmentOne.commaSeperated(Double.parseDouble(totalPrice)));
                netAmt = Double.parseDouble(totalPrice);
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
                    double discount = (Double.parseDouble(disPrice.toString()) / 100.0) * Double.parseDouble(totalPrice);
                    cDiscountValue = df.format(discount);
                    netAmt = Double.parseDouble(totalPrice) - discount;
                }
                if (netAmt <= 0) {
                    Toast.makeText(CheckoutActivity.this, "Please enter valid discount", Toast.LENGTH_SHORT).show();
                    cDiscount.setText("");
                    cDiscountValue = "";
                    //     cPayment.setText("PAYMENT - " + rs + " " + FragmentOne.commaSeperated(netAmt));
                    return;
                }
                cNetAmount.setText(rs + " " + FragmentOne.commaSeperated(netAmt));
                cPayment.setText("PAYMENT - " + rs + " " + FragmentOne.commaSeperated(netAmt));
                tCash.setText("Cash (" + rs + ")");
                cCash.setText("");
                mEditSpinner.setText("CASH");
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


    private void printThreeInch() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh.mm a");
        String formattedDate = dateFormat.format(new Date()).toString();
        NumberFormat nf = new DecimalFormat("##.##");
        double quantity = 0;
        mPrinter.setPrintMode(BtpConsts.PRINT_IN_BATCH);
        mPrinter.resetPrinter();
        mPrinter.setPrinterWidth(PrinterWidth.PRINT_WIDTH_72MM);
        mPrinter.setHighIntensity();
        headerPrint();
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        mPrinter.printTextLine("CASH BILL\n");
        mPrinter.setRegular();
       // String custName = "test customer";
       // String cashName = "test cashier";
        String s = String.format("%-15s%8s%12s%12s\n", "Item", "Qty", "Price", "Amount");
        String s2 = String.format("%-15s%8s%12s%12s\n", "Aachi Masala 1K", "2kg", "4500.00", "9000.00");
        String total = String.format("%-15s%8s%9s%15s\n", "Total", "", "", "Rs." + FragmentOne.commaSeperated(netAmt));
        mPrinter.setAlignmentLeft();
        String billNo = String.format("%-10s%8s%9s%15s\n", "Bill No", sm.getBillNO(), "", formattedDate);
        mPrinter.printTextLine(billNo);
       // mPrinter.printTextLine(" Customer Name  : " + custName + "\n");
        //mPrinter.printTextLine(" Cashier Name   : " + cashName + "\n");
        mPrinter.printTextLine("------------------------------------------------\n");
        mPrinter.setBold();
        mPrinter.printTextLine(s);
        mPrinter.printTextLine("------------------------------------------------\n");
        mPrinter.setRegular();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        for (int i = 0; i < FragmentOne.billList.size(); i++) {
            BillItems billItems = FragmentOne.billList.get(i);
            quantity = quantity + billItems.getQty();
            if (billItems.getDesc().length() <= 15) {
                String sa = String.format("%-15s%8s%12s%12s\n", billItems.getDesc(), billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
                mPrinter.printTextLine(sa);
            } else {
                String sa1 = String.format("%-15s%8s%12s%12s\n", billItems.getDesc(), "", "", "");
                String sa2 = String.format("%-15s%8s%12s%12s\n", "", billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
                mPrinter.printTextLine(sa1);
                mPrinter.printTextLine(sa2);
            }
        }
        String discount = "";
        String qtyNetAmt = String.format("%-15s%8s%10s%14s\n", "Items : " + FragmentOne.billList.size(), "", "Subtotal", totalPrice);
        if (cDiscountValue.equals("")) {
            discount = String.format("%-15s%8s%10s%14s\n", "Qty   : " + nf.format(quantity), "", "", "");
        } else {
            discount = String.format("%-15s%8s%10s%14s\n", "Qty   : " + nf.format(quantity), "", "Discount", cDiscountValue);
        }
        mPrinter.printTextLine("------------------------------------------------\n");
        mPrinter.printTextLine(qtyNetAmt);
        mPrinter.printTextLine(discount);
        mPrinter.printLineFeed();
        mPrinter.printTextLine("------------------------------------------------\n");
        mPrinter.setBold();
        mPrinter.printTextLine(total);
        mPrinter.printTextLine("------------------------------------------------\n");
        mPrinter.printLineFeed();
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        GetSettings getSettings = new GetSettings(CheckoutActivity.this);
        String[] footerArray = {getSettings.getFooterText1(), getSettings.getFooterText2(),
                getSettings.getFooterText3(), getSettings.getFooterText4()};
        for (String aFooterArray : footerArray) {
            if (!aFooterArray.isEmpty()) {
                mPrinter.printTextLine( aFooterArray + "\n");
                mPrinter.printLineFeed();
            }
        }
        mPrinter.setRegular();
        mPrinter.printTextLine("************************************************\n");
        mPrinter.printLineFeed();
        mPrinter.printLineFeed();
        mPrinter.setAlignmentCenter();
        mPrinter.printLineFeed();
        mPrinter.printLineFeed();
        mPrinter.resetPrinter();
        //print all commands
        mPrinter.batchPrint();
    }


    public void headerPrint() {
        String city = getSettings.getCompanyCity();
        String pincode = getSettings.getCompanyPincode();
        String cityPin = city + "-" + pincode;
        String phNumber = "PH:" + getSettings.getCompanyPhoneNo();
        String[] billHeader = {getSettings.getCompanyAddressLine1(),
                getSettings.getCompanyAddressLine2(), getSettings.getCompanyAddressLine3(),
                cityPin, phNumber, getSettings.getCompanyGstNo() };
        String clName = getSettings.getCompanyLegalName();
        String ctName = getSettings.getCompanyTradeName();
        if (ctName.isEmpty()) {
            Toast.makeText(CheckoutActivity.this, "Company name not present in settings", Toast.LENGTH_SHORT).show();
            return;
        }
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        // mPrinter.setFontSizeXSmall();
        //   mPrinter.printTextLine("\n"+clName+"\n");
        mPrinter.printTextLine("\n" + ctName + "\n");
        mPrinter.setRegular();
        for ( String aBillHeader:billHeader){
            if (!aBillHeader.isEmpty()){
                mPrinter.printTextLine(aBillHeader + "\n");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void printTwoInch() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh.mm a");
        String formattedDate = dateFormat.format(new Date()).toString();
        NumberFormat nf = new DecimalFormat("##.##");
        double quantity = 0;
        String clName = getSettings.getCompanyLegalName();
        String ctName = getSettings.getCompanyTradeName();
        String addLine1 = getSettings.getCompanyAddressLine1();
        String addLine2 = getSettings.getCompanyAddressLine2();
        String addLine3 = getSettings.getCompanyAddressLine3();
        String city = getSettings.getCompanyCity();
        String pincode = getSettings.getCompanyPincode();
        String cityPin = city + "-" + pincode;
        String phNum = getSettings.getCompanyPhoneNo();
        String gstin = getSettings.getCompanyGstNo();
        //String custName = "test customer";
        //String cashName = "test cashier";
        if (ctName.isEmpty()) {
            Toast.makeText(CheckoutActivity.this, "Company name not present in settings", Toast.LENGTH_SHORT).show();
            return;
        }
        String s = String.format("%12s%-10s%5s%9s%10s\n", "", "Item", "Qty", "Rate", "Amount");
        String total = String.format("%12s%-10s%5s%9s%10s\n", "", "Total", "", "", "Rs." + FragmentOne.commaSeperated(netAmt));
        String billNo = String.format("%12s%-7s%-5s%4s%15s\n", "", "Bill No:", sm.getBillNO(), "", formattedDate);
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        mPrinter.setFontSizeXSmall();
        //   mPrinter.printTextLine("\n"+clName+"\n");
        mPrinter.printTextLine("\n           " + ctName + "\n");
        mPrinter.setRegular();
        if (!addLine1.isEmpty())
            mPrinter.printTextLine("            " + addLine1 + "\n");
        if (!addLine2.isEmpty())
            mPrinter.printTextLine("            " + addLine2 + "\n");
        if (!addLine3.isEmpty())
            mPrinter.printTextLine("            " +
                    addLine3 + "\n");
        if (!cityPin.isEmpty())
            mPrinter.printTextLine("            " + cityPin + "\n");
        if (!phNum.isEmpty())
            mPrinter.printTextLine("            " + "PH:" + phNum + "\n");
        mPrinter.printLineFeed();
        mPrinter.setBold();
        mPrinter.printTextLine("            CASH BILL\n");
        mPrinter.printLineFeed();
        mPrinter.setRegular();
        mPrinter.setAlignmentLeft();
        mPrinter.printTextLine(billNo);
        //mPrinter.printTextLine("            Customer Name  : " + custName + "\n");
       // mPrinter.printTextLine("            Cashier Name   : " + cashName + "\n");
        mPrinter.printLineFeed();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.setBold();
        mPrinter.printTextLine(s);
        mPrinter.setRegular();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        for (int i = 0; i < FragmentOne.billList.size(); i++) {
            BillItems billItems = FragmentOne.billList.get(i);
            quantity = quantity + billItems.getQty();
            //  if (billItems.getDesc().length() <= 10) {
            //     String sa = String.format("12s%-13s%3s%9s%10s\n", "",billItems.getDesc(), billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
            //      mPrinter.printTextLine(sa);
            //   } else {
            String sa1 = String.format("%12s%-20s%4s%5s%5s\n", "", billItems.getDesc(), "", "", "");
            String sa2 = String.format("%12s%-10s%5s%9s%10s\n", "", "", billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
            mPrinter.printTextLine(sa1);
            mPrinter.printLineFeed();
            mPrinter.printTextLine(sa2);
            // }
        }

        String discount = "";
        String qtyNetAmt = String.format("%12s%-10s%5s%9s%10s\n", "", "Items : " + FragmentOne.billList.size(), "", "Subtotal", totalPrice);
        System.out.println("cDiscountValue " + cDiscountType);
        if (cDiscountValue.isEmpty()) {
            discount = String.format("%12s%-10s%5s%9s%10s\n", "", "Qty   : " + nf.format(quantity), "", "", "");
        } else {
            discount = String.format("%12s%-10s%5s%9s%10s\n", "", "Qty   : " + nf.format(quantity), "", "Discount", cDiscountValue);
        }
        // if(!gstin.isEmpty())
        //mPrinter.printTextLine(gstin+"\n");
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.printTextLine(qtyNetAmt);
        mPrinter.printTextLine(discount);
        mPrinter.printLineFeed();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        mPrinter.setBold();
        mPrinter.setFontSizeXSmall();
        mPrinter.printTextLine(total);
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.printLineFeed();
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        GetSettings getSettings = new GetSettings(CheckoutActivity.this);
        String[] footerArray = {getSettings.getFooterText1(), getSettings.getFooterText2(),
                getSettings.getFooterText3(), getSettings.getFooterText4()};
        for (String aFooterArray : footerArray) {
            if (!aFooterArray.isEmpty()) {
                mPrinter.printTextLine("            " + aFooterArray + "\n");
                mPrinter.printLineFeed();
            }
        }
        mPrinter.setRegular();
        mPrinter.printTextLine("            ************************************\n");
        mPrinter.printLineFeed();
        mPrinter.printLineFeed();
        mPrinter.setAlignmentCenter();
        //Clearance for Paper tear
        mPrinter.printLineFeed();
        mPrinter.printLineFeed();
        mPrinter.resetPrinter();
        //print all commands
        mPrinter.batchPrint();
    }

    private void centerPrint1() {

    }

/*
    private void performPrinterTask2() {

        mPrinter.setPrintMode(BtpConsts.PRINT_IN_BATCH);
        mPrinter.resetPrinter();
        mPrinter.setHighIntensity();
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        mPrinter.printTextLine("\nMY COMPANY BILL\n");
        mPrinter.setRegular();
        mPrinter.printTextLine("~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        mPrinter.printLineFeed();
        // Bill Header End

        // Bill Details Start
        Bill b = new Bill();
        b.setCustomerName("Test Cust");
        b.setCustomerOrderNo("0045");

        mPrinter.setAlignmentLeft();
        mPrinter.printTextLine("Customer Name     : " + b.getCustomerName() + "\n");
        mPrinter.printTextLine("Customer Order ID : " + b.getCustomerOrderNo() + "\n");
        mPrinter.printTextLine("------------------------------\n");
        mPrinter.printTextLine("  Item      Quantity     Price\n");
        mPrinter.printTextLine("------------------------------\n");
        mPrinter.printTextLine("  Item 1          1       1.00\n");
        mPrinter.printTextLine("  Bags           10    2220.00\n");
        mPrinter.printTextLine("  Next Item     999   99999.00\n");
        mPrinter.printLineFeed();
        mPrinter.printTextLine("------------------------------\n");
        mPrinter.printTextLine("  Total              107220.00\n");
        mPrinter.printTextLine("------------------------------\n");
        mPrinter.printLineFeed();
        mPrinter.setBold();
        mPrinter.printTextLine("    Thank you ! Visit Again   \n");
        mPrinter.setRegular();
        mPrinter.printLineFeed();
        mPrinter.printTextLine("******************************\n");
        mPrinter.printLineFeed();

        mPrinter.printTextLine("~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        mPrinter.printLineFeed();

        mPrinter.setAlignmentCenter();

        //Clearance for Paper tear
        mPrinter.printLineFeed();
        mPrinter.printLineFeed();
        mPrinter.resetPrinter();

        //print all commands
        mPrinter.batchPrint();
    }

*/
}
