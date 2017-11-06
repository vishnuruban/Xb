package in.net.maitri.xb.db;

import android.database.sqlite.SQLiteOpenHelper;

import com.clough.android.androiddbviewer.ADBVApplication;

/**
 * Created by SYSRAJ4 on 06/11/2017.
 */

public class CustomApplication extends ADBVApplication {
    @Override
    public SQLiteOpenHelper getDataBase() {
        return new DbHandler(getApplicationContext());
    }
}
