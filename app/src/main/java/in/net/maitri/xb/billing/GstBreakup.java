package in.net.maitri.xb.billing;

import android.support.annotation.NonNull;

public class GstBreakup implements Comparable<GstBreakup> {

    private String hsn, gst;

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public float getCgst() {
        return cgst;
    }

    public void setCgst(float cgst) {
        this.cgst = cgst;
    }

    public float getSgst() {
        return sgst;
    }

    public void setSgst(float sgst) {
        this.sgst = sgst;
    }

    public float getTaxNetAmr() {
        return taxNetAmr;
    }

    public void setTaxNetAmr(float taxNetAmr) {
        this.taxNetAmr = taxNetAmr;
    }

    private float cgst,sgst,taxNetAmr;


    @Override
    public int compareTo(@NonNull GstBreakup o) {
        return Integer.parseInt(this.getHsn()) - Integer.parseInt(o.getHsn());
    }
}
