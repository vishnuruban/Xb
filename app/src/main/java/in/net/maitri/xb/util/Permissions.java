package in.net.maitri.xb.util;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class Permissions extends Activity {

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1,
            MY_PERMISSIONS_CAMERA = 2;
    private Context context;

    public Permissions(Context context) {
        this.context = context;
    }

    public void checkWriteExternalStoragePermission() {
        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }
    }

    public void checkCameraPermission() {
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE:
               /* if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
                }*/
                break;

            case MY_PERMISSIONS_CAMERA:

              /*  if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
                }*/
                break;

        }
    }
}