package in.net.maitri.xb.billing;

/**
 * Created by SYSRAJ4 on 16/11/2017.
 */

public class BillItems {


    private String desc;
    private int qty;
    private double rate;

    public BillItems(String desc, int qty, double rate, double amount) {
        this.desc = desc;
        this.qty = qty;
        this.rate = rate;
        this.amount = amount;
    }

    private double amount;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
