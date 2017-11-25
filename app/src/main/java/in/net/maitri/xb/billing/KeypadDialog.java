package in.net.maitri.xb.billing;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Item;

/**
 * Created by SYSRAJ4 on 18/11/2017.
 */

public class KeypadDialog extends Dialog implements DialogInterface.OnClickListener ,View.OnClickListener {


    public Activity c;
    TextInputEditText et_result;
    TextView tv_itemName;
    Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven,
            btn_eight, btn_nine, btn_zero, btn_point, btn_ok, btn_cancel, btn_clear;

    double num1 = 0;
    int indicator = 0;
    //String bItemName;
    Item bItem;

    BillItems billItems;

    public KeypadDialog(Activity c,Item bItem)
    {
        super(c);
        this.c = c;
        this.bItem = bItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.number_keypad_dialog);

        initializeVars();
        disableSoftInputFromAppearing(et_result);

       DecimalFormat df = new DecimalFormat("0.00");
        tv_itemName.setText(bItem.getItemName());

        et_result.setText(df.format(bItem.getItemSP()));
        et_result.addTextChangedListener(watch);
        et_result.setSelection(et_result.getText().length());
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }






    public void initializeVars(){


        tv_itemName = (TextView) findViewById(R.id.tv_itemName);
        et_result = (TextInputEditText) findViewById(R.id.et_result);
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
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);

        btn_clear = (Button)findViewById(R.id.btn_clear);

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
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        btn_clear.setOnClickListener(this);

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

            case R.id.btn_clear:

                int length = et_result.getText().length();
                if (length > 0) {
                    et_result.getText().delete(length - 1, length);
                }
              //  et_result.setText("");
                break;
            case R.id.btn_ok:

                if(et_result.getText().toString().isEmpty() || et_result.getText().toString().startsWith("."))
                {
                    Toast.makeText(c,"Please enter valid price",Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(
                        et_result.getText().toString());

                if(price == 0 )
                {
                    Toast.makeText(c,"Please enter valid quantity",Toast.LENGTH_SHORT).show();
                    return;
                }

              //  double price =  qty * bItem.getItemSP();
                billItems = new BillItems(bItem.getCategoryId(),bItem.getId(),bItem.getItemName(),1,price,price);
                Toast.makeText(c,et_result.getText().toString(),Toast.LENGTH_SHORT).show();
                FragmentOne.populateList(billItems);
                FragmentThree.dismissDialog();
                break;

            case R.id.btn_cancel:
                FragmentThree.dismissDialog();
                break;

        }

        }


    TextWatcher watch = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            String searchString = s.toString();
            int textLength = searchString.length();
            et_result.setSelection(textLength);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub


        }
    };

    public static void disableSoftInputFromAppearing(TextInputEditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }
}
