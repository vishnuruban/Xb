package in.net.maitri.xb.billReports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.itemdetails.RecyclerTouchListener;

public class FilterActivity extends AppCompatActivity {

    private HashMap<String, ArrayList<FilterModel>> mFilterValueData;
    private DbHandler mDbHandler;
    private ArrayList<FilterModel>  mFilterValueAdapterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mDbHandler = new DbHandler(FilterActivity.this);
        mFilterValueData = new HashMap<>();
        mFilterValueAdapterData = new ArrayList<>();
        getData();
        RecyclerView filterNameView = (RecyclerView) findViewById(R.id.filter_name);
        final ArrayList<String> filterNameData = new ArrayList<>();
        filterNameData.add("Category");
        filterNameData.add("Item");
        final FilterNameAdapter filterNameAdapter = new FilterNameAdapter(filterNameData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FilterActivity.this);
        filterNameView.setLayoutManager(layoutManager);
        filterNameView.setItemAnimator(new DefaultItemAnimator());
        filterNameView.setAdapter(filterNameAdapter);

        RecyclerView filterValueView = (RecyclerView) findViewById(R.id.filter_value);
        FilterValueAdapter filterValueAdapter = new FilterValueAdapter(mFilterValueAdapterData);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(FilterActivity.this);
        filterValueView.setLayoutManager(layoutManager1);
        filterValueView.setItemAnimator(new DefaultItemAnimator());
        filterValueView.setAdapter(filterValueAdapter);

        filterNameView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), filterNameView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mFilterValueAdapterData.clear();
                switch (filterNameData.get(position)){
                    case "Category":
                        mFilterValueAdapterData.addAll( mFilterValueData.get("Category"));
                        break;
                    case "Item":
                        mFilterValueAdapterData.addAll( mFilterValueData.get("Item"));
                        break;
                }
                filterNameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getData(){
        ArrayList<FilterModel> categoryArrayList = new ArrayList<>();
        List<Category> categoryList = mDbHandler.getAllcategorys();
        for (int i = 0; i < categoryList.size(); i++){
            FilterModel filterModel = new FilterModel();
            filterModel.setName(categoryList.get(i).getCategoryName());
            filterModel.setCatId(categoryList.get(i).getId());
            categoryArrayList.add(filterModel);
        }
        mFilterValueData.put("Category", categoryArrayList);

        ArrayList<FilterModel> itemArrayList = new ArrayList<>();
        List<Item> itemList = mDbHandler.getAllitems1();
        for (int i = 0; i < itemList.size(); i++){
            FilterModel filterModel = new FilterModel();
            filterModel.setName(itemList.get(i).getItemName());
            filterModel.setCatId(itemList.get(i).getCategoryId());
            filterModel.setItmId(itemList.get(i).getId());
            itemArrayList.add(filterModel);
        }
        mFilterValueData.put("Item", itemArrayList);
    }
}
