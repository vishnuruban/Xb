package in.net.maitri.xb.billing;

/**
 * Created by SYSRAJ4 on 16/11/2017.
 */

public class BillItems {


    private String desc;

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    private int qty;
    private double rate;

    public BillItems( int cat_id, int item_id,String desc, int qty, double rate, double amount) {
        this.desc = desc;
        this.qty = qty;
        this.rate = rate;
        this.cat_id = cat_id;
        this.item_id = item_id;
        this.amount = amount;
    }

    private int cat_id;
    private int item_id;

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
