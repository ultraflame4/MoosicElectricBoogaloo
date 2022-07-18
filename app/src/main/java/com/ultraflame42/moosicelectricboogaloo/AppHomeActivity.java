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
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;


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
        SongRegistry.songs.add(
                new Song("abcdefu", "GAYLE", "https://p.scdn.co/mp3-preview/83c53804d9a84bee1cca941679370f0541dd4ca2?cid=2afe87a64b0042dabf51f37318616965")
        );
        SongRegistry.songs.add(
                new Song("abcdefuA", "GAYLEA", "https://p.scdn.co/mp3-preview/83c53804d9a84bee1cca941679370f0541dd4ca2?cid=2afe87a64b0042dabf51f37318616965")
        );
        SongRegistry.songs.add(
                new Song("abcdefuB", "GAYLEB", "https://p.scdn.co/mp3-preview/83c53804d9a84bee1cca941679370f0541dd4ca2?cid=2afe87a64b0042dabf51f37318616965")
        );
        // temp values for testing todo remove ltr
        SongRegistry.playlists.add(
                new SongPlaylist("Debug 1", "Test 1", new Integer[]{0, 1,2})
        );


        SongPlayer.init();


    }

    public void onBackPressed() {
        mAuth.signOut(); //todo, QOL for debugging, remove in release
        AccountManager.AppHomeExitEvent.pushEvent(null);
        finish();
    }

    public void handleGoogleSignIn() {

    }


}