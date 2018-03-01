package in.net.maitri.xb.billing;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.net.maitri.xb.R;


public class BillListAdapter extends BaseAdapter {

    private ArrayList<BillItems> billItems;
    private Activity activity;
    private Context c;
    private LayoutInflater inflater;


   BillListAdapter(Activity activity, ArrayList<BillItems> billItems) {
        this.activity = activity;
        this.billItems = billItems;
        inflater = activity.getLayoutInflater();
    }

    public BillListAdapter(Context c, ArrayList<BillItems> billItems) {
        this.c = c;
        this.billItems = billItems;
        inflater = LayoutInflater.from(c);
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


    private class ViewHolder {
        TextView mDesc;
        TextView mQty;
        TextView mRate;
        TextView mAmt;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.bill_row, null);
            holder = new ViewHolder();
            holder.mDesc = (TextView) view.findViewById(R.id.desc);
            holder.mQty = (TextView) view.findViewById(R.id.qty);
            holder.mRate = (TextView) view.findViewById(R.id.rate);
            holder.mAmt = (TextView) view.findViewById(R.id.amt);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        BillItems biItems = billItems.get(position);
        holder.mDesc.setText(biItems.getDesc());
        holder.mQty.setText(String.valueOf(biItems.getQty()));
        holder.mAmt.setText(df.format(biItems.getAmount()));
        holder.mRate.setText(df.format(biItems.getRate()));
        return view;
    }


}
