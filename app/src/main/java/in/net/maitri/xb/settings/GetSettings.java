package in.net.maitri.xb.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GetSettings {

    private Context mContext;

    public GetSettings(Context context){
        mContext = context;
    }

    //Company Settings
    private SharedPreferences getSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }
    public String getCompanyLegalName(){
        return getSharedPreferences().getString("key_settings_company_legal_name","");
    }
    public String getCompanyTradeName(){
        return getSharedPreferences().getString("key_settings_company_trade_name","");
    }
    public String getCompanyAddressLine1(){
        return getSharedPreferences().getString("key_settings_company_address1","");
    }
    public String getCompanyAddressLine2(){
        return getSharedPreferences().getString("key_settings_company_address2","");
    }
    public String getCompanyAddressLine3(){
        return getSharedPreferences().getString("key_settings_company_address3","");
    }
    public String getCompanyPincode(){
        return getSharedPreferences().getString("key_settings_company_pincode","");
    }
    public String getCompanyCity(){
        return getSharedPreferences().getString("key_settings_company_city","");
    }
    public String getCompanyState(){
        return getSharedPreferences().getString("key_settings_company_state","");
    }
    public String getCompanyPhoneNo(){
        return getSharedPreferences().getString("key_settings_company_phone_no","");
    }
    public String getCompanyEmail(){
        return getSharedPreferences().getString("key_settings_company_email","");
    }
    public String getCompanyRegistrationType(){
        return getSharedPreferences().getString("key_settings_company_registration_type","");
    }
    public String getCompanyGstNo(){
        return getSharedPreferences().getString("key_settings_company_gst_no","");
    }
    public String getCompanyPanNo(){
        return getSharedPreferences().getString("key_settings_company_pan_no","");
    }
    public String getCompanyCinNo(){
        return getSharedPreferences().getString("key_settings_company_cin_no","");
    }
    public String getCompanyAuthorisedSignatory(){
        return getSharedPreferences().getString("key_settings_authorised_signatory","");
    }

    //Bill Settings
    public String getBillTaxType(){
        return getSharedPreferences().getString("key_settings_bill_inclusive_exclusive","");
    }
    public boolean getBillSalesPersonRequired(){
        return getSharedPreferences().getBoolean("key_settings_bill_sales_person_rqd",false);
    }
    public boolean getBillRoundOfBill(){
        return getSharedPreferences().getBoolean("key_settings_bill_round_cash",false);
    }
    public String getBillBillName(){
        return getSharedPreferences().getString("key_settings_bill_suffix_prefix_name","");
    }
    public String getBillBillNameAs(){
        return getSharedPreferences().getString("key_settings_bill_suffix_prefix","");
    }
    public String getBillStartingBillNo(){
        return getSharedPreferences().getString("key_settings_company_starting_bill_no","");
    }
    public String getBillMaximumDiscountPercentage(){
        return getSharedPreferences().getString("key_settings_bill_discount_percentage","");
    }
}
