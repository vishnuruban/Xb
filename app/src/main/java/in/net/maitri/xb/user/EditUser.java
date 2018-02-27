package in.net.maitri.xb.user;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.User;


public class EditUser extends DialogFragment {

    private User mSelectedUser;
    private LinearLayout layout, layout1;
    private Button update;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_user, container, false);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        layout = (LinearLayout) view.findViewById(R.id.isAdmin_layout);
        layout1 = (LinearLayout) view.findViewById(R.id.user_name_layout);
        final Spinner userDropDown = (Spinner) view.findViewById(R.id.user_name);
        final Switch isAdmin = (Switch) view.findViewById(R.id.isAdmin);
        final EditText newUserName = (EditText) view.findViewById(R.id.new_user_name);
        update = (Button) view.findViewById(R.id.update);
        List<String> getAllUsers = new DbHandler(getActivity()).getAllUsers();
        final ArrayList<String> userAdapter = new ArrayList<>();
        userAdapter.add("--Select User--");
        for (int i = 0; i < getAllUsers.size(); i++) {
            userAdapter.add(getAllUsers.get(i));
        }
        userDropDown.setAdapter(createAdapter(userAdapter));
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getNewUserName = newUserName.getText().toString();
                if (!getNewUserName.isEmpty()) {
                    mSelectedUser.setUserName(getNewUserName);
                }
                if (isAdmin.isChecked()) {
                    mSelectedUser.setIsAdmin(1);
                } else {
                    mSelectedUser.setIsAdmin(0);
                }

                if (new DbHandler(getActivity()).updateUser(mSelectedUser)) {
                    Toast.makeText(getActivity(), "User updated successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String currentUserName = sharedPreferences.getString("current_user","");
                    if(!currentUserName.equals(getNewUserName)){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("current_user", getNewUserName);
                        editor.putBoolean("user_is_admin", new DbHandler(getActivity()).isAdmin(getNewUserName));
                        editor.apply();
                    }
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Failed to update user.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
        });
        userDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedUser = adapterView.getItemAtPosition(i).toString();
                if (!selectedUser.equals("--Select User--")) {
                    mSelectedUser = new DbHandler(getActivity()).getUser(selectedUser);

                    if (mSelectedUser.getUserName().equals("admin")) {
                        Toast.makeText(getActivity(), "You can't edit admin user.", Toast.LENGTH_SHORT).show();
                    } else if (mSelectedUser.getIsAdmin() == 1) {

                        layout.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.VISIBLE);
                        isAdmin.setChecked(true);
                        update.setEnabled(true);
                    } else {

                        layout.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.VISIBLE);
                        isAdmin.setChecked(false);
                        update.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    private ArrayAdapter<String> createAdapter(ArrayList<String> list) {
        return new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_dropdown_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
    }
}
