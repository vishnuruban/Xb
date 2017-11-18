package in.net.maitri.xb.db;



public class Customer {

    private int id;
    private int mobileno;
    private String name, address, gstin;

    public Customer(){}

    public Customer(String name, int mobileno, String gstin, String address){
        this.name = name;
        this.mobileno = mobileno;
        this.gstin = gstin;
        this.address = address;
    }

    public Customer(int id, String name, int mobileno, String gstin, String address){
        this.id = id;
        this.name = name;
        this.mobileno = mobileno;
        this.gstin = gstin;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMobileno() {
        return mobileno;
    }

    public void setMobileno(int mobileno) {
        this.mobileno = mobileno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

}
