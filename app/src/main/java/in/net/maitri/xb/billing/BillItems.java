package in.net.maitri.xb.billing;

import java.math.BigDecimal;

/**
 * Created by SYSRAJ4 on 16/11/2017.
 */

public class BillItems {

    private double qty;
    private String desc;
    private int cat_id;
    private int item_id;
    private double net_rate;
    private double rate;
    private BigDecimal taxRate1;
    private BigDecimal taxRate2;
    private BigDecimal taxAmt1;
    private BigDecimal taxAmt2;
    private BigDecimal taxSaleAmt;


    public BillItems () {}

    public BillItems(String desc, int qty, double rate, double amount) {
        this.desc = desc;
        this.qty = qty;
        this.rate = rate;
        this.amount = amount;
    }

    public BillItems( int cat_id, int item_id,String desc, double qty,double net_rate,
                      double rate, double amount) {
        this.desc = desc;
        this.qty = qty;
        this.rate = rate;
        this.cat_id = cat_id;
        this.item_id = item_id;
        this.amount = amount;
        this.net_rate = net_rate;
    }

    public BillItems( int cat_id, int item_id,String desc, double qty,double net_rate,
                      double rate, double amount, BigDecimal taxRate1, BigDecimal taxRate2,
                      BigDecimal taxAmt1, BigDecimal taxAmt2, BigDecimal taxSaleAmt) {
        this(cat_id, item_id, desc, qty,net_rate, rate, amount);
        this.taxAmt1 = taxAmt1;
        this.taxRate1 = taxRate1;
        this.taxAmt2 = taxAmt2;
        this.taxRate2 = taxRate2;
        this.taxSaleAmt = taxSaleAmt;
    }


    public BigDecimal getTaxRate1() {
        return taxRate1;
    }

    public void setTaxRate1(BigDecimal taxRate1) {
        this.taxRate1 = taxRate1;
    }

    public BigDecimal getTaxRate2() {
        return taxRate2;
    }

    public void setTaxRate2(BigDecimal taxRate2) {
        this.taxRate2 = taxRate2;
    }

    public BigDecimal getTaxAmt1() {
        return taxAmt1;
    }

    public void setTaxAmt1(BigDecimal taxAmt1) {
        this.taxAmt1 = taxAmt1;
    }

    public BigDecimal getTaxAmt2() {
        return taxAmt2;
    }

    public void setTaxAmt2(BigDecimal taxAmt2) {
        this.taxAmt2 = taxAmt2;
    }

    public BigDecimal getTaxSaleAmt() {
        return taxSaleAmt;
    }

    public void setTaxSaleAmt(BigDecimal taxSaleAmt) {
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

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getNet_rate() {
        return net_rate;
    }

    public void setNet_rate(double net_rate) {
        this.net_rate = net_rate;
    }

    private double amount;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
