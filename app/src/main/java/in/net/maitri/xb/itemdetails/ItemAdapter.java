package in.net.maitri.xb.itemdetails;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Item;

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<Item> mItemList;

    ItemAdapter(Context context, List<Item> itemList) {
        mContext = context;
        mItemList = itemList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemName, itemSp;

        MyViewHolder(View itemView) {
            super(itemView);

            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemSp = (TextView) itemView.findViewById(R.id.item_sp);
        }
    }

    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.MyViewHolder holder, int position) {

        Item item = mItemList.get(position);

        holder.itemName.setText(item.getItemName());
        String sp = "Rs. " + String.valueOf((int) item.getItemSP());
        holder.itemSp.setText(sp);
        String imagePath = item.getItemImage();
        File imgFile = new File(imagePath);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.itemImage.setImageBitmap(myBitmap);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
