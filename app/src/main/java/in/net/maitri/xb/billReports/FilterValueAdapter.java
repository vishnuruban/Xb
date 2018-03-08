package in.net.maitri.xb.billReports;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.net.maitri.xb.R;

class FilterValueAdapter extends RecyclerView.Adapter<FilterValueAdapter.MyViewHolder> {


    private ArrayList<FilterModel> filterValue;

    FilterValueAdapter(ArrayList<FilterModel> filterValue) {
        this.filterValue = filterValue;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView filterValueView;

        MyViewHolder(View itemView) {
            super(itemView);
            filterValueView = (TextView) itemView.findViewById(R.id.filter_value);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_value_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FilterModel fm = filterValue.get(position);
        holder.filterValueView.setText(fm.getName());
    }

    @Override
    public int getItemCount() {
        return filterValue.size();
    }
}
