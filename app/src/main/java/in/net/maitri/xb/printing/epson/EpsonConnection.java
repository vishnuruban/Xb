package in.net.maitri.xb.printing.epson;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;

import in.net.maitri.xb.settings.GetSettings;

public class EpsonConnection extends Application {


    private static Printer mPrinter;
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        EpsonConnection.mContext = getApplicationContext();
        try {
            mPrinter = new Printer(Printer.TM_T81, Printer.MODEL_SOUTHASIA, EpsonConnection.mContext);
        } catch (Epos2Exception e) {
            Toast.makeText(EpsonConnection.mContext, "Printer connection failed.", Toast.LENGTH_SHORT).show();
        }
    }
    public static Printer initializeEpson() {
        return EpsonConnection.mPrinter;
    }


}
