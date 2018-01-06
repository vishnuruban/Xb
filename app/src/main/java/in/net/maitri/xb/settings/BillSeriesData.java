package in.net.maitri.xb.settings;


import android.preference.Preference;

import java.io.Serializable;

public class BillSeriesData implements Serializable{

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbNumber() {
        return bNumber;
    }

    public void setbNumber(String bNumber) {
        this.bNumber = bNumber;
    }

    public String getbPrefix() {
        return bPrefix;
    }

    public void setbPrefix(String bPrefix) {
        this.bPrefix = bPrefix;
    }

    public String getbCashier() {
        return bCashier;
    }

    public void setbCashier(String bCashier) {
        this.bCashier = bCashier;
    }

    public String getbCustomer() {
        return bCustomer;
    }

    public void setbCustomer(String bCustomer) {
        this.bCustomer = bCustomer;
    }

    public Preference getgCashier() {
        return gCashier;
    }

    public void setgCashier(Preference gCashier) {
        this.gCashier = gCashier;
    }

    public Preference getgCustomer() {
        return gCustomer;
    }

    public void setgCustomer(Preference gCustomer) {
        this.gCustomer = gCustomer;
    }

    public Preference getgBillNo() {
        return gBillNo;
    }

    public void setgBillNo(Preference gBillNo) {
        this.gBillNo = gBillNo;
    }

    public Preference getgPrefix() {
        return gPrefix;
    }

    public void setgPrefix(Preference gPrefix) {
        this.gPrefix = gPrefix;
    }

    private String bName;
    private String bNumber;
    private String bPrefix;
    private String bCashier;
    private String bCustomer;
    Preference gCashier;
    Preference gCustomer;
    Preference gBillNo;
    Preference gPrefix;
}
