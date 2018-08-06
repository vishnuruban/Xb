package in.net.maitri.xb.util;

import android.util.Log;

import java.math.BigDecimal;

import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.db.Item;

public class Calculation {

    public BillItems calculateInclusiveGst(Item bItem, float qty, float discount){
        float gst = bItem.getItemGST();
        float rate = (bItem.getItemSP()*qty) - discount;
        BigDecimal gstTaxAmt = BigDecimal.valueOf((rate*gst)/(100+gst));
        gstTaxAmt = gstTaxAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal bd_cgst = gstTaxAmt.divide(BigDecimal.valueOf(2)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal bd_gstSaleAmt = BigDecimal.valueOf(bItem.getItemSP()).subtract(bd_cgst.add(bd_cgst)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        float tax = gstTaxAmt.floatValue();
        float cgst = bd_cgst.floatValue();
        float sgst = cgst;
        float gstSaleAmt =  bd_gstSaleAmt.floatValue();
        BillItems billItems = new BillItems(bItem.getCategoryId(), bItem.getId(),
                bItem.getItemName(), qty, bItem.getItemSP(), bItem.getItemSP(), bItem.getItemSP() * qty,
                gst/2, gst/2, cgst, sgst,gstSaleAmt, bItem.getItemHSNcode());
        Log.d("Tax", String.valueOf(tax));
        Log.d("GST/2", String.valueOf(gst/2));
        Log.d("CGST", String.valueOf(cgst));
        Log.d("SGST", String.valueOf(sgst));
        Log.d("GSTSaleAmt", String.valueOf(gstSaleAmt));
        return billItems;
    }
}
