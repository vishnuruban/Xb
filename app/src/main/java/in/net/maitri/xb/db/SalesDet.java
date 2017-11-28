package in.net.maitri.xb.db;

import in.net.maitri.xb.billing.BillItems;

public class SalesDet {

    private int id, billNo, category, item;
    private float qty;

    public BillItems billItems;



    private String itemName;
 public SalesDet()
{

}
    public SalesDet(int billNo, int category, int item){}

    public SalesDet(int billNo, BillItems billItems) {
        this.billNo = billNo;
        this.billItems = billItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

}
