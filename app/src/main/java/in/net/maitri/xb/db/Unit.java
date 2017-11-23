package in.net.maitri.xb.db;


public class Unit {

    private int id;
    private String desc;
    private int decimalAllowed;

    public Unit(){}

    public Unit(int id, String desc, int decimalAllowed){

        this.id = id;
        this.desc = desc;
        this.decimalAllowed = decimalAllowed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDecimalAllowed() {
        return decimalAllowed;
    }

    public void setDecimalAllowed(int decimalAllowed) {
        this.decimalAllowed = decimalAllowed;
    }
}
