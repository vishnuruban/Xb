package in.net.maitri.xb.db;


import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BackUpAndRestoreDb {

    private Context mContext;

    public BackUpAndRestoreDb(Context context){
        mContext = context;
    }

    public void importDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File myDir = new File(sd + "/Xb/Backup");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "in.net.maitri"
                        + "//databases//" + "DbHandler";
                String backupDBPath = "/Xb/Backup"; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(mContext, "Import Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(mContext, "Import Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    public void exportDB() {
        String DB_PATH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DB_PATH = mContext.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
        } else {
            DB_PATH = mContext.getFilesDir().getPath() + mContext.getPackageName() + "/databases/";
        }
        try {
            File sd = Environment.getExternalStorageDirectory();
            File myDir = new File(sd + "/Xb/Backup");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "XposeBilling";
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String backupDBPath = "Backup" + timeStamp + ".db";
                File currentDB = new File(DB_PATH, currentDBPath);
                File backupDB = new File(myDir, backupDBPath);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(mContext, "Backup Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(mContext, "Backup Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }
}
