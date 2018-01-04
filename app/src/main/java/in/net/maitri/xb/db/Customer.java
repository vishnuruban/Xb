package in.net.maitri.xb.db;


import java.io.Serializable;

public class Customer implements Serializable {

    private int id;
    private String mobileno;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String gstin;

    public Customer()
    {

    }

    public Customer(int id,String name, String mobileno) {
        this.id = id;
        this.mobileno = mobileno;
        this.name = name;
    }

    public Customer(String name, String mobileno, String email, String address1, String address2, String city, String state, String gstin) {
        this.mobileno = mobileno;
        this.name = name;
        this.address1 = address1;

        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.gstin = gstin;
        this.email = email;
    }

    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
