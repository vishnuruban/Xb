package in.net.maitri.xb.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;



public class NoInternetConnDialog {

    private Context _context;
    private int no;
    private String[] errorMsg = {"No internet connection. Please check your network connectivity.",
            "Unable to connect host server. Please try later."};

    public NoInternetConnDialog(Context context, int no) {
        this._context = context;
        this.no = no;
    }

    public void createDialog() {

        android.support.v7.app.AlertDialog.Builder b = new android.support.v7.app.AlertDialog.Builder(_context);
        b.setMessage(errorMsg[no]);
        b.setCancelable(false);
        b.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((Activity)_context).finish();
                        dialog.dismiss();
                    }
                }
        );
        b.show();

    }
}
