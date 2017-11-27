package in.net.maitri.xb.billing;

/**
 * Created by SYSRAJ4 on 23/11/2017.
 */

public class CustomerList {

    private String name;

    public CustomerList(String name, String mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    private String mobileNumber;
}
