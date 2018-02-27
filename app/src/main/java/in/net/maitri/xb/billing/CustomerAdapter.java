package in.net.maitri.xb.billing;

import android.content.Context;;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Customer;

public class CustomerAdapter extends BaseAdapter implements Filterable {

    private ArrayList<Customer> mOriginalValues; // Original Values
    private ArrayList<Customer> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;
    Context context ;
    String mode;

    public CustomerAdapter (Context context,ArrayList<Customer> customerLists,String mode)
    {
        this.context = context;
        this.mOriginalValues = customerLists;
        this.mDisplayedValues = customerLists;
        this.mode = mode;
        inflater = LayoutInflater.from(context);
        System.out.println("MODE "+mode);
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int i) {
        return mDisplayedValues.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    private class ViewHolder {
        LinearLayout llContainer;
        TextView cName,cMobileNumber;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.customer_list_row, null);
            holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
            holder.cName = (TextView) convertView.findViewById(R.id.cName);
            holder.cMobileNumber = (TextView) convertView.findViewById(R.id.cNumber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cName.setText(mDisplayedValues.get(position).getName());
        holder.cMobileNumber.setText(mDisplayedValues.get(position).getMobileno()+"");
        Log.i("CID " ,String.valueOf(mDisplayedValues.get(position).getId()));

//        holder.llContainer.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                Toast.makeText(context, mDisplayedValues.get(position).getName(), Toast.LENGTH_SHORT).show();
//
//            }
//        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<Customer>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Customer> FilteredArrList = new ArrayList<Customer>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Customer>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {

                        String data ="";
                        String data1 ="";
                        if(mode.equals("NAME")) {
                            data = mOriginalValues.get(i).getName();
                            data1 =  mOriginalValues.get(i).getMobileno();
                        }
                        else if(mode.equals("PHONE NUMBER"))
                        {
                            data = mOriginalValues.get(i).getMobileno();
                        }
                        if (data.toLowerCase().contains(constraint.toString()) ||data1.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new Customer(mOriginalValues.get(i).getId(),mOriginalValues.get(i).getName(),mOriginalValues.get(i).getMobileno()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    public void clear()
    {
        mDisplayedValues.clear();
    }
}
