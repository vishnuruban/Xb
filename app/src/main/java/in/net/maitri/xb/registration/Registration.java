package in.net.maitri.xb.registration;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.net.maitri.xb.MainActivity;
import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.util.ConnectionDetector;
import in.net.maitri.xb.util.NoInternetConnDialog;


public class Registration extends AppCompatActivity {

    private String mMobNo, IMEI = "", mVerificationCode;
    private ConnectionDetector mConnectionDetector = new ConnectionDetector(Registration.this);
    private NoInternetConnDialog mNoInternetConnDialog0 = new NoInternetConnDialog(Registration.this, 0);
    private NoInternetConnDialog mNoInternetConnDialog1 = new NoInternetConnDialog(Registration.this, 1);
    private EditText mMobTextField;
    private ProgressDialog mDialog;
    private static final int MY_PERMISSIONS_PHONE_STATE = 0,
            MY_PERMISSIONS_READ_SMS = 1,
            MY_PERMISSIONS_RECEIVE_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                IMEI = getIMEI();
            } else {
                phoneStatePermission();
            }
        } else {
            IMEI = getIMEI();
        }


        mMobTextField = (EditText) findViewById(R.id.mob);
        ImageButton mLoginButton = (ImageButton) findViewById(R.id.signin);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        if (IMEI.isEmpty()){
            mVerificationCode = randomString(16);
        } else{
            mVerificationCode = IMEI;
        }
        mMobNo = mMobTextField.getText().toString();
        if (TextUtils.isEmpty(mMobNo)) {
            mMobTextField.setError("Enter mobile no.");
        } else if (mMobNo.length() != 10) {
            mMobTextField.setError("Please enter 10 digit mobile no.");
        } else if (!mConnectionDetector.isOnline()) {
            mNoInternetConnDialog0.createDialog();
        } else {
            mDialog = new ProgressDialog(Registration.this);
            mDialog.setMessage("Validating...");
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(false);
            mDialog.show();
            Log.d("code", mVerificationCode);
            postProject();
        }
    }


    private void postProject() {
        String PROJECT_URL = "http://103.228.249.46:8082/XpandLogin/GetLoginStatus";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROJECT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Json", response);
                        try {
                            JSONArray mJSONArray =  new JSONArray(response);
                            JSONObject mJsonObj = (JSONObject) mJSONArray.get(0);
                            String isActive = mJsonObj.getString("cancel_regd");
                            String lockDate =  mJsonObj.getString("lock_date");
                            String status = mJsonObj.getString("status");
                            String message = mJsonObj.getString("message");
                            int lockDateCount = new DbHandler(Registration.this).getDateCount(lockDate);
                            int currentDateCount =  new DbHandler(Registration.this).getDateCount(getCurrentDate());
                            if (status.equals("true")) {
                                if (isActive.equals("1") || (currentDateCount >= lockDateCount)) {
                                    mDialog.cancel();
                                    createErrorDialog("Registration Error", "Your application license has expired. Contact Maitri.");
                                } else {
                                    prepareData(status, message);
                                }
                            } else {
                                prepareData(status, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            prepareData("Error", "Json error");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.cancel();
                        if (mConnectionDetector.isOnline()) {
                            mDialog.cancel();
                            mNoInternetConnDialog1.createDialog();
                        } else {
                            mDialog.cancel();
                            mNoInternetConnDialog0.createDialog();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                HashMap<String, String> headers = new HashMap<>();
                headers.put("mobileNumber", mMobNo);
                headers.put("verificationCode", mVerificationCode);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void prepareData(String status, String msg) {

        switch (status) {
            case "true":
                mDialog.cancel();
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(Registration.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("KEY_VERIFICATION_CODE", mVerificationCode);
                editor.putString("KEY_MOBILE_NO", mMobNo);
                editor.apply();
                Toast.makeText(Registration.this, msg, Toast.LENGTH_SHORT).show();
                smsReadPermission();
                new OtpVerification(Registration.this, mMobNo).show();
                break;
            case "false":
                mDialog.cancel();
                mMobTextField.setText("");
                AlertDialog b = new AlertDialog.Builder(Registration.this).create();
                b.setTitle("Login Error");
                b.setMessage("Mobile no " + mMobNo + " is not registered with XPand.");
                b.setButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
                b.show();
                break;
            case "Error":
                mDialog.cancel();
                Toast.makeText(Registration.this, "json error", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void smsReadPermission(){
        ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.READ_SMS);
        if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Registration.this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_READ_SMS);
        }
    }


    private void phoneStatePermission() {
        ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Registration.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_PHONE_STATE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_READ_SMS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    IMEI = getIMEI();
                } else {
                    createPermissionDialog("Application need sms permission to read otp.", true);
                }
                break;

            case MY_PERMISSIONS_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    IMEI = getIMEI();
                } else {
                    createPermissionDialog("Application need phone state permission to check your registration validity.", false);
                }
                break;

        }
    }

    private void createPermissionDialog(String msg, final boolean isSMS) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Registration.this);
        builder.setTitle("Permission Required")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       if (isSMS){
                           smsReadPermission();
                       } else {
                           phoneStatePermission();
                       }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void createErrorDialog(String title, String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Registration.this);
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }


    String randomString( int len ){
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}