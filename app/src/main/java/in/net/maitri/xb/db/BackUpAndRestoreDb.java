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

    public boolean importDB(File file) {
        String DB_PATH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DB_PATH = mContext.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
        } else {
            DB_PATH = mContext.getFilesDir().getPath() + mContext.getPackageName() + "/databases/";
        }
        File sd = Environment.getExternalStorageDirectory();
    /*    File myDir = new File(sd + "/Xb/Backup");
        String backupDBPath = "Backup20171205_113033.db";
        File currDB = new File(myDir, backupDBPath);*/
        try {
            if (sd.canWrite()) {
                String restoreDBPath = "XposeBilling";
                File restoreDB = new File(DB_PATH, restoreDBPath);
                if (restoreDB.delete()) {
                    FileChannel src = new FileInputStream(file).getChannel();
                    FileChannel dst = new FileOutputStream(restoreDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(mContext, "Restore Successful!",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "Restore Failed!", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return false;
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