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
                Log.d("Success", "1");
                String currentDBPath = "XposeBilling";
                Log.d("Success", "2");
                String backupDBPath = "XposeBilling.db";
                Log.d("Success", "3");
                File currentDB = new File(DB_PATH, currentDBPath);
                Log.d("Success", currentDB.toString());
                File backupDB = new File(myDir, backupDBPath);
                Log.d("Success", backupDB.toString());
                FileChannel src = new FileInputStream(currentDB).getChannel();
                Log.d("Success", "6");
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                Log.d("Success", "7");
                dst.transferFrom(src, 0, src.size());
                Log.d("Success", "8");
                src.close();
                Log.d("Success", "9");
                dst.close();
                Log.d("Success", "10");
                Toast.makeText(mContext, "Backup Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(mContext, "Backup Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }
}
