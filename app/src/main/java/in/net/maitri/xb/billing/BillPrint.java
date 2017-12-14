package in.net.maitri.xb.billing;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.cie.btp.BtpConsts;
import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.PrinterWidth;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



import in.net.maitri.xb.settings.GetSettings;

/**
 * Created by SYSRAJ4 on 14/12/2017.
 */

public class BillPrint {


        GetSettings getSettings;
        DecimalFormat df;
    Context context;



    public BillPrint (Context context)
    {
        this.context = context;
    }




    public void printThreeInch(CieBluetoothPrinter mPrinter,ArrayList<BillItems> bItems, double netAmount, String BillNo, String tPrice, String tDiscount, double tQty,String formattedDate) {

       // SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh.mm a");
      //  String formattedDate = dateFormat.format(new Date()).toString();
        df = new DecimalFormat("0.00");
        NumberFormat nf = new DecimalFormat("##.##");
        double quantity = 0;
        mPrinter.setPrintMode(BtpConsts.PRINT_IN_BATCH);
        mPrinter.resetPrinter();
        mPrinter.setPrinterWidth(PrinterWidth.PRINT_WIDTH_72MM);
        mPrinter.setHighIntensity();
        getSettings = new GetSettings(context);
        if(headerPrint(context,mPrinter)) {
            mPrinter.setAlignmentCenter();
            mPrinter.setBold();
            mPrinter.printTextLine("CASH BILL\n");
            mPrinter.setRegular();
            // String custName = "test customer";
            // String cashName = "test cashier";
            String s = String.format("%-15s%8s%12s%12s\n", "Item", "Qty", "Price", "Amount");
            String s2 = String.format("%-15s%8s%12s%12s\n", "Aachi Masala 1K", "2kg", "4500.00", "9000.00");
            String total = String.format("%-15s%8s%9s%15s\n", "Total", "", "", "Rs." + FragmentOne.commaSeperated(netAmount));
            mPrinter.setAlignmentLeft();
            String billNo = String.format("%-10s%-8s%9s%15s\n", "Bill No", BillNo, "", formattedDate);
            mPrinter.printTextLine(billNo);
            // mPrinter.printTextLine(" Customer Name  : " + custName + "\n");
            //mPrinter.printTextLine(" Cashier Name   : " + cashName + "\n");
            mPrinter.printTextLine("------------------------------------------------\n");
            mPrinter.setBold();
            mPrinter.printTextLine(s);
            mPrinter.printTextLine("------------------------------------------------\n");
            mPrinter.setRegular();

            for (int i = 0; i < bItems.size(); i++) {
                BillItems billItems = bItems.get(i);
                quantity = quantity + billItems.getQty();
                if (billItems.getDesc().length() <= 15) {
                    String sa = String.format("%-15s%8s%12s%12s\n", billItems.getDesc(), billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
                    mPrinter.printTextLine(sa);
                } else {
                    String sa1 = String.format("%-40s%3s\n", billItems.getDesc(), "");
                    String sa2 = String.format("%-15s%8s%12s%12s\n", "", billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
                    mPrinter.printTextLine(sa1);
                    mPrinter.printTextLine(sa2);
                }
            }
            String discount = "";
            String qtyNetAmt = String.format("%-15s%8s%10s%14s\n", "Items : " + bItems.size(), "", "Subtotal", tPrice);
            if (tDiscount.equals("")) {
                discount = String.format("%-15s%8s%10s%14s\n", "Qty   : " + nf.format(tQty), "", "", "");
            } else {
                discount = String.format("%-15s%8s%10s%14s\n", "Qty   : " + nf.format(tQty), "", "Discount", tDiscount);
            }
            mPrinter.printTextLine("------------------------------------------------\n");
            mPrinter.printTextLine(qtyNetAmt);
            mPrinter.printTextLine(discount);
            mPrinter.printLineFeed();
            mPrinter.printTextLine("------------------------------------------------\n");
            mPrinter.setBold();
            mPrinter.printTextLine(total);
            mPrinter.printTextLine("------------------------------------------------\n");
            mPrinter.printLineFeed();
            mPrinter.setAlignmentCenter();
            mPrinter.setBold();

            String[] footerArray = {getSettings.getFooterText1(), getSettings.getFooterText2(),
                    getSettings.getFooterText3(), getSettings.getFooterText4()};
            for (String aFooterArray : footerArray) {
                if (!aFooterArray.isEmpty()) {
                    mPrinter.printTextLine(aFooterArray + "\n");
                    mPrinter.printLineFeed();
                }
            }
            mPrinter.setRegular();
            mPrinter.printTextLine("************************************************\n");
            mPrinter.printLineFeed();
            mPrinter.printLineFeed();
            mPrinter.setAlignmentCenter();
            mPrinter.printLineFeed();
            mPrinter.printLineFeed();
            mPrinter.resetPrinter();
            //print all commands
            mPrinter.batchPrint();
        }
    }


    public boolean headerPrint(Context context,CieBluetoothPrinter mPrinter) {
        String city = getSettings.getCompanyCity();
        String pincode = getSettings.getCompanyPincode();
        String cityPin = city + "-" + pincode;
        String phNumber = "Ph:" + getSettings.getCompanyPhoneNo();
        String[] billHeader = {getSettings.getCompanyAddressLine1(),
                getSettings.getCompanyAddressLine2(), getSettings.getCompanyAddressLine3(),
                cityPin, phNumber, getSettings.getCompanyGstNo() };
        String clName = getSettings.getCompanyLegalName();
        String ctName = getSettings.getCompanyTradeName();
        if (ctName.isEmpty()) {
            Toast.makeText(context, "Company name not present in settings", Toast.LENGTH_SHORT).show();
            return false;
        }
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        // mPrinter.setFontSizeXSmall();
        //   mPrinter.printTextLine("\n"+clName+"\n");
        mPrinter.printTextLine("\n" + ctName + "\n");
        mPrinter.setRegular();
        for ( String aBillHeader:billHeader){
            if (!aBillHeader.isEmpty()){
                mPrinter.printTextLine(aBillHeader + "\n");
            }
        }
        return true;
    }




    public void printTwoInch(CieBluetoothPrinter mPrinter,ArrayList<BillItems> bItems, double netAmount, String BillNo, String tPrice, String tDiscount, double tQty,String formattedDate) {
       // SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh.mm a");
       // String formattedDate = dateFormat.format(new Date()).toString();
        Log.i("Net Amt" ,String.valueOf(netAmount));
        NumberFormat nf = new DecimalFormat("##.##");
        df = new DecimalFormat("0.00");
        double quantity = 0;
        getSettings = new GetSettings(context);
        String clName = getSettings.getCompanyLegalName();
        String ctName = getSettings.getCompanyTradeName();
        String addLine1 = getSettings.getCompanyAddressLine1();
        String addLine2 = getSettings.getCompanyAddressLine2();
        String addLine3 = getSettings.getCompanyAddressLine3();
        String city = getSettings.getCompanyCity();
        String pincode = getSettings.getCompanyPincode();
        String cityPin = city + "-" + pincode;
        String phNum = getSettings.getCompanyPhoneNo();
        String gstin = getSettings.getCompanyGstNo();
        //String custName = "test customer";
        //String cashName = "test cashier";
        if (ctName.isEmpty()) {
            Toast.makeText(context, "Company name not present in settings", Toast.LENGTH_SHORT).show();
            return;
        }
        String s = String.format("%12s%-10s%5s%9s%10s\n", "", "Item", "Qty", "Rate", "Amount");
        String total = String.format("%12s%-10s%5s%9s%10s\n", "", "Total", "", "", "Rs." + FragmentOne.commaSeperated(netAmount));
        String billNo = String.format("%12s%-7s%-5s%4s%15s\n", "", "Bill No:", BillNo, "", formattedDate);
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        mPrinter.setFontSizeSmall();
        //   mPrinter.printTextLine("\n"+clName+"\n");
        mPrinter.printTextLine("\n           " + ctName + "\n");
        System.out.println("\n           " + ctName + "\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.setRegular();
        if (!addLine1.isEmpty())
            mPrinter.printTextLine("            " + addLine1 + "\n");
        System.out.println("            " + addLine1 + "\n");
        if (!addLine2.isEmpty())
            mPrinter.printTextLine("            " + addLine2 + "\n");
        System.out.println("            " + addLine2 + "\n");
        if (!addLine3.isEmpty())
            mPrinter.printTextLine("            " + addLine3 + "\n");
        System.out.println();
        if (!cityPin.isEmpty())
            mPrinter.printTextLine("            " + cityPin + "\n");
        System.out.println("            " + cityPin + "\n");
        if (!phNum.isEmpty())
            mPrinter.printTextLine("            " + "Ph:" + phNum + "\n");
        System.out.println("            " + "Ph:" + phNum + "\n");
        mPrinter.printLineFeed();
        mPrinter.setBold();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            CASH BILL\n");
        System.out.println("            CASH BILL\n");
        mPrinter.printLineFeed();
        mPrinter.setRegular();
        mPrinter.setFontSizeXSmall();
        mPrinter.setAlignmentLeft();
        mPrinter.printTextLine(billNo);
        System.out.println(billNo);
        //mPrinter.printTextLine("            Customer Name  : " + custName + "\n");
        // mPrinter.printTextLine("            Cashier Name   : " + cashName + "\n");
      //  mPrinter.printLineFeed();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        System.out.println("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.setBold();
        mPrinter.printTextLine(s);
        System.out.println(s);
        mPrinter.setRegular();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        System.out.println("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        for (int i = 0; i < bItems.size(); i++) {
            BillItems billItems = bItems.get(i);
            quantity = quantity + billItems.getQty();
            //  if (billItems.getDesc().length() <= 10) {
            //     String sa = String.format("12s%-13s%3s%9s%10s\n", "",billItems.getDesc(), billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
            //      mPrinter.printTextLine(sa);
            //   } else {
            String sa1 = String.format("%12s%-34s\n", "", billItems.getDesc());
            String sa2 = String.format("%12s%-10s%5s%9s%10s\n", "", "", billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
            mPrinter.printTextLine(sa1);
            System.out.println(sa1);
            mPrinter.printLineFeed();
            mPrinter.printTextLine(sa2);
            System.out.println(sa2);
            // }
        }

        String discount = "";
        String qtyNetAmt = String.format("%12s%-10s%5s%9s%10s\n", "", "Items : " + bItems.size(), "", "Subtotal", tPrice);
        System.out.println("cDiscountValue " + tDiscount);
        if (tDiscount.isEmpty()) {
            discount = String.format("%12s%-10s%5s%9s%10s\n", "", "Qty   : " + nf.format(tQty), "", "", "");
        } else {
            discount = String.format("%12s%-10s%5s%9s%10s\n", "", "Qty   : " + nf.format(tQty), "", "Discount", tDiscount);
        }
        // if(!gstin.isEmpty())
        //mPrinter.printTextLine(gstin+"\n");
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        System.out.println("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.printTextLine(qtyNetAmt);
        System.out.println(qtyNetAmt);
        mPrinter.printTextLine(discount);
        System.out.println(discount);
        mPrinter.printLineFeed();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        System.out.println("            ------------------------------------\n");
        mPrinter.setBold();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine(total);
        System.out.println(total);
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        System.out.println("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.printLineFeed();
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();

        String[] footerArray = {getSettings.getFooterText1(), getSettings.getFooterText2(),
                getSettings.getFooterText3(), getSettings.getFooterText4()};
        for (String aFooterArray : footerArray) {
            if (!aFooterArray.isEmpty()) {
                mPrinter.printTextLine("            " + aFooterArray + "\n");
                mPrinter.printLineFeed();
            }
        }
        mPrinter.setRegular();
        mPrinter.printTextLine("            ************************************\n");
        mPrinter.printLineFeed();
        mPrinter.printLineFeed();
        mPrinter.setAlignmentCenter();
        //Clearance for Paper tear
        mPrinter.printLineFeed();
        mPrinter.printLineFeed();
        mPrinter.resetPrinter();
        //print all commands
        mPrinter.batchPrint();
        System.out.println("RESULT "+mPrinter.toString());

    }

}
