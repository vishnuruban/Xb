package in.net.maitri.xb.billReports;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.BillingActivity;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.itemdetails.AddItemCategory;
import in.net.maitri.xb.itemdetails.RecyclerTouchListener;

public class FilterActivity extends AppCompatActivity {

    private HashMap<String, ArrayList<FilterModel>> mFilterValueData;
    private HashMap<String, ArrayList<Integer>> mSelectedFilterValue;
    private DbHandler mDbHandler;
    private ArrayList<FilterModel> mFilterValueAdapterData;
    private String[] filterName = {"Category", "Item"};
    private int mSelectedFilterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mSelectedFilterValue = (HashMap<String, ArrayList<Integer>>) bundle.getSerializable("filter");
        mDbHandler = new DbHandler(FilterActivity.this);
        mFilterValueData = new HashMap<>();
        mFilterValueAdapterData = new ArrayList<>();
        getData();
        RecyclerView filterNameView = (RecyclerView) findViewById(R.id.filter_name);
        final ArrayList<FilterModel> filterNameData = new ArrayList<>();
        for (String i : filterName) {
            FilterModel fm = new FilterModel();
            fm.setSelected(false);
            fm.setName(i);
            filterNameData.add(fm);
        }
        final FilterNameAdapter filterNameAdapter = new FilterNameAdapter(filterNameData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FilterActivity.this);
        filterNameView.setLayoutManager(layoutManager);
        filterNameView.setItemAnimator(new DefaultItemAnimator());
        filterNameView.setAdapter(filterNameAdapter);

        RecyclerView filterValueView = (RecyclerView) findViewById(R.id.filter_value);
        final FilterValueAdapter filterValueAdapter = new FilterValueAdapter(mFilterValueAdapterData);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(FilterActivity.this);
        filterValueView.setLayoutManager(layoutManager1);
        filterValueView.setItemAnimator(new DefaultItemAnimator());
        filterValueView.setAdapter(filterValueAdapter);

        filterNameView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), filterNameView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mFilterValueAdapterData.clear();
                mSelectedFilterPosition = position;
                switch (filterName[position]) {
                    case "Category":
                        mFilterValueAdapterData.addAll(mFilterValueData.get("Category"));
                        break;
                    case "Item":
                        ArrayList<FilterModel> filterModelArrayList = mFilterValueData.get("Item");
                        ArrayList<Integer> selectedCategory = mSelectedFilterValue.get("Category");
                        if (!selectedCategory.isEmpty()) {
                            for (int i = 0; i < selectedCategory.size(); i++) {

                                for (int j = 0; j < filterModelArrayList.size(); j++) {
                                    if (filterModelArrayList.get(j).getCatId() == selectedCategory.get(i)) {
                                        mFilterValueAdapterData.add(filterModelArrayList.get(j));
                                    } else {
                                        mFilterValueData.get("Item").get(j).setSelected(false);
                                    }
                                }
                            }
                        } else {
                            mFilterValueAdapterData.addAll(filterModelArrayList);
                        }
                        break;
                }
                filterNameAdapter.setSelected(position);
                filterValueAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        filterValueView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), filterValueView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImageView checkBox = (ImageView) view.findViewById(R.id.checkbox);
                filterValueAdapter.setSelected(position, checkBox);
                String filter = filterName[mSelectedFilterPosition];
                ArrayList<Integer> selectedList = mSelectedFilterValue.get(filter);
                switch (filter) {
                    case "Category":
                        int catCode = mFilterValueData.get(filter).get(position).getCatId();
                        if (selectedList.contains(catCode)) {
                            selectedList.remove(selectedList.indexOf(catCode));
                        } else {
                            selectedList.add(catCode);
                        }
                        break;

                    case "Item":
                        if (!mSelectedFilterValue.get("Category").isEmpty()) {
                            int itmCode = mFilterValueAdapterData.get(position).getItmId();
                            if (selectedList.contains(itmCode)) {
                                selectedList.remove(selectedList.indexOf(itmCode));
                            } else {
                                selectedList.add(itmCode);
                            }
                        } else {
                            int itmCode = mFilterValueData.get(filter).get(position).getItmId();
                            if (selectedList.contains(itmCode)) {
                                selectedList.remove(selectedList.indexOf(itmCode));
                            } else {
                                selectedList.add(itmCode);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Button applyFilter = (Button) findViewById(R.id.apply);
        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnFilter();
            }
        });
    }

    @Override
    public void onBackPressed() {
        returnFilter();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            returnFilter();
        }
        return true;
    }

    private void returnFilter(){
        String query ="";
        if (!mSelectedFilterValue.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            ArrayList<Integer> selectedCategory = mSelectedFilterValue.get("Category");
            if (!selectedCategory.isEmpty()) {
                String text = android.text.TextUtils.join(",", selectedCategory);
                sb.append(" and CAT.id in (").append(text).append(")");
            }
            ArrayList<Integer> selectedItem = mSelectedFilterValue.get("Item");
            if (!selectedItem.isEmpty()) {
                String text = android.text.TextUtils.join(",", selectedItem);
                sb.append(" and ITM.id in (").append(text).append(")");
            }
            query = sb.toString();
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("filter", mSelectedFilterValue);
        returnIntent.putExtra("query", query);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void getData() {
        if (mSelectedFilterValue.isEmpty()) {
            ArrayList<FilterModel> categoryArrayList = new ArrayList<>();
            List<Category> categoryList = mDbHandler.getAllcategorys();
            for (int i = 0; i < categoryList.size(); i++) {
                FilterModel filterModel = new FilterModel();
                filterModel.setName(categoryList.get(i).getCategoryName());
                filterModel.setCatId(categoryList.get(i).getId());
                filterModel.setSelected(false);
                categoryArrayList.add(filterModel);
            }
            mFilterValueData.put("Category", categoryArrayList);

            ArrayList<FilterModel> itemArrayList = new ArrayList<>();
            List<Item> itemList = mDbHandler.getAllitems1();
            for (int i = 0; i < itemList.size(); i++) {
                FilterModel filterModel = new FilterModel();
                filterModel.setName(itemList.get(i).getItemName());
                filterModel.setCatId(itemList.get(i).getCategoryId());
                filterModel.setItmId(itemList.get(i).getId());
                filterModel.setSelected(false);
                itemArrayList.add(filterModel);
            }
            mFilterValueData.put("Item", itemArrayList);
        } else {
            ArrayList<Integer> selectedCategory = mSelectedFilterValue.get("Category");
            ArrayList<FilterModel> categoryArrayList = new ArrayList<>();
            List<Category> categoryList = mDbHandler.getAllcategorys();
            for (int i = 0; i < categoryList.size(); i++) {
                FilterModel filterModel = new FilterModel();
                if (selectedCategory.contains(categoryList.get(i).getId())) {
                    filterModel.setSelected(true);
                } else {
                    filterModel.setSelected(false);
                }
                filterModel.setName(categoryList.get(i).getCategoryName());
                filterModel.setCatId(categoryList.get(i).getId());
                categoryArrayList.add(filterModel);
            }
            mFilterValueData.put("Category", categoryArrayList);

            ArrayList<Integer> selectedItem = mSelectedFilterValue.get("Item");
            ArrayList<FilterModel> itemArrayList = new ArrayList<>();
            List<Item> itemList = mDbHandler.getAllitems1();
            for (int i = 0; i < itemList.size(); i++) {
                FilterModel filterModel = new FilterModel();
                if (selectedItem.contains(itemList.get(i).getId())) {
                    filterModel.setSelected(true);
                } else {
                    filterModel.setSelected(false);
                }
                filterModel.setName(itemList.get(i).getItemName());
                filterModel.setCatId(itemList.get(i).getCategoryId());
                filterModel.setItmId(itemList.get(i).getId());
                itemArrayList.add(filterModel);
            }
            mFilterValueData.put("Item", itemArrayList);
        }
    }
}