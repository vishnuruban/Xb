package in.net.maitri.xb.itemdetails;


import android.content.Context;
import android.util.DisplayMetrics;

class CalculateNoOfColumnsAccScreenSize {
    static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }
}
