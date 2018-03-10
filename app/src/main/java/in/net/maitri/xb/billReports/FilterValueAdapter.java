package in.net.maitri.xb.billReports;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        ImageView checkBox;

        MyViewHolder(View itemView) {
            super(itemView);
            filterValueView = (TextView) itemView.findViewById(R.id.filter_value);
            checkBox = (ImageView) itemView.findViewById(R.id.checkbox);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_value_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        FilterModel fm = filterValue.get(position);
        holder.filterValueView.setText(fm.getName());
        if (fm.isSelected()) {
            holder.checkBox.setImageResource(R.mipmap.ic_check_box_black_24dp);
        } else {
            holder.checkBox.setImageResource(R.mipmap.ic_check_box_outline_blank_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return filterValue.size();
    }

    public void setSelected(int pos, ImageView checkBox) {
        if (filterValue.get(pos).isSelected()){
            filterValue.get(pos).setSelected(false);
            checkBox.setImageResource(R.mipmap.ic_check_box_outline_blank_black_24dp);
        } else {
            filterValue.get(pos).setSelected(true);
            checkBox.setImageResource(R.mipmap.ic_check_box_black_24dp);
        }
    }
}
