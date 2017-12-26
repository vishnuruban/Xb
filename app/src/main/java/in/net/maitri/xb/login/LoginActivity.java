package in.net.maitri.xb.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.itemdetails.AddItemCategory;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserNameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserNameView = (EditText) findViewById(R.id.user_name);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin() {

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String enteredPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(enteredPassword) && !isPasswordValid(enteredPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!isMobileNoValid(userName)) {
            mUserNameView.setError(getString(R.string.error_invalid_user_name));
            focusView = mUserNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(enteredPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            String pswd = new DbHandler(LoginActivity.this).getPassword(userName);
            if (pswd.isEmpty()){
                Toast.makeText(LoginActivity.this, "Invalid User.",Toast.LENGTH_SHORT).show();
            } else if (!pswd.equals(enteredPassword)){
                Toast.makeText(LoginActivity.this, "Invalid Password.",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Login Success.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, AddItemCategory.class));
            }
        }
    }

    private boolean isMobileNoValid(String mobNo) {
        return mobNo.length() >= 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

}

