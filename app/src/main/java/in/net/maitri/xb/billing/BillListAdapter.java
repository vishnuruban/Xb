package in.net.maitri.xb.billing;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.net.maitri.xb.R;

/**
 * Created by SYSRAJ4 on 16/11/2017.
 */

public class BillListAdapter extends BaseAdapter {

    public ArrayList<BillItems> billItems;
    Activity activity;



    public BillListAdapter(Activity activity,ArrayList<BillItems> billItems)
    {
        this.activity = activity;
        this.billItems = billItems;
    }


    @Override
    public int getCount() {
        return billItems.size();
    }

    @Override
    public Object getItem(int i) {
        return billItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    private class ViewHolder
    {
        TextView mDesc;
        TextView mQty;
        TextView mRate;
        TextView mAmt;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if(view == null)
        {
         view = inflater.inflate(R.layout.bill_row,null);
            holder = new ViewHolder();
            holder.mDesc = (TextView) view.findViewById(R.id.desc);
            holder.mQty  = (TextView) view.findViewById(R.id.qty);
            holder.mRate = (TextView) view.findViewById(R.id.rate);
            holder.mAmt  = (TextView) view.findViewById(R.id.amt);
            view.setTag(holder);
        }
        else
        {

            holder = (ViewHolder) view.getTag();
        }

      DecimalFormat  df = new DecimalFormat("0.00");

           BillItems biItems = billItems.get(position);
            holder.mDesc.setText(biItems.getDesc());
            holder.mQty.setText(String.valueOf(biItems.getQty()));
            holder.mAmt.setText(String.valueOf(df.format(biItems.getAmount())));
            holder.mRate.setText(String.valueOf(df.format(biItems.getRate())));


        return view;
    }





}
