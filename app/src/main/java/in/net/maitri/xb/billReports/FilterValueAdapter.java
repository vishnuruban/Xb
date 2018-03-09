package in.net.maitri.xb.billReports;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        CheckBox checkBox;

        MyViewHolder(View itemView) {
            super(itemView);
            filterValueView = (TextView) itemView.findViewById(R.id.filter_value);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
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
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

      /*  holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setSelected(holder.getAdapterPosition(), holder.checkBox);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return filterValue.size();
    }

    public void setSelected(int pos, CheckBox checkBox) {
        if (filterValue.get(pos).isSelected()){
            filterValue.get(pos).setSelected(false);
            checkBox.setChecked(false);
        } else {
            filterValue.get(pos).setSelected(true);
            checkBox.setChecked(true);
        }
    }
}
