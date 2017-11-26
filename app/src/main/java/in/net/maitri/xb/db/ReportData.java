package in.net.maitri.xb.db;


public class ReportData {

    private String rDescription;
    private String rQty;
    private String rMrp;
    private String rDiscount;
    private String rCategory;

    public String getrCategory() {
        return rCategory;
    }

    public void setrCategory(String rCategory) {
        this.rCategory = rCategory;
    }

    public String getrItem() {
        return rItem;
    }

    public void setrItem(String rItem) {
        this.rItem = rItem;
    }

    private String rItem;

    public String getrDescription() {
        return rDescription;
    }

    public void setrDescription(String rDescription) {
        this.rDescription = rDescription;
    }

    public String getrQty() {
        return rQty;
    }

    public void setrQty(String rQty) {
        this.rQty = rQty;
    }

    public String getrMrp() {
        return rMrp;
    }

    public void setrMrp(String rMrp) {
        this.rMrp = rMrp;
    }

    public String getrDiscount() {
        return rDiscount;
    }

    public void setrDiscount(String rDiscount) {
        this.rDiscount = rDiscount;
    }

    public String getrNetSales() {
        return rNetSales;
    }

    public void setrNetSales(String rNetSales) {
        this.rNetSales = rNetSales;
    }

    private String rNetSales;
}
