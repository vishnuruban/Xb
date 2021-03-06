package in.net.maitri.xb.itemdetails;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
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
import in.net.maitri.xb.billing.BillingActivity;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.util.CheckDeviceType;
import in.net.maitri.xb.util.Permissions;

public class AddItemCategory extends AppCompatActivity {

    private CategoryAdapter mCategoryAdapter;
    private ItemAdapter mItemAdapter;
    private List<Category> mGetAllCategories;
    private List<Item> mGetAllItems;
    private DbHandler mDbHandler;
    private TextView mNoItem, mNoCategory, mSelectedCategory;
    private int mCategoryId;
    private FloatingActionButton mAddItemBtn;
    private boolean isAll = true;
    private RecyclerView categoryView, itemView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_category);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        new Permissions(AddItemCategory.this).checkWriteExternalStoragePermission();

        mNoItem = (TextView) findViewById(R.id.no_item);
        mNoCategory = (TextView) findViewById(R.id.no_category);
        mSelectedCategory = (TextView) findViewById(R.id.selected_category_name);
        categoryView = (RecyclerView) findViewById(R.id.category_view);
        itemView = (RecyclerView) findViewById(R.id.item_view);
        mDbHandler = new DbHandler(AddItemCategory.this);

        new GetCategories().execute();

        // category click and ong click
        categoryView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), categoryView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                progressDialog = new ProgressDialog(AddItemCategory.this);
                progressDialog.setMessage("Getting items...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if (position == 0) {
                    selectAll();
                } else {
                    mAddItemBtn.setVisibility(View.VISIBLE);
                    mCategoryAdapter.setSelected(position);
                    Category category = mGetAllCategories.get(position);
                    isAll = false;
                    updateItem(category.getId());
                    view.setBackgroundColor(ContextCompat.getColor(AddItemCategory.this, R.color.colorPrimary));
                    String categoryName = category.getCategoryName();
                    mSelectedCategory.setText(categoryName);
                    mCategoryId = category.getId();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("catName", categoryName);
                    editor.putInt("catId", mCategoryId);
                    editor.apply();
                }
                progressDialog.cancel();
            }

            @Override
            public void onLongClick(View view, int position) {
                if (position != 0) {
                    Category category = mGetAllCategories.get(position);
                    showPopupMenu(view, category, new Item(), true);
                    String categoryName = category.getCategoryName();
                    String catImgPath = category.getCategoryImage();
                    mCategoryId = category.getId();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("catName", categoryName);
                    editor.putInt("catId", mCategoryId);
                    editor.putString("catImg", catImgPath);
                    editor.apply();
                }
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

        mAddItemBtn = (FloatingActionButton) findViewById(R.id.add_item_button);
        mAddItemBtn.setVisibility(View.GONE);
        mAddItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mSelectedCategory.getText().toString().isEmpty()) {
                    Toast.makeText(AddItemCategory.this, "Please select category before adding item.", Toast.LENGTH_SHORT).show();
                } else {
                    new Permissions(AddItemCategory.this).checkWriteExternalStoragePermission();
                    showFragment(new AddItem());
                }
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
        private Item mItem;
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
                    if (isCategory) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("catObj", mCategory);
                        DialogFragment newFragment = new EditCatrgory();
                        newFragment.setArguments(bundle);
                        showFragment(newFragment);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("itemObj", mItem);
                        DialogFragment newFragment = new EditItem();
                        newFragment.setArguments(bundle);
                        showFragment(newFragment);
                    }
                    return true;
                case R.id.delete:
                    if (isCategory) {
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
                            selectAll();
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
        mGetAllCategories = mDbHandler.getAllcategorys1();
        if (mGetAllCategories.size() == 0) {
            mNoCategory.setVisibility(View.VISIBLE);
        } else {
            mNoCategory.setVisibility(View.GONE);
        }
        mCategoryAdapter.notifyDataSetChanged();
    }

    void updateItem(int categoryId) {
        mGetAllItems.clear();
        if (isAll) {
            mGetAllItems = mDbHandler.getAllitems1();
            if (mGetAllItems.size() == 0) {
                mNoItem.setVisibility(View.VISIBLE);
            } else {
                mNoItem.setVisibility(View.GONE);
            }
            mItemAdapter.notifyDataSetChanged();
        } else {
            mGetAllItems = mDbHandler.getAllitems(categoryId);
            if (mGetAllItems.size() == 0) {
                mNoItem.setVisibility(View.VISIBLE);
            } else {
                mNoItem.setVisibility(View.GONE);
            }
            mItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddItemCategory.this, BillingActivity.class));
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AddItemCategory.this, BillingActivity.class));
            finish();
        }
        return true;
    }

    private class GetCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AddItemCategory.this);
            progressDialog.setMessage("Getting items...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mGetAllCategories = mDbHandler.getAllcategorys1();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            LinearLayoutManager linearLayoutManager;
            if (new CheckDeviceType(AddItemCategory.this).isTablet()) {
                linearLayoutManager = new LinearLayoutManager(AddItemCategory.this,
                        LinearLayoutManager.VERTICAL, false);
            } else {
                linearLayoutManager = new LinearLayoutManager(AddItemCategory.this,
                        LinearLayoutManager.HORIZONTAL, false);
            }
            categoryView.setLayoutManager(linearLayoutManager);
            mCategoryAdapter = new CategoryAdapter(AddItemCategory.this, mGetAllCategories);
            categoryView.setAdapter(mCategoryAdapter);
            if (mGetAllCategories.size() == 0) {
                mNoCategory.setVisibility(View.VISIBLE);
            } else {
                mSelectedCategory.setText(mGetAllCategories.get(0).getCategoryName());
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("catName", mGetAllCategories.get(0).getCategoryName());
                editor.putInt("catId", mGetAllCategories.get(0).getId());
                editor.apply();
                mGetAllCategories.get(0).setSelected(true);
            }
            int columns = CalculateNoOfColumnsAccScreenSize.calculateNoOfColumns(AddItemCategory.this);
            GridLayoutManager gridLayoutManager;
            if (new CheckDeviceType(AddItemCategory.this).isTablet()) {
                gridLayoutManager = new GridLayoutManager(AddItemCategory.this, columns);
            } else {
                gridLayoutManager = new GridLayoutManager(AddItemCategory.this, columns + 1);
            }
            itemView.setLayoutManager(gridLayoutManager);
            new GetItems().execute();
        }
    }

    private class GetItems extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            mGetAllItems = mDbHandler.getAllitems1();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (mGetAllItems.size() == 0) {
                mNoItem.setVisibility(View.VISIBLE);
            } else {
                mNoItem.setVisibility(View.GONE);
            }
            mItemAdapter = new ItemAdapter(AddItemCategory.this, mGetAllItems);
            itemView.setAdapter(mItemAdapter);
            progressDialog.cancel();
        }
    }

    private void selectAll() {
        mAddItemBtn.setVisibility(View.GONE);
        mCategoryAdapter.setSelected(0);
        isAll = true;
        updateItem(0);
        mSelectedCategory.setText(R.string.all);
    }

}