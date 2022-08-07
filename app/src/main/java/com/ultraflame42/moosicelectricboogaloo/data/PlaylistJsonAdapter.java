package com.ultraflame42.moosicelectricboogaloo.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistJsonAdapter implements JsonSerializer<SongPlaylist>, JsonDeserializer<SongPlaylist> {
    @Override
    public JsonElement serialize(SongPlaylist src, Type typeOfSrc, JsonSerializationContext context) {
        // Create new json object
        JsonObject result = new JsonObject();
        // serialsie the creator and title attributes and add to json object
        result.add("creator", new JsonPrimitive(src.getCreator()));
        result.add("title", new JsonPrimitive(src.getTitle()));

        // serialise the songs array in the playlist
        JsonArray songs = new JsonArray();
        for (int songId : src.getSongs()) {
            songs.add(songId);
        }
        // add to the json object
        result.add("songs", songs);

        // Return final json object result
        return result;
    }

    @Override
    public SongPlaylist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Convert json to to json object
        JsonObject obj = json.getAsJsonObject();
        // Get the song array
        JsonArray songsJson = obj.getAsJsonArray("songs");
        // De code it into an arraylist
        List<Integer> songs = new ArrayList<>();
        for (JsonElement jsonElement : songsJson) {
            songs.add(jsonElement.getAsInt());
        }

        // create new SongPlaylsit object with the json data
        return new SongPlaylist(
                // get the serialised creator name
                obj.getAsJsonPrimitive("creator").getAsString(),
                // get the serialised title
                obj.getAsJsonPrimitive("title").getAsString(),
                songs.toArray(new Integer[0])
        );
    }

}
