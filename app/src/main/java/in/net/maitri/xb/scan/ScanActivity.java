package in.net.maitri.xb.scan;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MenuItem;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.billing.BillingActivity;
import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }

    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.playBeep();

        // ticket details activity by passing barcode
        Intent intent = new Intent(ScanActivity.this, BillingActivity.class);
        intent.putExtra("code", barcode.displayValue);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Intent intent = new Intent(ScanActivity.this, BillingActivity.class);
        intent.putExtra("code", "Unable to scan");
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onCameraPermissionDenied() {
        Intent intent = new Intent(ScanActivity.this, BillingActivity.class);
        intent.putExtra("code", "Camera permission denied");
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ScanActivity.this, BillingActivity.class);
        intent.putExtra("code", "");
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScanActivity.this, BillingActivity.class);
        intent.putExtra("code", "");
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
