package in.net.maitri.xb.billing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.net.maitri.xb.R;

/**
 * Created by SYSRAJ4 on 15/11/2017.
 */

public class CheckoutActivity extends AppCompatActivity {


    ListView listView;
    BillListAdapter badapter;
    TextView cProducts,cPrice,cDate;

    String totalProducts, totalPrice;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity);
        listView = (ListView) findViewById(R.id.listview);

        cPrice    = (TextView)findViewById(R.id.cTotalPrice);
        cProducts = (TextView)findViewById(R.id.cTotalProducts);
        cDate = (TextView)findViewById(R.id.date);

        badapter = new BillListAdapter(CheckoutActivity.this,FragmentOne.billList);
        listView.setAdapter(badapter);

        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        cDate.setText(date);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null) {
           totalProducts = bundle.getString("products");
            totalPrice  = bundle.getString("price");

        }

        cProducts.setText(totalProducts);
        cPrice.setText(totalPrice);


    }
}
