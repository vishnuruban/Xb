package in.net.maitri.xb.db;

public class TaxMst {

    private String taxType;
    private String taxIgstCode;
    private int taxCode;
    private float taxRate;

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getTaxIgstCode() {
        return taxIgstCode;
    }

    public void setTaxIgstCode(String taxIgstCode) {
        this.taxIgstCode = taxIgstCode;
    }

    public int getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(int taxCode) {
        this.taxCode = taxCode;
    }

    public float getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(float taxRate) {
        this.taxRate = taxRate;
    }


}
