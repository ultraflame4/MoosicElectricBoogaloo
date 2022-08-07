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
import com.ultraflame42.moosicelectricboogaloo.data.Storage;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventListenerGroup;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class AppHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EventListenerGroup listenerGroup = new EventListenerGroup();
    private SongRegistry songRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);

        // Load data from shared preferences --------------------------------
        Storage.LoadedData loadedData = Storage.getInstance().Load(this);

        // Get registries
        SongRegistry.LoadFromData(loadedData, this);
        songRegistry = SongRegistry.getInstance();


        PlaylistRegistry.LoadFromData(loadedData);
        PlaylistRegistry playlistRegistry = PlaylistRegistry.getInstance();
        // ------------------------------------------------------------------

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_app_home);


        listenerGroup.subscribe(AccountManager.LoggedOutEvent, data -> {
            finish();
        });

        // Set up bottom navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NavFragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.NavMenu);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        // Toast any errors from songRegistry
        listenerGroup.subscribe(songRegistry.OnRegistryWarningsUI, warning -> {
            Toast.makeText(this, warning, Toast.LENGTH_LONG).show();
        });
        songRegistry.setHomeContext(this);

        if (songRegistry.getAllItems().length < 1) {
            // if song registry is empty, load a default songs
            songRegistry.add(
                    new Song(
                            "Singapore Examinations Listening Comprehension (2016 onwards)",
                            "SEAB",
                            "https://audio.jukehost.co.uk/LYmVQyUlmaUsyDExITZRefbZZRaFpkrM",
                            ""
                    )
            );
            songRegistry.add(
                    new Song(
                            "DADDY! DADDY! DO!",
                            "Masayuki Suzuki",
                            "https://audio.jukehost.co.uk/tzpYpTO7nqcwgzauuyfoeAKyHfchQtgb",
                            "https://iili.io/UcUCXf.png"
                    )
            );
            songRegistry.add(
                    new Song(
                            "my name is...",
                            "lullaboy",
                            "https://audio.jukehost.co.uk/34UKZDAZuq01TcyDlOkoSGm4Jx28aIj6",
                            "https://iili.io/UciV7p.th.png"
                    )
            );

            songRegistry.add(
                    new Song(
                            "GIRI GIRI",
                            "Masayuki Suzuki",
                            "https://audio.jukehost.co.uk/cLeZolv6EnPeT1rfo4Yk6PLryISB5TTJ",
                            "https://iili.io/Ucm8ej.md.png"
                    )
            );

            songRegistry.add(
                    new Song(
                            "Numb Little Bug",
                            "Emi Beihold",
                            "https://audio.jukehost.co.uk/mdNHjw8Mq6oHcgJ6Z0UBebyJoEwKSaLS",
                            "https://iili.io/Uldd5F.png"
                    )
            );

        }

        // Initiate song player
        SongPlayer.init(this);
        // Add listener for On song play error
        listenerGroup.subscribe(SongPlayer.OnSongPlayError, data -> {
            Toast.makeText(this, "SongPlayer: " + data, Toast.LENGTH_SHORT).show();
        });

        // using a scheduler ,..
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        // Save data to shared preferences every 5 seconds
        scheduler.scheduleAtFixedRate(() -> {
            // Save data
            Storage.getInstance().Save(this);

        }, 0, 5, java.util.concurrent.TimeUnit.SECONDS);

    }

    public void onBackPressed() {
        // When the user press the android back btn
        AccountManager.AppHomeExitEvent.pushEvent(null);
    }

    @Override
    protected void onDestroy() {
        // Stop memory leaks
        // unsub all listeners
        listenerGroup.unsubscribeAll();
        // remove home context
        songRegistry.removeHomeContext();
        super.onDestroy();
    }
}