package in.net.maitri.xb.itemdetails;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;

public class AddItemCategory extends AppCompatActivity {

    CategoryAdapter mCategoryAdapter;
    ItemAdapter mItemAdapter;
    List<Category> mGetAllCategories;
    List<Item> mGetAllItems;
    DbHandler mDbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_category);

        mDbHandler = new DbHandler(AddItemCategory.this);
        mGetAllCategories = mDbHandler.getAllcategorys();
        final RecyclerView categoryView = (RecyclerView) findViewById(R.id.category_view);
        if (mGetAllCategories.size() == 0) {
            findViewById(R.id.no_category).setVisibility(View.VISIBLE);
        } else {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddItemCategory.this);
            categoryView.setLayoutManager(linearLayoutManager);
            mCategoryAdapter = new CategoryAdapter(AddItemCategory.this, mGetAllCategories);
            categoryView.setAdapter(mCategoryAdapter);

        }

        RecyclerView itemView = (RecyclerView) findViewById(R.id.item_view);
        mGetAllItems = new ArrayList<>();
        mItemAdapter = new ItemAdapter(AddItemCategory.this, mGetAllItems);
        itemView.setAdapter(mItemAdapter);

        categoryView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), categoryView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Category category = mGetAllCategories.get(position);
               // updateItem(category.getId());
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                Toast.makeText(getApplicationContext(), category.getCategoryName() + " is selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        final FloatingActionButton addCategoryBtn = (FloatingActionButton) findViewById(R.id.add_category);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        FloatingActionButton addItemBtn = (FloatingActionButton) findViewById(R.id.add_item_button);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    private void addCategory() {
        DialogFragment newFragment = new AddCategory();
        newFragment.setCancelable(false);
        newFragment.show(getSupportFragmentManager(), "");
    }

    private void addItem() {
        DialogFragment newFragment = new AddItem();
        newFragment.setCancelable(false);
        newFragment.show(getSupportFragmentManager(), "");
    }

    void updateCategoryAdapter() {
        mGetAllCategories = mDbHandler.getAllcategorys();
        Log.d("Category", "Updated");
        mCategoryAdapter.notifyDataSetChanged();
        Log.d("Category", "Notified");
    }

    private void updateItem(int categoryId){
        mGetAllItems = mDbHandler.getAllitems(categoryId);
        mItemAdapter.notifyDataSetChanged();
    }

}
