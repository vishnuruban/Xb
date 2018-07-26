package in.net.maitri.xb.printing.sunmi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.settings.GetSettings;

public class SunmiPrint {

    private DecimalFormat df = new DecimalFormat("0.00");
    private BluetoothService mService;
    private Context mContext;
    private ArrayList<BillItems> mBillItems;
    private double mNetAmount;
    private String mBillNo;
    private String mTotalPrice;
    private String mTotalDiscount;
    private double mTotalQty;
    private String mBillDate;
    private String mCashierName;
    private String mCustomerName;

    public SunmiPrint(Context mContext,BluetoothService mService, ArrayList<BillItems> mBillItems,
                      double mNetAmount, String mBillNo, String mTotalPrice, String mTotalDiscount,
                      double mTotalQty, String mBillDate, String mCashierName, String mCustomerName) {

        this.mContext = mContext;
        this.mBillItems = mBillItems;
        this.mNetAmount = mNetAmount;
        this.mBillNo = mBillNo;
        this.mTotalPrice = mTotalPrice;
        this.mTotalDiscount = mTotalDiscount;
        this.mTotalQty = mTotalQty;
        this.mBillDate = mBillDate;
        this.mCashierName = mCashierName;
        this.mCustomerName = mCustomerName;
        this.mService = mService;
    }


    public void doPrint() {

        String companyName = new GetSettings(mContext).getCompanyLegalName();
        if (companyName.isEmpty()){
            Toast.makeText(mContext, "Company legal name not found. Enter details in settings."
                    , Toast.LENGTH_SHORT).show();
        }else {
            int effectivePrintWidth = 48;
            int minChars = 4;//this defines minimum characters per column; since you can
            // create multiple rows, this value will define how many columns you want per row
   /*     int totalNoOfColumns = 2;//no of columns
        boolean doubleWidthColumnExists = true;//this is true if you want a column with double space than other others
        int doubleWidthColRank = 0;//this is the rank of column which will have double space than other columns; starts from 0
        int rightAlignColRank = 3;//this is the rank of column whose content is right aligned; starts from 0*/

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");
            String formattedDate = dateFormat.format(new Date());

            //mService.printBitmap(new PrintPic().printDraw(bmp));
            String item = String.format("%-25s%5s\n", "Item", "");
            String s = String.format("%6s%12s%12s\n", "Qty", "Price", "Amount");
            String total = String.format("%5s%26s\n", "Total", mTotalPrice);
            String dash = "--------------------------------";

   /*     String salesManText = String.format("%-10s%-20s\n", "Salesman:", salesman);
        String commentText = String.format("%-10s%-20s\n", "Comment:", comment);

        */

            PlainPrint pp = new PlainPrint(mContext, effectivePrintWidth, minChars);
            byte[] normalText = {27, 33, 0};
            byte[] boldText = {27, 33, 0};
            boldText[2] = ((byte) (0x8 | normalText[2]));
            byte[] tail = new byte[]{10, 13, 0};
            pp.startAddingContent4printFields();
            pp.addTextCenterAlign(companyName, true);
            for (int i = 0; i < getCompanyHeaders().length; i++) {
                if (!getCompanyHeaders()[i].isEmpty()) {
                    pp.addTextCenterAlign(getCompanyHeaders()[i], true);
                }
            }
            mService.write(boldText);
            mService.sendMessage(pp.getContent4PrintFields(), "");
            mService.write(tail);
            pp.startAddingContent4printFields();
            pp.addTextCenterAlign("Bill No : " + mBillNo, true);
            pp.addTextCenterAlign("Date : " + formattedDate, true);
            pp.addStarsFullLine();
            mService.write(normalText);
            mService.sendMessage(pp.getContent4PrintFields(), "");
            mService.write(tail);
            pp.startAddingContent4printFields();
            mService.write(boldText);
            mService.sendMessage(item, "");
            mService.sendMessage(s, "");
            mService.write(normalText);
            mService.sendMessage(dash, "");

            for (int i = 0; i < mBillItems.size(); i++) {
                BillItems billItems = mBillItems.get(i);
                item = String.format("%-25s%5s\n", billItems.getDesc(), "");
                mService.sendMessage(item, "");
                String value = String.format("%7s%12s%12s\n", billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
                mService.sendMessage(value, "");
            }

            mService.sendMessage(dash, "");
            mService.write(boldText);
            mService.sendMessage(total, "");
            mService.write(normalText);
            mService.sendMessage(dash, "");
            pp.startAddingContent4printFields();
            GetSettings getSettings = new GetSettings(mContext);
            String[] footerArray = {getSettings.getFooterText1(), getSettings.getFooterText2(),
                    getSettings.getFooterText3(), getSettings.getFooterText4()};
            for (String aFooterArray : footerArray) {
                if (!aFooterArray.isEmpty()) {
                    pp.addTextCenterAlign(aFooterArray, true);
                }
            }
            pp.addNewLine();
            pp.addNewLine();
            pp.addNewLine();
            pp.addNewLine();
            pp.addNewLine();
            mService.sendMessage(pp.getContent4PrintFields(), "");
        }
    }

    private String[] getCompanyHeaders() {
        GetSettings getSettings = new GetSettings(mContext);
        String cityPin = getSettings.getCompanyCity() + "-" + getSettings.getCompanyPincode();
        String phNumber = "Ph:" + getSettings.getCompanyPhoneNo();
        return new String[]{getSettings.getCompanyAddressLine1(),
                getSettings.getCompanyAddressLine2(), getSettings.getCompanyAddressLine3(),
                cityPin, phNumber, getSettings.getCompanyGstNo()};
    }
}
