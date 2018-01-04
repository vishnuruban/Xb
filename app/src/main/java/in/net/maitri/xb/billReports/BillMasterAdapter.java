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
import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.SalesDet;
import in.net.maitri.xb.db.SalesMst;


/**
 * Created by SYSRAJ4 on 27/11/2017.
 */

public class BillMasterAdapter extends RecyclerView.Adapter<BillMasterAdapter.MyViewHolder> {

    private Context mContext;
    private List<SalesMst> mAllBills;

    BillMasterAdapter(Context context,List<SalesMst> mAllBills){
        mContext = context;
        this.mAllBills = mAllBills;


    }










    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView rBillNo,rQty,rItems,rDiscount,rNetAmt,rPayment,rSno,rDate,rCustomer,rCashier,rCustomerNumber;
        TextView rBillDate;
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);

           rBillNo = (TextView) itemView.findViewById(R.id.reBillno);
            rQty = (TextView) itemView.findViewById(R.id.reQty);
            rItems = (TextView) itemView.findViewById(R.id.reItems);
            rDiscount = (TextView) itemView.findViewById(R.id.reDiscount);
           rNetAmt = (TextView) itemView.findViewById(R.id.reNetAmt);
            rPayment = (TextView) itemView.findViewById(R.id.rePayment);
            rSno =(TextView) itemView.findViewById(R.id.reSno);
            rDate = (TextView) itemView.findViewById(R.id.reDate);
            rCustomer =(TextView) itemView.findViewById(R.id.reCustName);
            rCashier =(TextView) itemView.findViewById(R.id.reCashName);
            rBillDate = (TextView) itemView.findViewById(R.id.reDate);
            cardView  = (CardView) itemView.findViewById(R.id.card_view);
            rCustomerNumber =(TextView) itemView.findViewById(R.id.reCustNumber);
        }
    }


    @Override
    public BillMasterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_report_row, parent, false);
        return new BillMasterAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BillMasterAdapter.MyViewHolder holder, int position) {

        SalesMst billDetails = mAllBills.get(position);
      //  Category categoryDetails = mAllCategories.get(position);

        DecimalFormat df = new DecimalFormat("0.00");

        System.out.println("BILL NO "+billDetails.getBillNO());
        holder.rSno.setText(String.valueOf(position+1));
        holder.rBillDate.setText(billDetails.getDateTime());
        holder.rBillNo.setText(String.valueOf(billDetails.getBillNO()));
        holder.rQty.setText(String.valueOf((int)billDetails.getQty()));
        holder.rDiscount.setText(df.format(billDetails.getDiscount()));
        holder.rNetAmt.setText(df.format(billDetails.getNetAmt()));
        holder.rItems.setText(String.valueOf(billDetails.getItems()));
        holder.rPayment.setText(String.valueOf(billDetails.getPaymentMode()));
        holder.rCustomer.setText(billDetails.getCustName());
        holder.rCustomerNumber.setText(billDetails.getCustomerNumber());
        holder.rCashier.setText(billDetails.getCashName());
      //  holder.rBillDate.setText("Date  : "+String.valueOf(billDetails.getDateTime()));

        if (billDetails.isSelected()) {
            holder.cardView.setBackgroundColor(Color.parseColor("#80CBC4"));
           holder.rBillNo.setTextColor(Color.parseColor("#000000"));
            holder.rQty.setTextColor(Color.parseColor("#000000"));
            holder.rDiscount.setTextColor(Color.parseColor("#000000"));
            holder.rNetAmt.setTextColor(Color.parseColor("#000000"));
            holder.rItems.setTextColor(Color.parseColor("#000000"));
            holder.rPayment.setTextColor(Color.parseColor("#000000"));
           // holder.rBillDate.setTextColor(Color.parseColor("#ffffff"));
            billDetails.setSelected(false);

        } else {
            holder.cardView.setBackgroundColor(Color.parseColor("#E0F2F1"));
            holder.rBillNo.setTextColor(Color.parseColor("#000000"));
            holder.rQty.setTextColor(Color.parseColor("#000000"));
            holder.rDiscount.setTextColor(Color.parseColor("#000000"));
            holder.rNetAmt.setTextColor(Color.parseColor("#000000"));
            holder.rItems.setTextColor(Color.parseColor("#000000"));
            holder.rPayment.setTextColor(Color.parseColor("#000000"));
         //   holder.rBillDate.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return mAllBills.size();
    }


    public void setSelected(int pos) {
        try {

            mAllBills.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}