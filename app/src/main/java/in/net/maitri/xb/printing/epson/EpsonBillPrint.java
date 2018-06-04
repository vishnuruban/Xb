package in.net.maitri.xb.printing.epson;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.billing.FragmentOne;
import in.net.maitri.xb.settings.GetSettings;

public class EpsonBillPrint {

    private GetSettings getSettings;
    private Context mContext;
    private ArrayList<BillItems> mBillItems;
    private double mNetAmount, mTotalQty;
    private String mBillNo, mTotalPrice, mTotalDiscount, mBillDate, mCashierName, mCustomerName;
    private Printer mPrinter;
    private DecimalFormat df = new DecimalFormat("0.00");

    public EpsonBillPrint(Context mContext) {
        this.mContext = mContext;
    }

    public EpsonBillPrint(Context mContext, ArrayList<BillItems> mBillItems,
                          double mNetAmount, String mBillNo, String mTotalPrice, String mTotalDiscount,
                          double mTotalQty, String mBillDate, String mCashierName, String mCustomerName) {
        this.mContext = mContext;
        this.mBillItems = mBillItems;
        this.mNetAmount = mNetAmount;
        this.mTotalQty = mTotalQty;
        this.mBillNo = mBillNo;
        this.mTotalPrice = mTotalPrice;
        this.mTotalDiscount = mTotalDiscount;
        this.mBillDate = mBillDate;
        this.mCashierName = mCashierName;
        this.mCustomerName = mCustomerName;
    }

    private boolean epsonThreeInch() {
        NumberFormat nf = new DecimalFormat("##.##");
        StringBuilder textData = new StringBuilder();
        try {
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addTextSize(3, 2);
            mPrinter.addText(getCompanyName() + "\n");

            for (int i = 0; i < getCompanyHeaders().length; i++) {
                textData.append(getCompanyHeaders()[i]).append("\n");
            }
            mPrinter.addTextSize(1, 1);
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addTextSize(2, 1);
            mPrinter.addText("CASH BILL" + "\n");
            textData.append(String.format("%-27s%20s\n", "  Bill No : " + mBillNo, mBillDate));
            textData.append("  ----------------------------------------------\n");
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            mPrinter.addTextSize(1, 1);
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addTextSize(1, 2);
            String s = String.format("%-17s%6s%11s%13s\n", "  Item           ", "Qty", "Price", "Amount");
            mPrinter.addText(s + "\n");
            mPrinter.addTextSize(1, 1);
            textData.append("  ----------------------------------------------\n");
            for (int i = 0; i < mBillItems.size(); i++) {
                BillItems billItems = mBillItems.get(i);
//                if (billItems.getDesc().length() > 14){
                textData.append("  ").append(billItems.getDesc()).append("\n");
                s = String.format("%-17s%6s%11s%13s\n", "", billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
                /*}else {
                    s = String.format("%-17s%6s%11s%13s\n", "  " + billItems.getDesc(), billItems.getQty(), df.format(billItems.getRate()), df.format(billItems.getAmount()));
                }*/
                textData.append(s);
            }
            textData.append("  ----------------------------------------------\n");
            textData.append(String.format("%-15s%8s%10s%14s\n", "  Items : " + mBillItems.size(), "", "Subtotal", mTotalPrice));
            if (mTotalDiscount.equals("0.00")) {
                textData.append(String.format("%-15s%8s%10s%14s\n", "  Qty   : " + nf.format(mTotalQty), "", "", ""));
            } else {
                textData.append(String.format("%-15s%8s%10s%14s\n", "  Qty   : " + nf.format(mTotalQty), "", "Discount", mTotalDiscount));
            }
            textData.append("  ----------------------------------------------\n");
            mPrinter.addTextSize(1, 1);
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            textData.append(String.format("%-15s%8s%9s%15s\n", "  Total", "", "", "Rs." + FragmentOne.commaSeperated(mNetAmount)));
            mPrinter.addTextSize(1, 2);
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            textData.append("  ----------------------------------------------\n");
            mPrinter.addTextSize(1, 1);
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            String[] footerArray = {getSettings.getFooterText1(), getSettings.getFooterText2(),
                    getSettings.getFooterText3(), getSettings.getFooterText4()};
            for (String aFooterArray : footerArray) {
                if (!aFooterArray.isEmpty()) {
                    textData.append(aFooterArray).append("\n");
                }
            }
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addTextSize(1, 1);
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addFeedLine(2);

            mPrinter.addCut(Printer.CUT_FEED);
        } catch (Epos2Exception e) {
            Toast.makeText(mContext, "Printer connection failed", Toast.LENGTH_SHORT).show();
            ShowMsg.showException(e, "", mContext);
            return false;
        }

        return true;
    }

    public boolean runPrintReceiptSequence() {
       /* if (!initializeObject()) {
            return false;
        }*/

        if (!epsonThreeInch()) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();

//        dispPrinterWarnings(status);

        if (!isPrintable(status)) {
//            ShowMsg.showMsg(makeErrorMessage(status), mContext);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }



    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();
        mPrinter.setReceiveEventListener(null);
        mPrinter = null;
    }

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            Log.d("Usb Path", getSettings.getUsb());
            mPrinter.connect("USB:/dev/bus/usb/001/002", Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "connect", mContext);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            ShowMsg.showException(e, "beginTransaction", mContext);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
                return false;
            }
        }

        return true;
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        } else if (status.getOnline() == Printer.FALSE) {
            return false;
        } else {
            ;//print available
        }

        return true;
    }

    private String getCompanyName() {
        getSettings = new GetSettings(mContext);
        return getSettings.getCompanyTradeName();
    }

    private String[] getCompanyHeaders() {
        String cityPin = getSettings.getCompanyCity() + "-" + getSettings.getCompanyPincode();
        String phNumber = "Ph:" + getSettings.getCompanyPhoneNo();
        return new String[]{getSettings.getCompanyAddressLine1(),
                getSettings.getCompanyAddressLine2(), getSettings.getCompanyAddressLine3(),
                cityPin, phNumber, getSettings.getCompanyGstNo()};
    }
}
