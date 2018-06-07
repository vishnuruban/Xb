package in.net.maitri.xb.printing.epson;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;

import in.net.maitri.xb.settings.GetSettings;

public class EpsonConnection {

    private Context mContext;
    private Printer mPrinter;

    public EpsonConnection(Context mContext){
        this.mContext = mContext;
    }

    public  Printer initializeObject() {


        try {
            mPrinter = new Printer(Printer.TM_T81, Printer.MODEL_SOUTHASIA, mContext);
            connectPrinter();
        } catch (Epos2Exception e) {
            Toast.makeText(mContext, "Printer connection failed.", Toast.LENGTH_SHORT).show();
        }
        return mPrinter;

    }

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            Log.d("Usb Path", new GetSettings(mContext).getUsb());
            Toast.makeText(mContext, new GetSettings(mContext).getUsb(), Toast.LENGTH_SHORT).show();
            mPrinter.connect(new GetSettings(mContext).getUsb(), Printer.PARAM_DEFAULT);
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

        if (!isBeginTransaction) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
                return false;
            }
        }

        return true;
    }
}
