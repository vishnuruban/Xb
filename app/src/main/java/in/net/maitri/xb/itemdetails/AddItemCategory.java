package in.net.maitri.xb.itemdetails;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.util.Permissions;

public class AddItemCategory extends AppCompatActivity {

    private CategoryAdapter mCategoryAdapter;
    private ItemAdapter mItemAdapter;
    private List<Category> mGetAllCategories;
    private List<Item> mGetAllItems;
    private DbHandler mDbHandler;
    private TextView mNoItem, mNoCategory, mSelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_category);

        new Permissions(AddItemCategory.this).checkWriteExternalStoragePermission();

        mNoItem = (TextView) findViewById(R.id.no_item);
        mNoCategory = (TextView) findViewById(R.id.no_category);
        mSelectedCategory = (TextView) findViewById(R.id.selected_category_name);

        mDbHandler = new DbHandler(AddItemCategory.this);
        mGetAllCategories = mDbHandler.getAllcategorys();
        final RecyclerView categoryView = (RecyclerView) findViewById(R.id.category_view);
        RecyclerView itemView = (RecyclerView) findViewById(R.id.item_view);
        if (mGetAllCategories.size() == 0) {
            mNoCategory.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddItemCategory.this);
        categoryView.setLayoutManager(linearLayoutManager);
        mCategoryAdapter = new CategoryAdapter(AddItemCategory.this, mGetAllCategories);
        categoryView.setAdapter(mCategoryAdapter);
        mSelectedCategory.setText(mGetAllCategories.get(0).getCategoryName());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("catName", mGetAllCategories.get(0).getCategoryName());
        editor.putInt("catId", mGetAllCategories.get(0).getId());
        editor.apply();

        int columns = CalculateNoOfColumnsAccScreenSize.calculateNoOfColumns(AddItemCategory.this);
        if (columns > 1){
            columns -= 1;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AddItemCategory.this, columns);
        itemView.setLayoutManager(gridLayoutManager);
        mGetAllItems = mDbHandler.getAllitems(1);
        if (mGetAllItems.size() == 0) {
            mNoItem.setVisibility(View.VISIBLE);
        } else {
            mNoItem.setVisibility(View.GONE);
        }
        mItemAdapter = new ItemAdapter(AddItemCategory.this, mGetAllItems);
        itemView.setAdapter(mItemAdapter);

        categoryView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), categoryView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Category category = mGetAllCategories.get(position);
                updateItem(category.getId());
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                Toast.makeText(getApplicationContext(), category.getCategoryName() + " is selected!", Toast.LENGTH_SHORT).show();
                String categoryName = category.getCategoryName();
                mSelectedCategory.setText(categoryName);
                int categoryId = category.getId();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("catName", categoryName);
                editor.putInt("catId", categoryId);
                editor.apply();
            }

            @Override
            public void onLongClick(View view, int position) {
                Category category = mGetAllCategories.get(position);
                showPopupMenu(view, category.getId(), category.getCategoryName(), true);
            }
        }));

        final FloatingActionButton addCategoryBtn = (FloatingActionButton) findViewById(R.id.add_category);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Permissions(AddItemCategory.this).checkWriteExternalStoragePermission();
                addCategory();
            }
        });

        FloatingActionButton addItemBtn = (FloatingActionButton) findViewById(R.id.add_item_button);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Permissions(AddItemCategory.this).checkWriteExternalStoragePermission();
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
        mGetAllCategories.clear();
        mGetAllCategories = mDbHandler.getAllcategorys();
        if (mGetAllCategories.size() == 0) {
            mNoCategory.setVisibility(View.VISIBLE);
        } else {
            mNoCategory.setVisibility(View.GONE);
        }
        mCategoryAdapter.notifyDataSetChanged();
    }

    void updateItem(int categoryId) {
        mGetAllItems.clear();
        mGetAllItems = mDbHandler.getAllitems(categoryId);
        if (mGetAllItems.size() == 0) {
            mNoItem.setVisibility(View.VISIBLE);
        } else {
            mNoItem.setVisibility(View.GONE);
        }
        mItemAdapter.notifyDataSetChanged();
    }

    /**
     * Showing popup for long click
     */
    private void showPopupMenu(View view, int id, String name, boolean isCategory) {
        PopupMenu popup = new PopupMenu(AddItemCategory.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_add_item_category_long_click, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(id, name, isCategory));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int id;
        private String name;
        boolean isCategory;

        MyMenuItemClickListener(int itemId, String itemName, boolean isCategory) {
            id = itemId;
            name = itemName;
            this.isCategory = isCategory;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit:
                    Toast.makeText(AddItemCategory.this, "Edit", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.delete:
                    delete(id, name, isCategory);
                    return true;
                default:
            }
            return false;
        }
    }

    private void delete(final int id, String name, boolean isCategory) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete " + name + "?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id1) {
                        mDbHandler.deletecategory(id);
                        updateCategoryAdapter();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

}
