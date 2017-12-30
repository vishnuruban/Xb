package in.net.maitri.xb.user;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;

public class ChangePassword extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.change_password, container, false);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        EditText userName = (EditText) view.findViewById(R.id.user_name);
        final EditText currentPassword = (EditText) view.findViewById(R.id.current_password);
        final EditText newPassword = (EditText) view.findViewById(R.id.new_password);
        final EditText confirmPassword = (EditText) view.findViewById(R.id.confirm_password);
        Button save = (Button) view.findViewById(R.id.save);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String getUserName = sharedPreferences.getString("current_user","");
        userName.setText(getUserName);
        userName.setEnabled(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pswd = new DbHandler(getActivity()).getPassword(getUserName);
                String getCurrentPassword = currentPassword.getText().toString();
                String getNewPassword = newPassword.getText().toString();
                String getConfirmPassword = confirmPassword.getText().toString();
                if (getCurrentPassword.isEmpty() || getNewPassword.isEmpty() || getConfirmPassword.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter all details.", Toast.LENGTH_SHORT).show();
                } else if (isPasswordValid(getCurrentPassword) || isPasswordValid(getNewPassword) || isPasswordValid(getConfirmPassword)){
                    Toast.makeText(getActivity(), "Password can't be less than 4 digit.", Toast.LENGTH_SHORT).show();
                } else if (!getCurrentPassword.equals(pswd)){
                    Toast.makeText(getActivity(), "Invalid current password.", Toast.LENGTH_SHORT).show();
                } else if (!getNewPassword.equals(getConfirmPassword)){
                    Toast.makeText(getActivity(), "New password and confirm password not matching.", Toast.LENGTH_SHORT).show();
                } else {
                    if (new DbHandler(getActivity()).changePassword(getUserName, getNewPassword)){
                        Toast.makeText(getActivity(), "Password changed successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Password changed failed.", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            }
        });

        return view;
    }


    private boolean isPasswordValid(String password) {
        return password.length() <= 4;
    }

}
