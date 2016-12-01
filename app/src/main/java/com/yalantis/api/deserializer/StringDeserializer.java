package com.yalantis.api.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by voltazor on 27/06/16.
 */
public class StringDeserializer implements JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject() || json.isJsonArray()) {
            return json.toString();
        } else if (json.isJsonNull()) {
            return null;
        }
        return json.getAsString();
    }

}
