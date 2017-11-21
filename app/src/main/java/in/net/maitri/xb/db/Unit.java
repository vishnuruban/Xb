package in.net.maitri.xb.db;


public class Unit {

    private int id;
    private String desc;

    public Unit(){}

    public Unit(int id, String desc){

        this.id = id;
        this.desc = desc;
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
}
