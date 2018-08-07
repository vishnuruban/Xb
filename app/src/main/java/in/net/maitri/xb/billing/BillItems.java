package in.net.maitri.xb.billing;


/**
 * Created by SYSRAJ4 on 16/11/2017.
 */

public class BillItems {

    private float qty;
    private String desc;
    private int cat_id;
    private int item_id;
    private float net_rate;
    private float rate;
    private float taxRate1;
    private float taxRate2;
    private float taxAmt1;
    private float taxAmt2;
    private float taxSaleAmt;

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    private float discount;

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    private String hsn;


    public BillItems () {}

    public BillItems(String desc, int qty, float rate, float amount) {
        this.desc = desc;
        this.qty = qty;
        this.rate = rate;
        this.amount = amount;
    }

    public BillItems( int cat_id, int item_id,String desc, float qty,float net_rate,
                      float rate, float amount) {
        this.desc = desc;
        this.qty = qty;
        this.rate = rate;
        this.cat_id = cat_id;
        this.item_id = item_id;
        this.amount = amount;
        this.net_rate = net_rate;
    }

    public BillItems( int cat_id, int item_id,String desc, float qty,float net_rate,
                      float rate, float amount, float taxRate1, float taxRate2,
                      float taxAmt1, float taxAmt2, float taxSaleAmt, String hsn) {
        this(cat_id, item_id, desc, qty,net_rate, rate, amount);
        this.taxAmt1 = taxAmt1;
        this.taxRate1 = taxRate1;
        this.taxAmt2 = taxAmt2;
        this.taxRate2 = taxRate2;
        this.taxSaleAmt = taxSaleAmt;
        this.hsn = hsn;
    }


    public float getTaxRate1() {
        return taxRate1;
    }

    public void setTaxRate1(float taxRate1) {
        this.taxRate1 = taxRate1;
    }

    public float getTaxRate2() {
        return taxRate2;
    }

    public void setTaxRate2(float taxRate2) {
        this.taxRate2 = taxRate2;
    }

    public float getTaxAmt1() {
        return taxAmt1;
    }

    public void setTaxAmt1(float taxAmt1) {
        this.taxAmt1 = taxAmt1;
    }

    public float getTaxAmt2() {
        return taxAmt2;
    }

    public void setTaxAmt2(float taxAmt2) {
        this.taxAmt2 = taxAmt2;
    }

    public float getTaxSaleAmt() {
        return taxSaleAmt;
    }

    public void setTaxSaleAmt(float taxSaleAmt) {
        this.taxSaleAmt = taxSaleAmt;
    }

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

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getNet_rate() {
        return net_rate;
    }

    public void setNet_rate(float net_rate) {
        this.net_rate = net_rate;
    }

    private float amount;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
