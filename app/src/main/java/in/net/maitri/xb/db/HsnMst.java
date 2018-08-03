package in.net.maitri.xb.db;

public class HsnMst {

    public int getHsnId() {
        return hsnId;
    }

    public void setHsnId(int hsnId) {
        this.hsnId = hsnId;
    }

    public int getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(int hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getHsnDesc() {
        return hsnDesc;
    }

    public void setHsnDesc(String hsnDesc) {
        this.hsnDesc = hsnDesc;
    }

    private int hsnId, hsnCode;
    private String hsnDesc;
}
