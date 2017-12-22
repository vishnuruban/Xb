package in.net.maitri.xb.billing;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billReports.BillMasterAdapter;
import in.net.maitri.xb.db.SalesMst;

/**
 * Created by SYSRAJ4 on 21/12/2017.
 */

public class BillSeriesAdapter extends RecyclerView.Adapter<BillSeriesAdapter.MyViewHolder> {

    private Context mContext;
    private List<BillSeries> mAllBillSeries;

    BillSeriesAdapter(Context mContext, List<BillSeries> mAllBillSeries) {
        this.mContext = mContext;
        this.mAllBillSeries = mAllBillSeries;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView bSno, bName, bShortName, bSeed, bCurrentBillNo, bResetType, bPrefix, bCustomerSelection, bRoundOff;

        CardView cardView;

        MyViewHolder(View view) {
            super(view);

            bSno = (TextView) view.findViewById(R.id.bsSno);
            bName = (TextView) view.findViewById(R.id.bsName);
            bShortName = (TextView) view.findViewById(R.id.bsShortName);
            bResetType = (TextView) view.findViewById(R.id.bsReset);
            bPrefix = (TextView) view.findViewById(R.id.bsPrefix);
            bSeed = (TextView) view.findViewById(R.id.bsSeed);
            bCurrentBillNo = (TextView) view.findViewById(R.id.bsCurrentBillNo);
            bCustomerSelection = (TextView) view.findViewById(R.id.bsCusSelection);
            bRoundOff = (TextView) view.findViewById(R.id.bsRoundOff);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }


    @Override
    public BillSeriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_series_row, parent, false);
        return new BillSeriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BillSeriesAdapter.MyViewHolder holder, int position) {

        BillSeries billSeriesDetails = mAllBillSeries.get(position);
        //  Category categoryDetails = mAllCategories.get(position);

        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat df1 = new DecimalFormat("0.00");

        System.out.println("BILL NA" + billSeriesDetails.getBillName());
        System.out.println("BILL NA" + billSeriesDetails.getShortName());

        holder.bSno.setText(billSeriesDetails.getId());
        holder.bName.setText(billSeriesDetails.getBillName());
        holder.bShortName.setText(billSeriesDetails.getShortName());
        holder.bResetType.setText(billSeriesDetails.getResetType());
        holder.bPrefix.setText(billSeriesDetails.getPrefix());
        holder.bSeed.setText(billSeriesDetails.getSeed());
        holder.bCurrentBillNo.setText(billSeriesDetails.getCurrentBillNo());
        holder.bCustomerSelection.setText(billSeriesDetails.getCustomerSelection());
        holder.bRoundOff.setText(billSeriesDetails.getRoundOff());
        //holder.rBillDate.setText("Date  : "+String.valueOf(billDetails.getDateTime()));

       /* if (billDetails.isSelected()) {
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
        }*/

    }

    @Override
    public int getItemCount() {
        return mAllBillSeries.size();
    }


    public void setSelected(int pos) {
        try {

          //  mAllBillSeries.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
