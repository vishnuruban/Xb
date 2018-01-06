package in.net.maitri.xb;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.login.LoginActivity;
import in.net.maitri.xb.registration.Registration;
import in.net.maitri.xb.util.ConnectionDetector;
import in.net.maitri.xb.util.NoInternetConnDialog;

public class MainActivity extends AppCompatActivity {

    private String mMobNo, mVerificationCode;
    private ConnectionDetector mConnectionDetector = new ConnectionDetector(MainActivity.this);
    private NoInternetConnDialog mNoInternetConnDialog0 = new NoInternetConnDialog(MainActivity.this, 0);
    private NoInternetConnDialog mNoInternetConnDialog1 = new NoInternetConnDialog(MainActivity.this, 1);
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfFirstTym();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sharedPreferences.getBoolean("KEY_IS_LOGIN", false)) {
            mMobNo = sharedPreferences.getString("KEY_MOBILE_NO", "");
            mVerificationCode = sharedPreferences.getString("KEY_VERIFICATION_CODE", "");
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Validating...");
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(false);
            mDialog.show();
            postProject();
        } else {
            startActivity(new Intent(MainActivity.this, Registration.class));
            finish();
        }
    }

    private void createErrorDialog(String title, String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
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


    private void checkIfFirstTym() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("isFirstTym", true)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTym", true);
            editor.apply();
        }
    }


    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
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
                            int lockDateCount = new DbHandler(MainActivity.this).getDateCount(lockDate);
                            int currentDateCount =  new DbHandler(MainActivity.this).getDateCount(getCurrentDate());
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
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case "false":
                mDialog.cancel();
                AlertDialog b = new AlertDialog.Builder(MainActivity.this).create();
                b.setTitle("Login Error");
                b.setMessage("Mobile no " + mMobNo + " is not registered with XPand.");
                b.setButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences sharedPreferences = PreferenceManager
                                        .getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("KEY_IS_LOGIN", false);
                                editor.apply();
                                startActivity(new Intent(MainActivity.this, Registration.class));
                                dialog.dismiss();
                            }
                        }
                );
                b.show();
                break;
            case "Error":
                mDialog.cancel();
                Toast.makeText(MainActivity.this, "json error", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}
