package com.ultraflame42.moosicelectricboogaloo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.data.DataManager;


public class AppHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_app_home);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NavFragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.NavMenu);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // register temp playable songs todo remove
        SongRegistry.registerSong(
                new Song("abcdefu","GAYLE",DataManager.getPlayable("https://www.youtube.com/watch?v=NaFd8ucHLuo"))
        );


    }

    public void onBackPressed() {
        mAuth.signOut(); //todo, QOL for debugging, remove in release
        AccountManager.AppHomeExitEvent.pushEvent(null);
        finish();
    }

    public void handleGoogleSignIn() {

    }


}