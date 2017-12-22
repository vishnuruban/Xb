package in.net.maitri.xb.billing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;

public class BillSeriesActivity extends AppCompatActivity {



    RecyclerView bsView;

    List<BillSeries> billSeries;
    DbHandler dbHandler;

    BillSeriesAdapter bsAdapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_series);


        dbHandler = new DbHandler(this);
        bsView = (RecyclerView) findViewById(R.id.bill_series_view);
        billSeries = new ArrayList<BillSeries>();
        billSeries = dbHandler.getAllBillSeries();

        for(int i =0;i<billSeries.size();i++)
         {
                 Log.i("Bill Name",  billSeries.get(i).getBillName());
         }

        bsAdapter = new BillSeriesAdapter(BillSeriesActivity.this,billSeries);
        bsView.setAdapter(bsAdapter);






    }
}
