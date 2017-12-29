package in.net.maitri.xb.db;

public class SalesMst {

    private int billNO, date, customerId;
    private String salesPerson;
    private String paymentMode;
    private String paymentDet;
    private String status;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String prefix;
    private double qty;
    private int  items;

    public int getInternalBillNo() {
        return internalBillNo;
    }

    public void setInternalBillNo(int internalBillNo) {
        this.internalBillNo = internalBillNo;
    }

    private int internalBillNo;

    public SalesMst(int billNO, int date, int customerId, String salesPerson, String paymentMode, String paymentDet, String status, double qty, int items, int internalBillNo, double netAmt, boolean selected, double discount, String dateTime) {
        this.billNO = billNO;
        this.date = date;
        this.customerId = customerId;
        this.salesPerson = salesPerson;
        this.paymentMode = paymentMode;
        this.paymentDet = paymentDet;
        this.status = status;
        this.qty = qty;
        this.items = items;
        this.internalBillNo = internalBillNo;
        this.netAmt = netAmt;
        this.selected = selected;
        this.discount = discount;
        this.dateTime = dateTime;
    }




    private double netAmt;
    private int saleBillNo;

    public SalesMst(int billNO, int date, int customerId, String salesPerson, String paymentMode, String paymentDet, String status, double qty, int items, double netAmt, double discount, String dateTime) {
        this.billNO = billNO;
        this.date = date;
        this.customerId = customerId;
        this.salesPerson = salesPerson;
        this.paymentMode = paymentMode;
        this.paymentDet = paymentDet;
        this.status = status;
        this.qty = qty;
        this.items = items;
        this.netAmt = netAmt;
        this.discount = discount;
        this.dateTime = dateTime;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    private double discount;
    private String dateTime;

    public SalesMst() {
    }

    public SalesMst(int date, int customerId, double qty,
                    double netAmt, double discount, String salesPerson, String paymentMode,
                    String paymentDet, String status) {

        this.date = date;
        this.customerId = customerId;
        this.qty = qty;
        this.netAmt = netAmt;
        this.discount = discount;
        this.salesPerson = salesPerson;
        this.paymentMode = paymentMode;
        this.paymentDet = paymentDet;
        this.status = status;
    }

    public SalesMst(int billNO, int date, int customerId, double qty,
                    double netAmt, double discount, String salesPerson, String paymentMode,
                    String paymentDet, String status) {
        this.billNO = billNO;
        this.date = date;
        this.customerId = customerId;
        this.qty = qty;
        this.netAmt = netAmt;
        this.discount = discount;
        this.salesPerson = salesPerson;
        this.paymentMode = paymentMode;
        this.paymentDet = paymentDet;
        this.status = status;
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

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(double netAmt) {
        this.netAmt = netAmt;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getPaymentDet() {
        return paymentDet;
    }

    public void setPaymentDet(String paymentDet) {
        this.paymentDet = paymentDet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
