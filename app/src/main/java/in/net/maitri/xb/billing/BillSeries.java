package in.net.maitri.xb.billing;

/**
 * Created by SYSRAJ4 on 21/12/2017.
 */

public class BillSeries {

    private int id;
    private String billName;
    private String shortName;
    private String resetType;
    private String prefix;
    private int seed;
    private int CurrentBillNo;

    public String getCashierSelection() {
        return cashierSelection;
    }

    public void setCashierSelection(String cashierSelection) {
        this.cashierSelection = cashierSelection;
    }

    private int default_bill;
    private String cashierSelection;

    private String customerSelection;

    private  int roundOff;

    public int getDefault_bill() {
        return default_bill;
    }

    public void setDefault_bill(int default_bill) {
        this.default_bill = default_bill;
    }

    public  BillSeries()
    {

    }

    public BillSeries(String billName, String shortName, String resetType, String prefix, int seed, int currentBillNo, String customerSelection, String cashierSelection,int roundOff,int default_bill) {

        this.billName = billName;
        this.shortName = shortName;
        this.resetType = resetType;
        this.prefix = prefix;
        this.seed = seed;
        CurrentBillNo = currentBillNo;
        this.customerSelection = customerSelection;
        this.cashierSelection = cashierSelection;
        this.roundOff = roundOff;
        this.default_bill = default_bill;
    }


    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getResetType() {
        return resetType;
    }

    public void setResetType(String resetType) {
        this.resetType = resetType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getCurrentBillNo() {
        return CurrentBillNo;
    }

    public void setCurrentBillNo(int currentBillNo) {
        CurrentBillNo = currentBillNo;
    }

    public String getCustomerSelection() {
        return customerSelection;
    }

    public void setCustomerSelection(String customerSelection) {
        this.customerSelection = customerSelection;
    }

    public int getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(int roundOff) {
        this.roundOff = roundOff;
    }




}
