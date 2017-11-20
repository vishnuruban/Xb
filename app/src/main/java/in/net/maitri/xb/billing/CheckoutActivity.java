package in.net.maitri.xb.billing;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.net.maitri.xb.R;

/**
 * Created by SYSRAJ4 on 15/11/2017.
 */

public class CheckoutActivity extends AppCompatActivity {


    ListView listView;
    BillListAdapter badapter;
    TextView cProducts,cPrice,cDate,cNetAmount,cPayment;
    EditText cDiscount;
    String totalProducts, totalPrice;
    LinearLayout lNetAmt;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity);

        cDiscount = (EditText) findViewById(R.id.cDiscount);
        cNetAmount = (TextView) findViewById(R.id.cNetamount);
        listView = (ListView) findViewById(R.id.listview);

        cPrice    = (TextView)findViewById(R.id.cTotalPrice);
        cProducts = (TextView)findViewById(R.id.cTotalProducts);
        cDate = (TextView)findViewById(R.id.date);
        cPayment =(TextView) findViewById(R.id.cPayment);
        lNetAmt = (LinearLayout)findViewById(R.id.layout_Net) ;

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


        cDiscount.addTextChangedListener(watch);
        cProducts.setText(totalProducts);
        cPrice.setText(totalPrice);


    }

    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            String rs = "\u20B9";
            try{
                byte[] utf8 = rs.getBytes("UTF-8");

                rs = new String(utf8, "UTF-8");}
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            if(s.toString().equals("")) {

              cNetAmount.setText(rs+" "+totalPrice);
            }

            else
            {


                double netAmt = Double.parseDouble(totalPrice) - Double.parseDouble(s.toString());

                if(netAmt <= 0)
                {
                    Toast.makeText(CheckoutActivity.this,"Please enter valid discount",Toast.LENGTH_SHORT).show();
                    return;
                }
                cNetAmount.setText(rs+" "+ String.valueOf(netAmt));
                cPayment.setText("PAYMENT - "+rs+" "+String.valueOf(netAmt));
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

}
