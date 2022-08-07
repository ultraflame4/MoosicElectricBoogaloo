package com.ultraflame42.moosicelectricboogaloo.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.account.LoginStatus;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class SettingsFragment extends Fragment {


    private TextView settingsAccountEmailText;
    private TextView settingsCreatedDateText;

    private Button settingsAccountChangePasswordBtn;
    private Button settingsAccountSignOutBtn;
    private Button settingsAccountDeleteBtn;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsAccountEmailText = view.findViewById(R.id.settingsAccountEmailText);
        settingsCreatedDateText = view.findViewById(R.id.settingsCreatedDateText);

        settingsAccountChangePasswordBtn = view.findViewById(R.id.settingsAccountChangePassword);
        settingsAccountSignOutBtn = view.findViewById(R.id.settingsAccountSignOutBtn);

        settingsAccountDeleteBtn = view.findViewById(R.id.settingsAccountDeleteBtn);

        settingsAccountSignOutBtn.setOnClickListener(view1 -> {
            AccountManager.SignOut();
        });

        settingsAccountDeleteBtn.setOnClickListener(view1 -> {
            // Check if logged in. if not logged in, cannot delete account (cuz no account) and warn user
            if (AccountManager.getAuthStatus()!=LoginStatus.LOGGED_IN){
                Toast.makeText(getContext(), "You must be logged in to able delete your account!", Toast.LENGTH_SHORT).show();
            }
            else{
                AccountManager.DeleteAccount();
            }
        });

        // set the accoutn info for user
        if (AccountManager.getAuthStatus() == LoginStatus.LOGGED_IN) {
            // if logged in, get email and created date
            settingsAccountEmailText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            settingsCreatedDateText.setText(
                    UsefulStuff.formatEpochToDate(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp())
            );

        } else if (AccountManager.getAuthStatus() == LoginStatus.GUEST) {
            settingsAccountEmailText.setText("N/A");
            settingsCreatedDateText.setText("N/A");
        }

        return view;
    }


}