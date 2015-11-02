package com.yalantis.model;

import com.google.gson.annotations.SerializedName;

public class GithubRepo {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String IS_FORK = "fork";

    private long id;

    @SerializedName(NAME)
    private String name;
    @SerializedName(DESCRIPTION)
    private String description;
    @SerializedName(IS_FORK)
    private Boolean isFork;

}