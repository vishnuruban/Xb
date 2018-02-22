package in.net.maitri.xb.billReports;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.BillItemAdapter;
import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.CustomerReport;
import in.net.maitri.xb.db.ReportData;
import in.net.maitri.xb.db.SalesDet;
import in.net.maitri.xb.db.SalesMst;


/**
 * Created by SYSRAJ4 on 27/11/2017.
 */

public class BillCustomerReportAdapter extends RecyclerView.Adapter<BillCustomerReportAdapter.MyViewHolder> {

    private Context mContext;
    private List<CustomerReport> cReport;

    BillCustomerReportAdapter(Context context,List<CustomerReport> cReport){
        mContext = context;
        this.cReport = cReport;
    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cSno,cCusName,cItemCount,cNetAmt,cQtyCount,cCusNumber;
        TextView rBillDate;
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);

            cSno = (TextView) itemView.findViewById(R.id.cSno);
            cCusName = (TextView) itemView.findViewById(R.id.cCusName);
            cItemCount = (TextView) itemView.findViewById(R.id.cItemCount);
            cQtyCount = (TextView) itemView.findViewById(R.id.cQtyCount);
            cNetAmt = (TextView) itemView.findViewById(R.id.cTotalNetAmt);
            cCusNumber = (TextView) itemView.findViewById(R.id.cCusNumber);
            cardView  = (CardView) itemView.findViewById(R.id.card_view);

        }
    }


    @Override
    public BillCustomerReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_customer_row, parent, false);
        return new BillCustomerReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BillCustomerReportAdapter.MyViewHolder holder, int position) {

        CustomerReport customerReport = cReport.get(position);
        //  Category categoryDetails = mAllCategories.get(position);

        DecimalFormat df = new DecimalFormat("0.00");


        holder.cSno.setText(String.valueOf(position+1));
        holder.cQtyCount.setText(df.format(customerReport.getcQtyCount()));
        holder.cNetAmt.setText(df.format(customerReport.getcNetAmt()));
        holder.cCusName.setText(customerReport.getcName());
        holder.cCusNumber.setText(customerReport.getcNumber());
        holder.cItemCount.setText(df.format(customerReport.getcItemCount()));


        //  holder.rBillDate.setText("Date  : "+String.valueOf(billDetails.getDateTime()));



    }

    @Override
    public int getItemCount() {
        return cReport.size();
    }


}