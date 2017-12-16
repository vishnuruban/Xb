package in.net.maitri.xb.itemdetails;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Category;


class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<Category> mAllCategories;

    CategoryAdapter(Context context, List<Category> getAllCategories){
        mContext = context;
        mAllCategories = getAllCategories;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryImage;
        TextView categoryName;
        CardView cardView;

         MyViewHolder(View itemView) {
            super(itemView);
             categoryImage = (ImageView) itemView.findViewById(R.id.category_img);
             categoryName = (TextView) itemView.findViewById(R.id.category_name);
             cardView  = (CardView) itemView.findViewById(R.id.card_view);
         }
    }


    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position) {
        Category categoryDetails = mAllCategories.get(position);
        holder.categoryName.setText(categoryDetails.getCategoryName());
        String imagePath = categoryDetails.getCategoryImage();
        if (mAllCategories.get(position).isSelected()) {
            holder.cardView.setBackgroundColor(Color.parseColor("#0675AD"));
            holder.categoryName.setTextColor(Color.parseColor("#ffffff"));
            mAllCategories.get(position).setSelected(false);

        } else {
            holder.cardView.setBackgroundColor(Color.parseColor("#B3E5FC"));
            holder.categoryName.setTextColor(Color.parseColor("#000000"));
        }
//         Log.i("Image Path",imagePath);

        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.categoryImage.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAllCategories.size();
    }


    public void setSelected(int pos) {
        try {

            mAllCategories.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
