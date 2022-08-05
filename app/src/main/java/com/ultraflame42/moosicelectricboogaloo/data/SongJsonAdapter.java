package com.ultraflame42.moosicelectricboogaloo.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;

import java.lang.reflect.Type;

public class SongJsonAdapter implements JsonSerializer<Song>, JsonDeserializer<Song> {
    @Override
    public JsonElement serialize(Song src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("title", new JsonPrimitive(src.getTitle()));
        result.add("artist", new JsonPrimitive(src.getArtist()));
        result.add("filelink", new JsonPrimitive(src.getFileLink()));
        result.add("imageUriLink", new JsonPrimitive(src.getImageUriLink()));
        result.add("playable", new JsonPrimitive(src.isPlayable()));
        result.add("length", new JsonPrimitive(src.getLength()));

        return result;
    }

    @Override
    public Song deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Song song = new Song(
                obj.getAsJsonPrimitive("title").getAsString(),
                obj.getAsJsonPrimitive("artist").getAsString(),
                obj.getAsJsonPrimitive("filelink").getAsString(),
                obj.getAsJsonPrimitive("imageUriLink").getAsString());
        song.setRuntimeInfo(
                obj.getAsJsonPrimitive("playable").getAsBoolean(),
                obj.getAsJsonPrimitive("length").getAsInt()
        );
        return song;
    }
}
