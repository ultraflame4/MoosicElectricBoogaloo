package com.ultraflame42.moosicelectricboogaloo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ultraflame42.moosicelectricboogaloo.login.LoginManager;
import com.ultraflame42.moosicelectricboogaloo.login.LoginStatus;
import com.ultraflame42.moosicelectricboogaloo.ui.login.LoginHomeActivity;


public class MainActivity extends AppCompatActivity {

    void sendUserToLogin() {
        if (LoginManager.getStatus() == LoginStatus.NOT_LOGGED_IN) {
            Intent intent = new Intent(this, LoginHomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sendUserToLogin();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NavFragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.NavMenu);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


    }


}