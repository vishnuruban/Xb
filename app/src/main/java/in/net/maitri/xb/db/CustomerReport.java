package in.net.maitri.xb.db;

/**
 * Created by SYSRAJ4 on 20/02/2018.
 */

public class CustomerReport {
    private String cName;
    private String cNumber;
    private int cItemCount;

    public String getcNumber() {
        return cNumber;
    }

    public void setcNumber(String cNumber) {
        this.cNumber = cNumber;
    }

    private int cQtyCount;
    private int cNetAmt;


    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getcItemCount() {
        return cItemCount;
    }

    public void setcItemCount(int cItemCount) {
        this.cItemCount = cItemCount;
    }

    public int getcQtyCount() {
        return cQtyCount;
    }

    public void setcQtyCount(int cQtyCount) {
        this.cQtyCount = cQtyCount;
    }

    public int getcNetAmt() {
        return cNetAmt;
    }

    public void setcNetAmt(int cNetAmt) {
        this.cNetAmt = cNetAmt;
    }
}
