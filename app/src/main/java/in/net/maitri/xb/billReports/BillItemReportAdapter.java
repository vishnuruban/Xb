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
import in.net.maitri.xb.db.ReportData;
import in.net.maitri.xb.db.SalesDet;
import in.net.maitri.xb.db.SalesMst;


/**
 * Created by SYSRAJ4 on 27/11/2017.
 */

public class BillItemReportAdapter extends RecyclerView.Adapter<BillItemReportAdapter.MyViewHolder> {

    private Context mContext;
    private List<ReportData> mAllReports;

    BillItemReportAdapter(Context context,List<ReportData> mAllReports){
        mContext = context;
        this.mAllReports = mAllReports;
    }



    class MyViewHolder extends RecyclerView.ViewHolder {

          TextView iSno,iCategory,iItem,iRate,iNetAmt,iQty;
        TextView rBillDate;
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);

            iSno = (TextView) itemView.findViewById(R.id.iSno);
            iCategory = (TextView) itemView.findViewById(R.id.iCatName);
            iItem = (TextView) itemView.findViewById(R.id.iItemName);
            iRate = (TextView) itemView.findViewById(R.id.iRate);
            iQty = (TextView) itemView.findViewById(R.id.iQty);
            iNetAmt = (TextView) itemView.findViewById(R.id.iNetAmt);
            cardView  = (CardView) itemView.findViewById(R.id.card_view);

        }
    }


    @Override
    public BillItemReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_row, parent, false);
        return new BillItemReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BillItemReportAdapter.MyViewHolder holder, int position) {

        ReportData itemBillDetails = mAllReports.get(position);
        //  Category categoryDetails = mAllCategories.get(position);

        DecimalFormat df = new DecimalFormat("0.00");


        holder.iSno.setText(String.valueOf(position+1));
        holder.iQty.setText(itemBillDetails.getrQty());
        holder.iRate.setText(itemBillDetails.getrMrp());
        holder.iNetAmt.setText(itemBillDetails.getrNetSales());
        holder.iCategory.setText(itemBillDetails.getrCategory());
        holder.iItem.setText(itemBillDetails.getrItem());

        //  holder.rBillDate.setText("Date  : "+String.valueOf(billDetails.getDateTime()));



    }

    @Override
    public int getItemCount() {
        return mAllReports.size();
    }


}