package in.net.maitri.xb.user;

import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.User;

public class AddNewUser extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_add_new_user, container, false);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final EditText userNameField = (EditText) view.findViewById(R.id.user_name);
        final EditText passwordField = (EditText) view.findViewById(R.id.password);
        final EditText confirmPasswordField = (EditText) view.findViewById(R.id.confirm_password);
        final Switch isAdmin = (Switch) view.findViewById(R.id.isAdmin);
        Button save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameValue = userNameField.getText().toString();
                String passwordValue = passwordField.getText().toString();
                String confirmPasswordValue = confirmPasswordField.getText().toString();
                if (userNameValue.isEmpty() || passwordValue.isEmpty() || confirmPasswordValue.isEmpty()){
                    Toast.makeText(getActivity(), "Enter all the fields.", Toast.LENGTH_SHORT).show();
                } else if (userNameValue.length()< 4){
                    Toast.makeText(getActivity(), "Username is too short.", Toast.LENGTH_SHORT).show();
                } else if (passwordValue.length()< 4){
                    Toast.makeText(getActivity(), "Password is too short.", Toast.LENGTH_SHORT).show();
                } else if (!passwordValue.equals(confirmPasswordValue)){
                    Toast.makeText(getActivity(), "Password and confirm password are not matching.", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User();
                    user.setUserName(userNameValue);
                    user.setPassword(passwordValue);
                    if (isAdmin.isChecked()){
                        user.setIsAdmin(1);
                    } else {
                        user.setIsAdmin(0);
                    }
                    if (doLogin(user)){
                        Toast.makeText(getActivity(), "User created successfully.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Failed to add user. Try again.", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            }
        });
        return view;
    }

    private boolean doLogin(User user){
        return new DbHandler(getActivity()).addUser(user);
    }
}
