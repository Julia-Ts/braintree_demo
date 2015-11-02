package com.yalantis.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Repository extends RealmObject {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String HOMEPAGE = "homepage";
    public static final String IS_FORK = "fork";
    public static final String FORKS_COUNT = "forks";
    public static final String STARS_COUNT = "stargazers_count";
    public static final String WATCHERS_COUNT = "watchers";

    @PrimaryKey
    @SerializedName(ID)
    private long id;

    @SerializedName(NAME)
    private String name;
    @SerializedName(DESCRIPTION)
    private String description;
    @SerializedName(HOMEPAGE)
    private String homepage;
    @SerializedName(IS_FORK)
    private Boolean isFork;
    @SerializedName(FORKS_COUNT)
    private int forksCount;
    @SerializedName(WATCHERS_COUNT)
    private int watchersCount;
    @SerializedName(STARS_COUNT)
    private int starsCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Boolean getIsFork() {
        return isFork;
    }

    public void setIsFork(Boolean isFork) {
        this.isFork = isFork;
    }

    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public int getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(int starsCount) {
        this.starsCount = starsCount;
    }

}