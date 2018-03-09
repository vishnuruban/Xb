package in.net.maitri.xb.billReports;


import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.net.maitri.xb.R;


class FilterNameAdapter extends RecyclerView.Adapter<FilterNameAdapter.MyViewHolder> {

    private ArrayList<FilterModel> filterName;

    FilterNameAdapter(ArrayList<FilterModel> filterName) {
        this.filterName = filterName;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView filterName;
        CardView cardView;


        MyViewHolder(View itemView) {
            super(itemView);
            filterName = (TextView) itemView.findViewById(R.id.filter_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_name_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FilterModel fm = filterName.get(position);
        holder.filterName.setText(fm.getName());
        if (fm.isSelected()) {
            holder.cardView.setBackgroundColor(Color.parseColor("#0675AD"));
            holder.filterName.setTextColor(Color.parseColor("#ffffff"));
            filterName.get(position).setSelected(false);
        } else {
            holder.cardView.setBackgroundColor(Color.parseColor("#B3E5FC"));
            holder.filterName.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return filterName.size();
    }

    public void setSelected(int pos) {
        try {
            filterName.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

