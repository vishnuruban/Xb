package in.net.maitri.xb.billReports;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.net.maitri.xb.R;


class FilterNameAdapter extends RecyclerView.Adapter<FilterNameAdapter.MyViewHolder> {

    private ArrayList<String> filterName;

    FilterNameAdapter(ArrayList<String> filterName) {
        this.filterName = filterName;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView filterName;

        MyViewHolder(View itemView) {
            super(itemView);
            filterName = (TextView) itemView.findViewById(R.id.filter_name);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_name_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name = filterName.get(position);
        holder.filterName.setText(name);
    }

    @Override
    public int getItemCount() {
        return filterName.size();
    }


}

