package in.net.maitri.xb.db;

public class TaxMst {

    private String taxType;

    public int getTaxIgstCode() {
        return taxIgstCode;
    }

    public void setTaxIgstCode(int taxIgstCode) {
        this.taxIgstCode = taxIgstCode;
    }

    private int taxIgstCode;
    private int taxCode;
    private float taxRate;

    public int getTaxIsActive() {
        return taxIsActive;
    }

    public void setTaxIsActive(int taxIsActive) {
        this.taxIsActive = taxIsActive;
    }

    private int taxIsActive;

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
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
