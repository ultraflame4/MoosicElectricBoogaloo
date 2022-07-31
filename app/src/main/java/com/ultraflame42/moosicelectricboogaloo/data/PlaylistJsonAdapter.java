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
        JsonObject result = new JsonObject();
        result.add("creator", new JsonPrimitive(src.getCreator()));
        result.add("title", new JsonPrimitive(src.getTitle()));
        JsonArray songs = new JsonArray();
        for (int songId : src.getSongs()) {
            songs.add(songId);
        }
        result.add("songs", songs);
        return result;
    }

    @Override
    public SongPlaylist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonArray songsJson = obj.getAsJsonArray("songs");
        List<Integer> songs = new ArrayList<>();
        for (JsonElement jsonElement : songsJson) {
            songs.add(jsonElement.getAsInt());
        }

        return new SongPlaylist(
                obj.getAsJsonPrimitive("creator").getAsString(),
                obj.getAsJsonPrimitive("title").getAsString(),
                songs.toArray(new Integer[0])
        );
    }

}
