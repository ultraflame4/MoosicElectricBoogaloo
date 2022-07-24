package com.ultraflame42.moosicelectricboogaloo.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.account.LoginStatus;

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
            AccountManager.setAuthStatus(LoginStatus.NOT_LOGGED_IN);
        });


        return view;
    }
}