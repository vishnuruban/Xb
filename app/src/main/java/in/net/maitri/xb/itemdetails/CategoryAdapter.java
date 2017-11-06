package in.net.maitri.xb.itemdetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.net.maitri.xb.R;


class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context mContext;

    CategoryAdapter(Context context){
        mContext = context;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryImage, categoryEdit;
        TextView categoryName;

         MyViewHolder(View itemView) {
            super(itemView);
             categoryImage = (ImageView) itemView.findViewById(R.id.category_img);
             categoryName = (TextView) itemView.findViewById(R.id.category_name);
             categoryEdit = (ImageView) itemView.findViewById(R.id.category_edit);
         }
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
