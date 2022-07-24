package com.ultraflame42.moosicelectricboogaloo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventListenerGroup;


public class AppHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EventListenerGroup listenerGroup = new EventListenerGroup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_app_home);


        listenerGroup.subscribe(AccountManager.LoggedOutEvent,data -> {
            finish();
        });

        // Set up bottom navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NavFragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.NavMenu);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Get registries
        SongRegistry songRegistry = SongRegistry.getInstance();
        PlaylistRegistry playlistRegistry = PlaylistRegistry.getInstance();
        // Toast any errors from songRegistry
        listenerGroup.subscribe(songRegistry.OnRegistryWarningsUI, warning -> {
            Toast.makeText(this, warning, Toast.LENGTH_LONG).show();
        });

        // register temp playable songs todo remove
        songRegistry.add(
                new Song("abcdefu", "GAYLE", "https://p.scdn.co/mp3-preview/83c53804d9a84bee1cca941679370f0541dd4ca2?cid=2afe87a64b0042dabf51f37318616965")
        );
        songRegistry.add(
                // song with absurdly long title
                new Song("abcdefuA----------------------------------------------------------d-", "GAYLEA", "https://p.scdn.co/mp3-preview/83c53804d9a84bee1cca941679370f0541dd4ca2?cid=2afe87a64b0042dabf51f37318616965")
        );
        songRegistry.add(
                // song with broken link
                new Song("abcdefuB", "GAYLEB", "https://p.scdn.co/mp3-preview/83dc53804d9a84bee1cca941679370f0541dd4ca2?cid=2afe87a64b0042dabf51f37318616965")
        );
        // temp values for testing todo remove ltr
        playlistRegistry.add(
                new SongPlaylist("Debug 1", "Test 1", new Integer[]{0, 1,2})
        );


        SongPlayer.init();
        listenerGroup.subscribe(SongPlayer.OnSongPlayError,data -> {
            Toast.makeText(this, "SongPlayer: "+data, Toast.LENGTH_SHORT).show();
        });

    }

    public void onBackPressed() {
        AccountManager.AppHomeExitEvent.pushEvent(null);
    }

    public void handleGoogleSignIn() {

    }


}