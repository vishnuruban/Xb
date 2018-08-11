package in.net.maitri.xb.util;

import android.content.Context;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import java.math.BigDecimal;

import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.db.TaxMst;
import in.net.maitri.xb.settings.GetSettings;

public class Calculation {

    private Context mContext;

    public  Calculation(Context mContext){
        this.mContext = mContext;
    }

    public BillItems calculateInclusiveGst(Item bItem, float qty, float discount){
        float gst = bItem.getItemGST();
        float rate = (bItem.getItemSP()*qty);
        BigDecimal gstTaxAmt = BigDecimal.valueOf((rate*gst)/(100+gst));
        gstTaxAmt = gstTaxAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal bd_cgst = gstTaxAmt.divide(BigDecimal.valueOf(2)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal bd_gstSaleAmt = BigDecimal.valueOf(rate).subtract(bd_cgst.add(bd_cgst)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        float tax = gstTaxAmt.floatValue();
        float cgst = bd_cgst.floatValue();
        float sgst = cgst;
        float gstSaleAmt =  bd_gstSaleAmt.floatValue();
        BillItems billItems = new BillItems(bItem.getCategoryId(), bItem.getId(),
                bItem.getItemName(), qty, bItem.getItemSP(), bItem.getItemSP(), rate,
                gst/2, gst/2, cgst, sgst,gstSaleAmt, bItem.getItemHSNcode());
        billItems.setDiscount(0);
        Log.d("Tax", String.valueOf(tax));
        Log.d("GST/2", String.valueOf(gst/2));
        Log.d("CGST", String.valueOf(cgst));
        Log.d("SGST", String.valueOf(sgst));
        Log.d("GSTSaleAmt", String.valueOf(gstSaleAmt));
        return billItems;
    }

    public BillItems calculateInclusiveGst(Item bItem, float qty, float discount, float newPrice){
        float gst = bItem.getItemGST();
        float rate = (newPrice*qty);
        BigDecimal gstTaxAmt = BigDecimal.valueOf((rate*gst)/(100+gst));
        gstTaxAmt = gstTaxAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal bd_cgst = gstTaxAmt.divide(BigDecimal.valueOf(2)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal bd_gstSaleAmt = BigDecimal.valueOf(rate).subtract(bd_cgst.add(bd_cgst)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        float tax = gstTaxAmt.floatValue();
        float cgst = bd_cgst.floatValue();
        float sgst = cgst;
        float gstSaleAmt =  bd_gstSaleAmt.floatValue();
        BillItems billItems = new BillItems(bItem.getCategoryId(), bItem.getId(),
                bItem.getItemName(), qty, bItem.getItemSP(), rate, rate,
                gst/2, gst/2, cgst, sgst,gstSaleAmt, bItem.getItemHSNcode());
        billItems.setDiscount(0);
        Log.d("Tax", String.valueOf(tax));
        Log.d("GST/2", String.valueOf(gst/2));
        Log.d("CGST", String.valueOf(cgst));
        Log.d("SGST", String.valueOf(sgst));
        Log.d("GSTSaleAmt", String.valueOf(gstSaleAmt));
        return billItems;
    }

    public void calculateInclusiveGst(BillItems bi, float qty, float discount){
        Float oldQty =  bi.getQty();
        Float totalDiscount = BigDecimal.valueOf(discount * bi.getRate() * qty)
                .setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
        Float amt = (qty * bi.getRate())- totalDiscount;
        bi.setAmount(qty * bi.getRate());
        if (discount == 0) {
            bi.setQty(qty);
            bi.setTaxAmt1((bi.getTaxAmt1() / oldQty) * qty);
            bi.setTaxAmt2((bi.getTaxAmt2() / oldQty) * qty);
            bi.setTaxSaleAmt((bi.getTaxSaleAmt() / oldQty) * qty);
        } else {
            float gst = bi.getTaxRate1() + bi.getTaxRate2();
            BigDecimal gstTaxAmt = BigDecimal.valueOf((amt*gst)/(100+gst));
            gstTaxAmt = gstTaxAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal bd_cgst = gstTaxAmt.divide(BigDecimal.valueOf(2)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal bd_gstSaleAmt = BigDecimal.valueOf(amt).subtract(bd_cgst.add(bd_cgst)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            float tax = gstTaxAmt.floatValue();
            float cgst = bd_cgst.floatValue();
            float sgst = cgst;
            float gstSaleAmt =  bd_gstSaleAmt.floatValue();
            bi.setTaxAmt1(cgst);
            bi.setTaxAmt2(sgst);
            bi.setTaxSaleAmt(gstSaleAmt);
            bi.setDiscount(totalDiscount);
            Log.d("Tax", String.valueOf(tax));
            Log.d("GST/2", String.valueOf(gst/2));
            Log.d("CGST", String.valueOf(cgst));
            Log.d("SGST", String.valueOf(sgst));
            Log.d("GSTSaleAmt", String.valueOf(gstSaleAmt));
        }
    }

    public void roundOffCalculation(float netAmount){
        GetSettings mGetSettings = new GetSettings(mContext);
        int roundOffUpto = Integer.parseInt(mGetSettings.getRoundOffUpto());
        if (roundOffUpto == 0) {
            Toast.makeText(mContext, "Roundoff amt can't be zero. " +
                    "Please change value in settings", Toast.LENGTH_SHORT).show();
        } else {
            float roundOffInRupees = BigDecimal.valueOf(roundOffUpto/100)
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
            float extraAmt = BigDecimal.valueOf(netAmount%roundOffInRupees)
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
            switch (mGetSettings.getRoundOffDirection()){
                case "1":
                    float amountToBeAdd = roundOffInRupees - extraAmt;
                    netAmount += amountToBeAdd;
                    break;
                case "2":
                    netAmount -= extraAmt;
                    break;
            }
        }
    }

}
