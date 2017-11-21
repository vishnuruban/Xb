package in.net.maitri.xb.db;

public class SalesMst {

    private int billNO, date, customerId;
    private String  salesPerson, paymentMode;
    private float qty;
    private float netAmt;
    private float discount;

    public SalesMst(){}

    public SalesMst( int date, int customerId, float qty,
                    float netAmt, float discount, String salesPerson, String paymentMode){

        this.date = date;
        this.customerId = customerId;
        this.qty = qty;
        this.netAmt = netAmt;
        this.discount = discount;
        this.salesPerson = salesPerson;
        this.paymentMode = paymentMode;
    }

    public SalesMst( int billNO, int date, int customerId, float qty,
                    float netAmt, float discount, String salesPerson, String paymentMode){
        this.billNO = billNO;
        this.date = date;
        this.customerId = customerId;
        this.qty = qty;
        this.netAmt = netAmt;
        this.discount = discount;
        this.salesPerson = salesPerson;
        this.paymentMode = paymentMode;
    }


    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getBillNO() {
        return billNO;
    }

    public void setBillNO(int billNO) {
        this.billNO = billNO;
    }

    public String getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(float netAmt) {
        this.netAmt = netAmt;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }


}
