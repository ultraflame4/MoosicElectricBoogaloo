package com.ultraflame42.moosicelectricboogaloo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;


public class AppHomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);

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


}