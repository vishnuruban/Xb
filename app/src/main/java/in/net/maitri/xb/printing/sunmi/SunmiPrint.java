package in.net.maitri.xb.printing.sunmi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.billing.GstBreakup;
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

    public void doPrintForInclusiveGst() {

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

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            String formattedDate = dateFormat.format(new Date());

            //mService.printBitmap(new PrintPic().printDraw(bmp));
            String gstBreakupHeader = String.format("%4s%5s%8s%7s%7s\n", "HSN", "GST%", "Basic", "CGST", "SGST");
            String item = String.format("%-25s%5s\n", "HSN-Item", "");
            String s = String.format("%5s%10s%5s%10s\n", "Qty", "Rate", "GST%", "Amount");
            String total = String.format("%5s%12s%14s\n", mTotalQty, "Sub Total", mTotalPrice);
            String discount =  String.format("%-8s%23s\n", "Discount", "-" + mTotalDiscount);
            String grandTotal =  String.format("%-15s%16s\n", "Grand Total", df.format(mNetAmount));
            String billDate = String.format("%-14s%-15s\n", "Bill No:" + mBillNo, "Date:" + formattedDate);
            String dash = "--------------------------------";

   /*     String salesManText = String.format("%-10s%-20s\n", "Salesman:", salesman);
        String commentText = String.format("%-10s%-20s\n", "Comment:", comment);

        */
            List<GstBreakup> listBreakUp = gstBreakupValues();
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
            mService.sendMessage(billDate, "");
            pp.startAddingContent4printFields();
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
                item = String.format("%-31s\n", billItems.getHsn() + "-" + billItems.getDesc());
                mService.sendMessage(item, "");
                String value = String.format("%5s%10s%5s%10s\n", billItems.getQty(),df.format(billItems.getRate()),
                        billItems.getTaxRate1() + billItems.getTaxRate2(), df.format(billItems.getAmount()));
                mService.sendMessage(value, "");
            }
            if (Float.valueOf(mTotalDiscount) != 0) {
                mService.sendMessage(dash, "");
                mService.write(boldText);
                mService.sendMessage(total, "");
                mService.write(normalText);
                mService.sendMessage(discount, "");
            }

            mService.sendMessage(dash, "");
            mService.write(boldText);
            mService.sendMessage(grandTotal, "");
            mService.write(normalText);
            mService.sendMessage(dash, "");
            pp.startAddingContent4printFields();

            pp.addNewLine();
            pp.addNewLine();
            mService.sendMessage(pp.getContent4PrintFields(), "");
            mService.write(boldText);
            mService.sendMessage(gstBreakupHeader, "");
            mService.write(normalText);
            pp.startAddingContent4printFields();
            for (int i = 0; i< listBreakUp.size(); i++){
                GstBreakup gb = listBreakUp.get(i);
                String value = String.format("%4s%1s%5s%8s%7s%7s\n", gb.getHsn(), " ",gb.getGst(),
                        df.format(gb.getTaxNetAmr()), df.format(gb.getCgst()), df.format(gb.getSgst()));
                mService.sendMessage(value, "");
            }
            mService.sendMessage(dash, "");


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

    private List<GstBreakup> gstBreakupValues(){
        List<String> hsnList = new ArrayList<>();
        HashMap<String, GstBreakup> hsnValue = new HashMap<>();
        for (int i = 0; i < mBillItems.size(); i++){
            BillItems bi = mBillItems.get(i);
            String hsnGst = bi.getHsn() + (bi.getTaxRate1() + bi.getTaxRate2());
            if (hsnList.contains(hsnGst)){
                GstBreakup gb = hsnValue.get(hsnGst);
                gb.setCgst(gb.getCgst() + bi.getTaxAmt1());
                gb.setSgst(gb.getSgst() + bi.getTaxAmt2());
                gb.setTaxNetAmr(gb.getTaxNetAmr() + bi.getTaxSaleAmt());
                hsnValue.put(hsnGst,gb);
            } else {
                hsnList.add(hsnGst);
                GstBreakup gb = new GstBreakup();
                gb.setHsn(bi.getHsn());
                gb.setGst(String.valueOf(bi.getTaxRate1() + bi.getTaxRate2()));
                gb.setCgst(bi.getTaxAmt1());
                gb.setSgst(bi.getTaxAmt2());
                gb.setTaxNetAmr(bi.getTaxSaleAmt());
                hsnValue.put(hsnGst,gb);
            }
        }
        List<GstBreakup> list = new ArrayList<>();
        for (int i = 0; i< hsnList.size(); i++){
            GstBreakup gb = hsnValue.get(hsnList.get(i));
            list.add(gb);
        }
        return list;
    }

    private String[] getCompanyHeaders() {
        GetSettings getSettings = new GetSettings(mContext);
        String cityPin = getSettings.getCompanyCity() + "-" + getSettings.getCompanyPincode();
        String phNumber, gst;
        if (getSettings.getCompanyPhoneNo().isEmpty()){
            phNumber = "";
        } else {
            phNumber = "Ph:" + getSettings.getCompanyPhoneNo();
        }
        if (getSettings.getCompanyGstNo().isEmpty()){
            gst = "";
        } else {
            gst = "GSTIN:" + getSettings.getCompanyGstNo();
        }
        return new String[]{getSettings.getCompanyAddressLine1(),
                getSettings.getCompanyAddressLine2(), getSettings.getCompanyAddressLine3(),
                cityPin, phNumber, gst};
    }
}