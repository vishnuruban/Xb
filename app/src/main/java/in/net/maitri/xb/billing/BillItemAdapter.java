package in.net.maitri.xb.billing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Item;


/**
 * Created by SYSRAJ4 on 17/11/2017.
 */

public class BillItemAdapter  extends RecyclerView.Adapter<BillItemAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<Item> mItemList;
    private List<Item> mFilteredList;
    BillItemAdapter(Context context, List<Item> mItemList) {
        mContext = context;
        this.mItemList = mItemList;
        this.mFilteredList = mItemList;
    }

    public Object getItem(int location) {
        return mFilteredList.get(location);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    mFilteredList = mItemList;
                } else {
                    ArrayList<Item> filteredList = new ArrayList<>();
                    for (Item item : mItemList) {
                        if (item.getItemName().toLowerCase().contains(charString) ) {
                            filteredList.add(item);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
    public BillItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new BillItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BillItemAdapter.MyViewHolder holder, int position) {
        Item item = mFilteredList.get(position);
        String rs = "\u20B9";
        try{
            byte[] utf8 = rs.getBytes("UTF-8");
            rs = new String(utf8, "UTF-8");}
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        holder.itemName.setText(item.getItemName());
        String sp = rs+" " + FragmentOne.df.format(item.getItemSP());
        holder.itemSp.setText(sp);

        String imagePath = item.getItemImage();
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.itemImage.setImageBitmap(myBitmap);
        }
    }
    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
    public void clear()
    {
        mFilteredList.clear();
    }
}