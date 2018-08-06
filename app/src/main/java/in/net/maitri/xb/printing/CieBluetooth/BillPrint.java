package in.net.maitri.xb.printing.CieBluetooth;

import android.content.Context;
import android.widget.Toast;
import com.cie.btp.BtpConsts;
import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.PrinterWidth;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.billing.FragmentOne;
import in.net.maitri.xb.settings.GetSettings;

public class BillPrint {

    private GetSettings getSettings;
    private DecimalFormat df;
    private Context context;

    public BillPrint(Context context) {
        this.context = context;
    }

    public void printThreeInch(CieBluetoothPrinter mPrinter, ArrayList<BillItems> bItems,
                               float netAmount, String BillNo, String tPrice, String tDiscount,
                               float tQty, String formattedDate, String cashierName, String customerName) {

        df = new DecimalFormat("0.00");
        NumberFormat nf = new DecimalFormat("##.##");
        float quantity = 0;
        mPrinter.setPrintMode(BtpConsts.PRINT_IN_BATCH);
        mPrinter.resetPrinter();
        mPrinter.setPrinterWidth(PrinterWidth.PRINT_WIDTH_72MM);
        mPrinter.setHighIntensity();
        getSettings = new GetSettings(context);
        if (headerPrint(context, mPrinter)) {
            mPrinter.setAlignmentCenter();
            mPrinter.setBold();
            mPrinter.printTextLine("CASH BILL\n");
            mPrinter.setRegular();
            String s = String.format("%-15s%8s%12s%12s\n", "Item", "Qty", "Price", "Amount");
            String s2 = String.format("%-15s%8s%12s%12s\n", "Aachi Masala 1K", "2kg", "4500.00", "9000.00");
            String total = String.format("%-15s%8s%9s%15s\n", "Total", "", "", "Rs." + FragmentOne.commaSeperated(netAmount));
            mPrinter.setAlignmentLeft();
            String billNo = String.format("%-10s%-8s%9s%15s\n", "Bill No", BillNo, "", formattedDate);
            mPrinter.printTextLine(billNo);
            if (!customerName.isEmpty())
                mPrinter.printTextLine(" Customer Name  : " + customerName + "\n");
            if (!cashierName.isEmpty())
                mPrinter.printTextLine(" Cashier Name   : " + cashierName + "\n");
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
            if (tDiscount.equals("0.00")) {
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
            mPrinter.batchPrint();
        }
    }

    public boolean headerPrint(Context context, CieBluetoothPrinter mPrinter) {
        String city = getSettings.getCompanyCity();
        String pincode = getSettings.getCompanyPincode();
        String cityPin = city + "-" + pincode;
        String phNumber = "Ph:" + getSettings.getCompanyPhoneNo();
        String[] billHeader = {getSettings.getCompanyAddressLine1(),
                getSettings.getCompanyAddressLine2(), getSettings.getCompanyAddressLine3(),
                cityPin, phNumber, getSettings.getCompanyGstNo()};
        String clName = getSettings.getCompanyLegalName();
        String ctName = getSettings.getCompanyTradeName();
        if (ctName.isEmpty()) {
            Toast.makeText(context, "Company name not present in settings", Toast.LENGTH_SHORT).show();
            return false;
        }
        mPrinter.setAlignmentCenter();
        mPrinter.setBold();
        mPrinter.printTextLine("\n" + ctName + "\n");
        mPrinter.setRegular();
        for (String aBillHeader : billHeader) {
            if (!aBillHeader.isEmpty()) {
                mPrinter.printTextLine(aBillHeader + "\n");
            }
        }
        return true;
    }




    public void printTwoInch(CieBluetoothPrinter mPrinter, ArrayList<BillItems> bItems, float netAmount, String BillNo, String tPrice, String tDiscount, float tQty, String formattedDate, String cashierName, String customerName) {
        NumberFormat nf = new DecimalFormat("##.##");
        df = new DecimalFormat("0.00");
        float quantity = 0;
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
        mPrinter.printTextLine("\n           " + ctName + "\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.setRegular();
        if (!addLine1.isEmpty())
            mPrinter.printTextLine("            " + addLine1 + "\n");
        if (!addLine2.isEmpty())
            mPrinter.printTextLine("            " + addLine2 + "\n");
        if (!addLine3.isEmpty())
            mPrinter.printTextLine("            " + addLine3 + "\n");
        if (!cityPin.isEmpty())
            mPrinter.printTextLine("            " + cityPin + "\n");
        if (!phNum.isEmpty())
            mPrinter.printTextLine("            " + "Ph:" + phNum + "\n");
        mPrinter.printLineFeed();
        mPrinter.setBold();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            CASH BILL\n");
        mPrinter.printLineFeed();
        mPrinter.setRegular();
        mPrinter.setFontSizeXSmall();
        mPrinter.setAlignmentLeft();
        mPrinter.printTextLine(billNo);
        if (!customerName.isEmpty()) {
            mPrinter.printTextLine("            Customer Name  : " + customerName + "\n");
        }
        if (!cashierName.isEmpty()) {
            mPrinter.printTextLine("            Cashier Name   : " + cashierName + "\n");
        }
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.setBold();
        mPrinter.printTextLine(s);
        mPrinter.setRegular();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        for (int i = 0; i < bItems.size(); i++) {
            BillItems billItems = bItems.get(i);
            quantity = quantity + billItems.getQty();
            String sa1 = String.format("%12s%-34s\n", "", billItems.getDesc());
            String sa2 = String.format("%12s%-10s%5s%9s%10s\n", "", "", billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
            mPrinter.printTextLine(sa1);
            mPrinter.printLineFeed();
            mPrinter.printTextLine(sa2);
        }

        String discount = "";
        String qtyNetAmt = String.format("%12s%-10s%5s%9s%10s\n", "", "Items : " + bItems.size(), "", "Subtotal", tPrice);
        System.out.println("cDiscountValue " + tDiscount);
        if (tDiscount.equals("0.00")) {
            discount = String.format("%12s%-10s%5s%9s%10s\n", "", "Qty   : " + nf.format(tQty), "", "", "");
        } else {
            discount = String.format("%12s%-10s%5s%9s%10s\n", "", "Qty   : " + nf.format(tQty), "", "Discount", tDiscount);
        }
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        mPrinter.setFontSizeXSmall();
        mPrinter.printTextLine(qtyNetAmt);
        mPrinter.printTextLine(discount);
        mPrinter.printLineFeed();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
        mPrinter.setBold();
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine(total);
        mPrinter.setFontSizeSmall();
        mPrinter.printTextLine("            ------------------------------------\n");
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
        mPrinter.printLineFeed();
        mPrinter.printLineFeed();
        mPrinter.resetPrinter();
        mPrinter.batchPrint();
    }

}
