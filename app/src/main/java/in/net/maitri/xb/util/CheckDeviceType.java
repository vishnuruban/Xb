package in.net.maitri.xb.util;

import android.content.Context;
import android.content.res.Configuration;



public class CheckDeviceType {

    private Context mContext;

    public CheckDeviceType(Context context){
        mContext = context;
    }

    public boolean isTablet() {
        boolean xlarge = ((mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}