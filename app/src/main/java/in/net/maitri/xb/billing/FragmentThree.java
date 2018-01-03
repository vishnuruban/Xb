package in.net.maitri.xb.billing;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.internal.TextScale;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.itemdetails.AddItemCategory;
import in.net.maitri.xb.itemdetails.CalculateNoOfColumnsAccScreenSize;
import in.net.maitri.xb.itemdetails.RecyclerTouchListener;


public class FragmentThree extends Fragment {


    private List<Category> mGetAllCategories;
    private List<Item> mGetAllItems;

    private List<Item> sGetAllItems;
    private List<Item> mGetAllItemsC;
    private DbHandler mDbHandler;
    private HorizontalCategoryAdapter hCategoryAdapter;
    private BillItemAdapter bItemAdapter;
    private BillItemAdapter bItemAdapter1;
    private int mCategoryId;
    TextView sCatName;

    private TextInputEditText searchItems;
    static KeypadDialog kpd;
    RecyclerView categoryView;
    RecyclerView itemView;
    AppCompatTextView searchResults;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_three, container, false);

        mDbHandler = new DbHandler(getActivity());
        mGetAllCategories = mDbHandler.getAllcategorys();
        mGetAllItemsC = new ArrayList<Item>();
        mGetAllItems = new ArrayList<Item>();

        for (int i = 0; i < mGetAllCategories.size(); i++) {
            Category c = mGetAllCategories.get(i);
            System.out.println("catName " + c.getCategoryName());
        }

        //  searchItems = (TextInputEditText)view.findViewById(R.id.search_Items);

        setHasOptionsMenu(true);


        categoryView = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);
        itemView = (RecyclerView) view.findViewById(R.id.bill_item_view);
        sCatName = (TextView)  view.findViewById(R.id.bill_text_view);
        searchResults = (AppCompatTextView) view.findViewById(R.id.searchResults);
        searchResults.setVisibility(View.GONE);


        categoryView.setVisibility(View.VISIBLE);


        if (mGetAllCategories.size() == 0) {
            Toast.makeText(getActivity(), "No Categories Present", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("catName", mGetAllCategories.get(0).getCategoryName());
            editor.putInt("catId", mGetAllCategories.get(0).getId());
            editor.apply();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(categoryView.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.recyview_divider);
        verticalDecoration.setDrawable(verticalDivider);
        categoryView.addItemDecoration(verticalDecoration);

        mGetAllCategories.get(0).setSelected(true);
        sCatName.setText(  mGetAllCategories.get(0).getCategoryName());
        hCategoryAdapter = new HorizontalCategoryAdapter(getActivity(), mGetAllCategories);
        categoryView.setAdapter(hCategoryAdapter);
        categoryView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), categoryView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                hCategoryAdapter.setSelected(position);

                Category category = mGetAllCategories.get(position);
                //  Toast.makeText(getActivity(), category.getCategoryName() + " is selected!", Toast.LENGTH_SHORT).show();
                String categoryName = category.getCategoryName();
                sCatName.setText(categoryName);
                mCategoryId = category.getId();
                updateItem(mCategoryId);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("catName", categoryName);
                editor.putInt("catId", mCategoryId);
                editor.apply();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //  int columns = CalculateNoOfColumnsAccScreenSize.calculateNoOfColumns(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        itemView.setLayoutManager(gridLayoutManager);
        //   mGetAllItems = new ArrayList<Item>();


    /*    for(int i=0;i<mGetAllItemsC.size();i++)
        {
            Item item = mGetAllItemsC.get(i);
            if(item.getCategoryId() == 1)
            {
                mGetAllItems.add(item);
            }
        }*/

        mGetAllItems = mDbHandler.getAllitems(1);

        bItemAdapter = new BillItemAdapter(getActivity(), mGetAllItems);

        itemView.setAdapter(bItemAdapter);

        itemView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), categoryView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (mGetAllItems.size() != 0) {
                    Item item = (Item) bItemAdapter.getItem(position);
                    kpd = new KeypadDialog(getActivity(), item);
                    kpd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    kpd.setCancelable(false);
                    kpd.show();
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem search_item = menu.findItem(R.id.mi_search);

        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Search Items");


        for (int i = 0; i < mGetAllItemsC.size(); i++) {
            Item c = mGetAllItemsC.get(i);
            System.out.println("itName " + c.getItemName());
        }

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want when search view expended
                System.out.println("EXPANDED");
                //bItemAdapter.clear();
                searchResults.setVisibility(View.VISIBLE);
                mGetAllItemsC = mDbHandler.getAllitemC();
                categoryView.setVisibility(View.GONE);
                sCatName.setVisibility(View.GONE);
                System.out.println("ONSEARCH " + mGetAllItemsC.size());
                for (int i = 0; i < mGetAllItemsC.size(); i++) {
                    System.out.println("ONSEARCH " + mGetAllItemsC.get(i).getItemName());
                }
                bItemAdapter = new BillItemAdapter(getActivity(), mGetAllItemsC);
                itemView.setAdapter(bItemAdapter);
                bItemAdapter.notifyDataSetChanged();
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                categoryView.setVisibility(View.VISIBLE);
                sCatName.setVisibility(View.VISIBLE);
                searchResults.setVisibility(View.GONE);


                // bItemAdapter = new BillItemAdapter(getActivity(), mGetAllItems);
                //  itemView.setAdapter(bItemAdapter);

                mGetAllItems = mDbHandler.getAllitems(1);
           //     sCatName.setText(mGetAllCategories.get(0).getCategoryName());
                mGetAllCategories.get(0).setSelected(true);
                sCatName.setText(  mGetAllCategories.get(0).getCategoryName());
                hCategoryAdapter = new HorizontalCategoryAdapter(getActivity(), mGetAllCategories);
                categoryView.setAdapter(hCategoryAdapter);
                bItemAdapter = new BillItemAdapter(getActivity(), mGetAllItems);
                itemView.setAdapter(bItemAdapter);
                bItemAdapter.notifyDataSetChanged();


                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {


                //bItemAdapter.getFilter().filter(s.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                categoryView.setVisibility(View.GONE);
                sCatName.setVisibility(View.GONE);
                searchResults.setVisibility(View.VISIBLE);
                if (bItemAdapter != null) bItemAdapter.getFilter().filter(s);
                bItemAdapter.notifyDataSetChanged();
                return true;

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.mi_search:

                Toast.makeText(getActivity(), "Search Clicked", Toast.LENGTH_SHORT).show();

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    void updateItem(int categoryId) {
        mGetAllItems.clear();


    /*
      for(int i=0;i<mGetAllItemsC.size();i++)
        {

            Item item = mGetAllItemsC.get(i);
            if(item.getCategoryId() == categoryId)
            {
                mGetAllItems.add(item);
            }
        }

        */

        mGetAllItems = mDbHandler.getAllitems(categoryId);
        if (mGetAllItems.size() == 0) {
            Toast.makeText(getActivity(), "No Items found", Toast.LENGTH_SHORT).show();
        } else {
            bItemAdapter.notifyDataSetChanged();

        }

    }

    public static void dismissDialog() {
        kpd.dismiss();
    }


}
