package com.ultraflame42.moosicelectricboogaloo.tools;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UsefulStuff {
    /**
     * This method exists solely to make my life easier.
     * <p>
     * Removes title bar from activity.
     *
     * @param activity
     */
    public static void setupActivity(AppCompatActivity activity) {
        // Remove title bar
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Hide the SupportAction bar
        activity.getSupportActionBar().hide();
    }

    /**
     * This method exists solely to make my life easier.
     * <p>
     * Makes dialog window background transparent. and titleless.
     * <p>
     * (So i can have rounded corners)
     *
     * @param fragment
     */
    public static void setupDialogFragment(DialogFragment fragment) {
        // Disable the window title
        fragment.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set dialog bg to transparent
        fragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    /**
     * Returns DisplayMetrics of the device.
     * @param activity
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(AppCompatActivity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPx(Context context, float dp) {
        // Converts dp to px
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static String formatMilliseconds(Integer time) {
        // Format ms into minutes and seconds
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        return minutes + " : " + seconds;
    }

    /**
     * Converts epoch ms into date string
     *
     * @param millisecondsSinceEpoch
     * @return
     */
    public static String formatEpochToDate(long millisecondsSinceEpoch) {
        // format epoch ms to date format
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(millisecondsSinceEpoch));
    }

    public static void setMediaPlayerDataSource(MediaPlayer mediaPlayer, String source, Context context) throws IOException {
        mediaPlayer.setDataSource(context, Uri.parse(source));

    }

    /**
     * Get file name from a valid uri.
     * <p>
     * modified from : https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content/25005243#25005243
     *
     * @param ctx context
     * @param uri uri of the file
     * @return
     */
    public static String getFileName(Context ctx, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = ctx.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int value = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    result = cursor.getString(value);
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * Using picaasso , loads an image from a uri string (local files, urls) into an image view
     *
     * @param uriLink   uri of the image in string
     * @param imageView image view to load the image into
     */
    public static void LoadImageUriIntoImageView(String uriLink, android.widget.ImageView imageView) {
        if (uriLink.isEmpty()) {
            uriLink = "EMPTY"; // if the uri is empty, set to fake link so that picasso errors out and uses the placeholder/error image
        }
        Picasso.get().load(uriLink).error(R.drawable.color_a_bg).into(imageView);
    }

    /**
     * Returns a list of all search name. This includes songs and playlists
     *
     * @return
     */
    public static List<SearchNameItem> getAllSearchNames() {
        // Get a copy of all song search names from song registry
        List<SearchNameItem> list = SongRegistry.getInstance().getSearchNames();
        // add all song search names from playlist registry to list
        list.addAll(PlaylistRegistry.getInstance().getSearchNames());
        return list;
    }
}
