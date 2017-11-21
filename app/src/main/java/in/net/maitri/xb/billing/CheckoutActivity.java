package in.net.maitri.xb.billing;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
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
import com.cie.btp.PrinterWidth;
import com.reginald.editspinner.EditSpinner;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.net.maitri.xb.R;

/**
 * Created by SYSRAJ4 on 15/11/2017.
 */

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {


    ListView listView;
    BillListAdapter badapter;
    TextView cProducts,cPrice,cDate,cNetAmount,cPayment,tCash,tBalance;
    EditText cDiscount;
    String totalProducts, totalPrice;
    LinearLayout lNetAmt;
    RadioGroup cDiscountType;
    String selectedButton;
    double netAmt = 0;
    String rs = "\u20B9";
    static DecimalFormat df;
    EditText et_result,cCash;
    EditSpinner mEditSpinner;
    public static CieBluetoothPrinter mPrinter = CieBluetoothPrinter.INSTANCE;







    String[] pModes = {"CASH","DEBIT CARD","CREDIT CARD","WALLET"};
    Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven,
            btn_eight, btn_nine, btn_zero, btn_point, btn_ok, btn_cancel, btn_clear,btn_100,btn_500,btn_2000,btn_cash,btn_dc,btn_cc,btn_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity);

        cDiscount = (EditText) findViewById(R.id.cDiscount);
     //   cDiscount.setInputType(InputType.TYPE_NULL);

        cNetAmount = (TextView) findViewById(R.id.cNetamount);
        listView = (ListView) findViewById(R.id.listview);

        cPrice    = (TextView)findViewById(R.id.cTotalPrice);
        cProducts = (TextView)findViewById(R.id.cTotalProducts);
        cDate = (TextView)findViewById(R.id.date);
        cPayment =(TextView) findViewById(R.id.cPayment);
        lNetAmt = (LinearLayout)findViewById(R.id.layout_Net) ;
        tCash = (TextView) findViewById(R.id.tCash);
        cCash = (EditText) findViewById(R.id.cCash);
        tBalance = (TextView)findViewById(R.id.cBalance);
        tBalance.setVisibility(View.INVISIBLE);

         cDiscountType = (RadioGroup)findViewById(R.id.discount_toggle);

        mEditSpinner = (EditSpinner) findViewById(R.id.cPaymentMode);
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.payment));
        mEditSpinner.setAdapter(adapter);
        mEditSpinner.setEditable(false);
        mEditSpinner.setText("CASH");





        df = new DecimalFormat("0.00");
        try{
            byte[] utf8 = rs.getBytes("UTF-8");

            rs = new String(utf8, "UTF-8");}
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        initializeVars();
        int selectedId = cDiscountType.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
        selectedButton = selectedRadioButton.getText().toString();
        cDiscount.setText(selectedButton);
        Selection.setSelection(cDiscount.getText(), cDiscount.getText().length());

        GradientDrawable bgShape = (GradientDrawable)lNetAmt.getBackground();
        bgShape.setColor(getResources().getColor(R.color.green));
        badapter = new BillListAdapter(CheckoutActivity.this,FragmentOne.billList);
        listView.setAdapter(badapter);

        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        cDate.setText(date);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null) {
           totalProducts = bundle.getString("products");
            totalPrice  = bundle.getString("price");

        }
        netAmt = Double.parseDouble(totalPrice);
       mEditSpinner.addTextChangedListener(paymentMode);
        cDiscount.addTextChangedListener(watch);
        cCash.addTextChangedListener(cash);
        cProducts.setText(totalProducts);
        cPrice.setText(totalPrice);
        cNetAmount.setText(rs+" "+totalPrice);
        cPayment.setText("PAYMENT - "+rs+" "+totalPrice);

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

        cDiscount.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {

                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                et_result = cDiscount;
                return true;


            }
        });



        cCash.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {

                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                et_result = cCash;
                return true;


            }
        });


    }



    public void initializeVars(){


        btn_one = (Button)findViewById(R.id.btn_one);
        btn_two = (Button)findViewById(R.id.btn_two);
        btn_three = (Button)findViewById(R.id.btn_three);
        btn_four = (Button)findViewById(R.id.btn_four);
        btn_five = (Button)findViewById(R.id.btn_five);
        btn_six = (Button)findViewById(R.id.btn_six);
        btn_seven = (Button)findViewById(R.id.btn_seven);
        btn_eight = (Button)findViewById(R.id.btn_eight);
        btn_nine = (Button)findViewById(R.id.btn_nine);
        btn_zero = (Button)findViewById(R.id.btn_zero);
        btn_point = (Button)findViewById(R.id.btn_point);
        btn_clear = (Button)findViewById(R.id.btn_clear);
        btn_100 = (Button)findViewById(R.id.btn_100);
        btn_500 = (Button)findViewById(R.id.btn_500);
        btn_2000 = (Button)findViewById(R.id.btn_2000);
        btn_cash = (Button)findViewById(R.id.btn_cash);
        btn_dc = (Button)findViewById(R.id.btn_dc);
        btn_cc = (Button)findViewById(R.id.btn_cc);
        btn_wallet = (Button)findViewById(R.id.btn_wallet);

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


    }



    @Override
    public void onClick(View view) {
        switch(view.getId()) {

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
                et_result.setText("");
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
        }
    }








    TextWatcher paymentMode = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub


          if(s.toString().equals("CASH"))
          {
               tCash.setText("Cash ("+rs+")");
               cCash.setText("");
               cCash.setFocusable(true);
              cCash.setFocusableInTouchMode(true);
               cCash.setClickable(true);

          }
          else
          {
              tCash.setText("Amount ("+rs+")");
              cCash.setText(df.format(netAmt));
              cCash.setFocusable(false);
              cCash.setClickable(false);
          }


        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub



        }};





    TextWatcher cash = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub


            if(s.toString().equals(""))
            {
                tBalance.setVisibility(View.INVISIBLE);
            }
            else
            {


                if(Double.parseDouble(s.toString()) > netAmt) {
                    tBalance.setVisibility(View.VISIBLE);
                    double balance = Double.parseDouble(s.toString()) - netAmt;
                        tBalance.setText("Balance  "+rs+" "+String.valueOf(df.format(balance)));
                }
                else
                {
                    tBalance.setVisibility(View.INVISIBLE);
                }
            }


        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub



        }};




















    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub




            if(!s.toString().startsWith(selectedButton)){
                cDiscount.setText(selectedButton);
                Selection.setSelection(cDiscount.getText(), cDiscount.getText().length());

            }

            if(s.toString().equals("") || s.toString().equals(rs) || s.toString().equals("%")) {

              cNetAmount.setText(rs+" "+totalPrice);
                netAmt = Double.parseDouble(totalPrice);
            }

            else
            {

                char disSymbl = s.charAt(0);
                String ss = Character.toString(disSymbl);
                if(ss.equals(rs)){
                        System.out.println("S "+s);
                    if(s.toString().substring(1).startsWith("."))
                    {
                        Toast.makeText(CheckoutActivity.this,"Enter valid discount",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.out.println("SS "+ s.toString().substring(1));
               netAmt = Double.parseDouble(totalPrice) - Double.parseDouble( s.toString().substring(1));


            }
            else
                {
                    String disPrice = s.toString().substring(1);

                    double discount = (Double.parseDouble(disPrice.toString()) / 100.0) * Double.parseDouble(totalPrice);
                    netAmt = Double.parseDouble(totalPrice) - discount;

                }


                if(netAmt <= 0)
                {
                    Toast.makeText(CheckoutActivity.this,"Please enter valid discount",Toast.LENGTH_SHORT).show();
                    cDiscount.setText("");
                    return;
                }
                cNetAmount.setText(rs+" "+ String.valueOf(df.format(netAmt)));
                cPayment.setText("PAYMENT - "+rs+" "+String.valueOf(df.format(netAmt)));
                tCash.setText("Cash ("+rs+")");
                cCash.setText("");
                mEditSpinner.setText("CASH");



            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub



        }};








    private void performPrinterTask() {


      /*  BillItems billItems = new  BillItems("Annapurna Atta","2","50.00","100.00");
        billList.add(billItems);
        billItems = new  BillItems("Aachi Chick ma. ","2","10.00","200.00");
        billList.add(billItems);*/
        String ph = String.format("%1$-20s%20s\n", "Ph: 080-40951133", "TIN: 29160093044");
        String bill = String.format("%1$-20s%20s\n", "Bill No: 578", "21/11/17 1.40 pm");
        mPrinter.setPrinterWidth(PrinterWidth.PRINT_WIDTH_72MM);
        mPrinter.setPrintMode(BtpConsts.PRINT_IN_BATCH);
        mPrinter.resetPrinter();
        mPrinter.setHighIntensity();
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        // mPrinter.setFontSizeMedium();
        mPrinter.printTextLine("\nSudarshan Family Retail Store\n");
        mPrinter.printLineFeed();
        mPrinter.setRegular();
        mPrinter.printTextLine("8 th Cross,Malleswaram Circle\n");
        mPrinter.printTextLine("Bangalore,Bangalore-560068\n");
        mPrinter.setAlignmentLeft();
        mPrinter.printTextLine(" Ph: 080-40951133              TIN: 29160093044  \n");
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        mPrinter.printTextLine("CASH BILL\n");
        mPrinter.setRegular();

        //mPrinter.printLineFeed();


        String custName ="test customer";
        String cashName = "test cashier";

        String s = String.format("%-15s%8s%12s%12s\n", "Item", "Qty", "Price","Amount");


        String s2 = String.format("%-15s%8s%12s%12s\n", "Aachi Masala 1K", "2kg", "4500.00","9000.00");

        String qtyNetAmt = String.format("%-15s%8s%12s%12s\n", "Items/qty : 1/1 ", "", "Subtotal","90000.00");
        String discount = String.format("%-15s%8s%12s%12s\n", "", "", "Discount","10.00");
        String total = String.format("%-15s%8s%12s%12s\n", "Total", "", "","80.00");
        mPrinter.setAlignmentLeft();

        mPrinter.printTextLine(" Bill No: 6789                 21/11/17 1.20 PM\n");
        mPrinter.printTextLine(" Customer Name    : " + custName + "\n");
        mPrinter.printTextLine(" Cashier Name     : " + cashName+ "\n");
        mPrinter.printTextLine("------------------------------------------------\n");

        mPrinter.setBold();
        mPrinter.printTextLine(s);
        //   mPrinter.setFontSizeXSmall();
        mPrinter.printTextLine("------------------------------------------------\n");
        mPrinter.setRegular();

        //   mPrinter.printTextLine(s0);
        //  mPrinter.printTextLine(s1);
        //  mPrinter.setFontSizeSmall();
        mPrinter.printTextLine(s2);
        mPrinter.printTextLine("------------------------------------------------\n");
        mPrinter.printTextLine(qtyNetAmt);
        mPrinter.printTextLine(discount);
        //  mPrinter.setFontSizeSmall();
        // for(int i=0;i<billList.size();i++)
        //{
        // BillItems bi = billList.get(i);
        // mPrinter.printTextLine(bi.getItem()+"    "+bi.getDesc()+"    "+bi.getRate()+"  "+bi.getAmt());
        //  }
        mPrinter.printLineFeed();
        mPrinter.printTextLine("------------------------------------------------\n");
        mPrinter.setBold();
        mPrinter.printTextLine(total);
        mPrinter.printTextLine("------------------------------------------------\n");
        mPrinter.printLineFeed();
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        mPrinter.printTextLine("Thank you ! Visit Again");
        mPrinter.setRegular();
        mPrinter.printLineFeed();
        mPrinter.printTextLine("************************************************\n");
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
























}
