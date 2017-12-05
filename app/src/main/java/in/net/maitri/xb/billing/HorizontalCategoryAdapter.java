package in.net.maitri.xb.billing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Category;


/**
 * Created by SYSRAJ4 on 17/11/2017.
 */

public class HorizontalCategoryAdapter extends RecyclerView.Adapter<HorizontalCategoryAdapter.MyViewHolder> {



    private Activity context;
    private List<Category> mAllCategories;



    HorizontalCategoryAdapter(Activity context, List<Category> getAllCategories){
        this.context = context;
        mAllCategories = getAllCategories;
    }



    @Override
    public HorizontalCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizonal_listitems, parent, false);
        return new HorizontalCategoryAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(HorizontalCategoryAdapter.MyViewHolder holder, int position) {
        Category categoryDetails = mAllCategories.get(position);

        System.out.println("C "+categoryDetails.getCategoryName());
        holder.categoryName.setText(categoryDetails.getCategoryName());

        if (mAllCategories.get(position).isSelected()) {
            holder.cardView.setBackgroundColor(Color.parseColor("#757575"));
            mAllCategories.get(position).setSelected(false);

        } else {
            holder.cardView.setBackgroundColor(Color.parseColor("#009688"));
        }
    }


    public void setSelected(int pos) {
        try {

            mAllCategories.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        TextView categoryName2;
        CardView cardView;

        MyViewHolder(View c) {
            super(c);
            categoryName = (TextView) c.findViewById(R.id.bCatName);
           // categoryName2 = (TextView) c.findViewById(R.id.bIemCount);
            cardView  = (CardView) c.findViewById(R.id.card_view);
        }
     }


    @Override
    public int getItemCount() {
        return mAllCategories.size();
    }


 }
