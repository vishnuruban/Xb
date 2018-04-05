package in.net.maitri.xb.util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;

import in.net.maitri.xb.R;

public class GetUsbDevise extends AppCompatActivity {
    private final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    PendingIntent mPermissionIntent;
    UsbManager usbManager;
    UsbDevice device;
    UsbDevice printer = null;
    private static final int PRINTER_VENDOR_ID = 1008;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            Log.i("Info", "Activity started");
            usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            if (deviceList.size() <= 0) {
                Log.i("Info", "No device found");
            } else {
                Log.i("Info", "Number of device : " + deviceList.size());
                ((TextView) findViewById(R.id.deviceCount))
                        .setText("No of device : " + deviceList.size());
            }
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            int count = 0;
            mPermissionIntent = PendingIntent.getBroadcast(getBaseContext(), 0,
                    new Intent(ACTION_USB_PERMISSION), 0);
            while (deviceIterator.hasNext()) {
                count++;
                device = deviceIterator.next();
                Log.i("info", "Device No " + count + "........");
                Log.i("info", "Vendor id : " + device.getVendorId());
                Log.i("info", "Product id : " + device.getProductId());
                Log.i("info", "Device  name : " + device.getDeviceName());
                Log.i("info", "Device class : " + device.getClass().getName());
                Log.i("info", "Device protocol: " + device.getDeviceProtocol());
                Log.i("info", "Device subclass : " + device.getDeviceSubclass());
                if (device.getVendorId() == PRINTER_VENDOR_ID) {
                    printer = device;
                    break;
                }
            }

            findViewById(R.id.buttonPrint).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("Info", "Print command given");
                            IntentFilter filter = new IntentFilter(
                                    ACTION_USB_PERMISSION);
                            registerReceiver(mUsbReceiver, filter);
                            if (printer != null) {
                                usbManager.requestPermission(printer,
                                        mPermissionIntent);
                            } else {
                                Log.e("Exception", "Printer not found");
                            }
                        }
                    });

        } catch (Exception e) {
            Log.e("Exception", "Exception in onCreate " + e.getMessage());
            e.printStackTrace();
        }

    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        final UsbDevice printerDevice = (UsbDevice) intent
                                .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(
                                UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (printerDevice != null) {
                                Log.i("Info", "Device permission granted");
                                startPrinting(printerDevice);
                            }
                        } else {
                            Log.d("Debug", "permission denied for device "
                                    + printerDevice);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("Exception", "Exception in onRecieve " + e.getMessage());
                e.printStackTrace();
            }
        }

    };

    public void startPrinting(final UsbDevice printerDevice) {
        new Handler().post(new Runnable() {
            UsbDeviceConnection conn;
            UsbInterface usbInterface;

            @Override
            public void run() {
                try {
                    Log.i("Info", "Bulk transfer started");
                    usbInterface = printerDevice.getInterface(0);
                    UsbEndpoint endPoint = usbInterface.getEndpoint(0);
                    conn = usbManager.openDevice(printer);
                    conn.claimInterface(usbInterface, true);
                    String myStringData = "\nThis \nis \nmy \nsample \ntext";
                    byte[] array = myStringData.getBytes();
                    ByteBuffer output_buffer = ByteBuffer
                            .allocate(array.length);
                    UsbRequest request = new UsbRequest();
                    request.initialize(conn, endPoint);
                    request.queue(output_buffer, array.length);
                    if (conn.requestWait() == request) {
                        Log.i("Info", output_buffer.getChar(0) + "");
                        Message m = new Message();
                        m.obj = output_buffer.array();
                        // handler.sendMessage(m);
                        output_buffer.clear();
                    } else {
                        Log.i("Info", "No request recieved");
                    }
                    // int transfered = conn.bulkTransfer(endPoint,
                    // myStringData.getBytes(),
                    // myStringData.getBytes().length, 5000);
                    // Log.i("Info", "Amount of data transferred : " +
                    // transfered);

                } catch (Exception e) {
                    Log.e("Exception", "Unable to transfer bulk data");
                    e.printStackTrace();
                } finally {
                    try {
                        conn.releaseInterface(usbInterface);
                        Log.i("Info", "Interface released");
                        conn.close();
                        Log.i("Info", "Usb connection closed");
                        unregisterReceiver(mUsbReceiver);
                        Log.i("Info", "Brodcast reciever unregistered");
                    } catch (Exception e) {
                        Log.e("Exception",
                                "Unable to release resources because : "
                                        + e.getMessage());
                        e.printStackTrace();
                    }
                }

            }
        });
    }

}
