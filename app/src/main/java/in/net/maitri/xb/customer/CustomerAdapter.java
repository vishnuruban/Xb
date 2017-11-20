package in.net.maitri.xb.customer;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Customer;

class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Customer> mCustomerList;

    CustomerAdapter(Context context, List<Customer> customerList) {
        mContext = context;
        mCustomerList = customerList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView customerName, customerNumber;

        MyViewHolder(View itemView) {
            super(itemView);
            customerName = (TextView) itemView.findViewById(R.id.customer_name);
            customerNumber = (TextView) itemView.findViewById(R.id.customer_number);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_view, parent, false);
        return new CustomerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Customer customer = mCustomerList.get(position);
        String name = customer.getName();
        String mob = customer.getMobileno();
        holder.customerName.setText(name);
        holder.customerNumber.setText(mob);
    }

    @Override
    public int getItemCount() {
        return mCustomerList.size();
    }
}
