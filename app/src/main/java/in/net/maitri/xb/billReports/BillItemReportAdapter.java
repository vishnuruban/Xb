package in.net.maitri.xb.billReports;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.net.maitri.xb.R;;
import in.net.maitri.xb.db.ReportData;

 class BillItemReportAdapter extends RecyclerView.Adapter<BillItemReportAdapter.MyViewHolder> {

    private List<ReportData> mAllReports;

    BillItemReportAdapter(List<ReportData> mAllReports) {
        this.mAllReports = mAllReports;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView iSno, iCategory, iItem, iRate, iNetAmt, iQty;
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);

            iSno = (TextView) itemView.findViewById(R.id.iSno);
            iCategory = (TextView) itemView.findViewById(R.id.iCatName);
            iItem = (TextView) itemView.findViewById(R.id.iItemName);
            iRate = (TextView) itemView.findViewById(R.id.iRate);
            iQty = (TextView) itemView.findViewById(R.id.iQty);
            iNetAmt = (TextView) itemView.findViewById(R.id.iNetAmt);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
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
        holder.iSno.setText(String.valueOf(position + 1));
        holder.iQty.setText(itemBillDetails.getrQty());
        holder.iRate.setText(itemBillDetails.getrMrp());
        holder.iNetAmt.setText(itemBillDetails.getrNetSales());
        holder.iCategory.setText(itemBillDetails.getrCategory());
        holder.iItem.setText(itemBillDetails.getrItem());
    }

    @Override
    public int getItemCount() {
        return mAllReports.size();
    }

}