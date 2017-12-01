package in.net.maitri.xb.billing;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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


/**
 * Created by SYSRAJ4 on 10/11/2017.
 */

public class FragmentThree extends Fragment {


    private List<Category> mGetAllCategories;
    private List<Item> mGetAllItems;
    private DbHandler mDbHandler;
    private HorizontalCategoryAdapter hCategoryAdapter;
    private BillItemAdapter bItemAdapter;
    private int mCategoryId;
   static KeypadDialog kpd;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_three, container, false);

        mDbHandler = new DbHandler(getActivity());
        mGetAllCategories = mDbHandler.getAllcategorys();

        for(int i =0;i<mGetAllCategories.size();i++)
        {
            Category c = mGetAllCategories.get(i);
            System.out.println("catName "+c.getCategoryName());
        }

        RecyclerView categoryView = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);
        RecyclerView itemView = (RecyclerView) view.findViewById(R.id.bill_item_view);

        if (mGetAllCategories.size() == 0) {
            Toast.makeText(getActivity(),"No Categories Present",Toast.LENGTH_SHORT).show();
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
        mGetAllCategories.get(0).setSelected(true);
        hCategoryAdapter = new HorizontalCategoryAdapter(getActivity(), mGetAllCategories);
        categoryView.setAdapter(hCategoryAdapter);
        categoryView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), categoryView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
               hCategoryAdapter.setSelected(position);

                Category category = mGetAllCategories.get(position);
              //  Toast.makeText(getActivity(), category.getCategoryName() + " is selected!", Toast.LENGTH_SHORT).show();
                String categoryName = category.getCategoryName();
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




       mGetAllItems = mDbHandler.getAllitems(1);
        bItemAdapter = new BillItemAdapter(getActivity(), mGetAllItems);

        itemView.setAdapter(bItemAdapter);

        itemView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), categoryView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Item item = mGetAllItems.get(position);
                kpd = new KeypadDialog(getActivity(),item);
                kpd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                kpd.show();

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));








        return  view;
    }

    void updateItem(int categoryId) {
        mGetAllItems.clear();
        mGetAllItems = mDbHandler.getAllitems(categoryId);
        if (mGetAllItems.size() == 0) {
          Toast.makeText(getActivity(),"No Items found",Toast.LENGTH_SHORT).show();
        } else {
            bItemAdapter.notifyDataSetChanged();
        }

    }

    public static void dismissDialog(){
        kpd.dismiss();
    }


}
