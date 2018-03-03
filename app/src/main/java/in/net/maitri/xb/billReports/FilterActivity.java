package in.net.maitri.xb.billReports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import in.net.maitri.xb.R;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        RecyclerView filterNameView = (RecyclerView) findViewById(R.id.filter_name);
        ArrayList<String> filterNameData = new ArrayList<>();
        filterNameData.add("Category");
        filterNameData.add("Item");
        FilterNameAdapter filterNameAdapter = new FilterNameAdapter(filterNameData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FilterActivity.this);
        filterNameView.setLayoutManager(layoutManager);
        filterNameView.setItemAnimator(new DefaultItemAnimator());
        filterNameView.setAdapter(filterNameAdapter);

        RecyclerView filterValueView = (RecyclerView) findViewById(R.id.filter_value);
        FilterValueAdapter filterValueAdapter = new FilterValueAdapter(filterNameData);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(FilterActivity.this);
        filterValueView.setLayoutManager(layoutManager1);
        filterValueView.setItemAnimator(new DefaultItemAnimator());
        filterValueView.setAdapter(filterValueAdapter);
    }
}
