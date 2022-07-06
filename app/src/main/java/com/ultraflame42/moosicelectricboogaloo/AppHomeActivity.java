package com.ultraflame42.moosicelectricboogaloo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;


public class AppHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_app);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NavFragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.NavMenu);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


    }

    public void onBackPressed() {
        finish();
        AccountManager.OnExitAppHome.pushEvent();
    }

    public void handleGoogleSignIn() {

    }


}