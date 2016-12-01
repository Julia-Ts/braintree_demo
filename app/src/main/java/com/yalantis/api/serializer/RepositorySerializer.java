package com.yalantis.api.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.yalantis.data.Repository;

import java.lang.reflect.Type;

/**
 * Created by irinagalata on 12/1/16.
 */

public class RepositorySerializer implements JsonSerializer<Repository> {

    @Override
    public JsonElement serialize(Repository src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(Repository.ID, src.getId());
        jsonObject.addProperty(Repository.NAME, src.getName());
        jsonObject.addProperty(Repository.DESCRIPTION, src.getDescription());
        jsonObject.addProperty(Repository.HOMEPAGE, src.getHomepage());
        jsonObject.addProperty(Repository.IS_FORK, src.getIsFork());
        jsonObject.addProperty(Repository.FORKS_COUNT, src.getForksCount());
        jsonObject.addProperty(Repository.STARS_COUNT, src.getStarsCount());
        jsonObject.addProperty(Repository.WATCHERS_COUNT, src.getWatchersCount());

        return jsonObject;
    }

}