package in.net.maitri.xb.itemdetails;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.customer.CustomerDetail;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.settings.SettingsActivity;
import in.net.maitri.xb.util.Permissions;

public class AddItemCategory extends AppCompatActivity {

    private CategoryAdapter mCategoryAdapter;
    private ItemAdapter mItemAdapter;
    private List<Category> mGetAllCategories;
    private List<Item> mGetAllItems;
    private DbHandler mDbHandler;
    private TextView mNoItem, mNoCategory, mSelectedCategory;
    private int mCategoryId;

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
        } else {
            mSelectedCategory.setText(mGetAllCategories.get(0).getCategoryName());
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("catName", mGetAllCategories.get(0).getCategoryName());
            editor.putInt("catId", mGetAllCategories.get(0).getId());
            editor.apply();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddItemCategory.this);
        categoryView.setLayoutManager(linearLayoutManager);
        mCategoryAdapter = new CategoryAdapter(AddItemCategory.this, mGetAllCategories);
        categoryView.setAdapter(mCategoryAdapter);

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

        // category click and ong click
        categoryView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), categoryView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Category category = mGetAllCategories.get(position);
                updateItem(category.getId());
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                Toast.makeText(getApplicationContext(), category.getCategoryName() + " is selected!", Toast.LENGTH_SHORT).show();
                String categoryName = category.getCategoryName();
                mSelectedCategory.setText(categoryName);
                mCategoryId = category.getId();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("catName", categoryName);
                editor.putInt("catId", mCategoryId);
                editor.apply();
            }

            @Override
            public void onLongClick(View view, int position) {
                Category category = mGetAllCategories.get(position);
                showPopupMenu(view, category, new Item(), true);
                String categoryName = category.getCategoryName();
                String catImgPath = category.getCategoryImage();
                mCategoryId = category.getId();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("catName", categoryName);
                editor.putInt("catId", mCategoryId);
                editor.putString("catImg",catImgPath);
                editor.apply();
            }
        }));

        itemView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), itemView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Item item = mGetAllItems.get(position);
                showPopupMenu(view, new Category(), item, false);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        final FloatingActionButton addCategoryBtn = (FloatingActionButton) findViewById(R.id.add_category);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Permissions(AddItemCategory.this).checkWriteExternalStoragePermission();
                showFragment(new AddCategory());
            }
        });

        FloatingActionButton addItemBtn = (FloatingActionButton) findViewById(R.id.add_item_button);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Permissions(AddItemCategory.this).checkWriteExternalStoragePermission();
                showFragment(new AddItem());
            }
        });
    }

    private void showFragment(DialogFragment newFragment) {
        newFragment.setCancelable(false);
        newFragment.show(getSupportFragmentManager(), "");
    }

    /**
     * Showing popup for long click
     */
    private void showPopupMenu(View view, Category category, Item item, boolean isCategory) {
        PopupMenu popup = new PopupMenu(AddItemCategory.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_add_item_category_long_click, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(category, item, isCategory));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private Category mCategory;
        private  Item mItem;
        boolean isCategory;

        MyMenuItemClickListener(Category category, Item item, boolean isCategory) {
            mCategory = category;
            mItem = item;
            this.isCategory = isCategory;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit:
                    if (isCategory){
                        showFragment(new EditCatrgory());
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("itemObj", (Serializable) mItem);
                        DialogFragment newFragment = new EditItem();
                        newFragment.setArguments(bundle);
                        showFragment(newFragment);
                    }
                    return true;
                case R.id.delete:
                    if (isCategory){
                        if (mDbHandler.getAllitems(mCategory.getId()).isEmpty()) {
                            delete(mCategory.getId(), mCategory.getCategoryName(), isCategory);
                        } else {
                            Toast.makeText(AddItemCategory.this, "You can't delete a category having items. ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        delete(mItem.getId(), mItem.getItemName(), isCategory);
                    }
                    return true;
                default:
            }
            return false;
        }
    }

    private void delete(final int id, String name, final boolean isCategory) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete " + name + "?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id1) {
                        if (isCategory) {
                            mDbHandler.deletecategory(id);
                            updateCategoryAdapter();
                        } else {
                            mDbHandler.deleteItem(id);
                            updateItem(mCategoryId);
                        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_item_category, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
             startActivity(new Intent(AddItemCategory.this, SettingsActivity.class));
                break;

            case R.id.customers:
                startActivity(new Intent(AddItemCategory.this, CustomerDetail.class));
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
