package in.net.maitri.xb.registration;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import in.net.maitri.xb.MainActivity;
import in.net.maitri.xb.R;
import in.net.maitri.xb.login.LoginActivity;


public class OtpVerification extends Dialog {

    private Context mContext;
    static EditText mOtpField;
    private String mMobNo, mOtp;
    private static final String URL = "http://smsc2.jaamoon.com/MsgBroadcast?wsdl";

     public OtpVerification(Context mContext, String mMobNo) {
        super(mContext);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.mMobNo = mMobNo;
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.otplayout);

        mOtp = String.valueOf(getOtp(100000, 999999));
        System.out.println(mOtp);
        String[] data = {mMobNo, mOtp};
        new ConnectSMSGateway().execute(data);
        final ImageView mClose = (ImageView) findViewById(R.id.close);
        mOtpField = (EditText) findViewById(R.id.otpText);
        Button mVerify = (Button) findViewById(R.id.verify);
        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOtpField.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, mOtp.toString(),Toast.LENGTH_SHORT).show();
                   // Toast.makeText(mContext, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else if (mOtpField.getText().toString().equals(mOtp)) {
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("KEY_IS_LOGIN", true);
                    editor.apply();
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    dismiss();
                    ((Activity) mContext).finish();
                } else {
                    Toast.makeText(mContext, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final TextView mResend = (TextView) findViewById(R.id.resend);
        mResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] data = {mMobNo, mOtp};
                new ConnectSMSGateway().execute(data);
                mResend.setVisibility(View.GONE);
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private static int getOtp(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }

    private class ConnectSMSGateway extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... data) {
            String METHOD_NAME = "Msgbroadcast";
            String NAMESPACE = "http://webservice.smsc.com/";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            PropertyInfo pi = new PropertyInfo();
            pi.setName("username");
            pi.setValue("maitri");
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("password");
            pi.setValue("mispl");
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("senderid");
            pi.setValue("MISPLB");
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("message");
            pi.setValue("OTP for XPand verification is " + data[1] + ".");
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("mobilenos");
            pi.setValue(data[0]);
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("isMsgTimeSensitive");
            pi.setValue("true");
            pi.setType(Boolean.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("validatenos");
            pi.setValue("true");
            pi.setType(Boolean.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("messagetype");
            pi.setValue("text");
            pi.setType(String.class);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                String SOAP_ACTION = "http://webservice.smsc.com/Msgbroadcast";
                androidHttpTransport.call(SOAP_ACTION, envelope);
                Object result = envelope.getResponse();
                Log.d("WS", String.valueOf(result));

                System.out.println("Result :" + result.toString());
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
