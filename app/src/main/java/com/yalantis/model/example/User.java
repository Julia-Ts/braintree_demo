package com.yalantis.model.example;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user")
    private int userDataId; // use this for other models user id

    private String bio;

    private String advice;

    private String avatar;

    @SerializedName("avatar_s3")
    private String avatarObject;

    @SerializedName("facebook_url")
    private String facebookUrl;

    @SerializedName("twitter_url")
    private String twitterUrl;

    @SerializedName("pinterest_url")
    private String pinterestUrl;

    @SerializedName("linkedin_url")
    private String linkedinUrl;

    @SerializedName("website_url")
    private String websiteUrl;

    @SerializedName("unique_url")
    private String uniqueUrl;

    private String timezone;

    @SerializedName("use_kn_storage")
    private boolean useKnStorage;

    private String slug;

    @SerializedName("user_data")
    private UserData userDataObject;

    private String language;

    private String email;

    private String name;

    public int getUserDataId() {
        return userDataId;
    }

    public void setUserDataId(int userDataId) {
        this.userDataId = userDataId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarObject() {
        return avatarObject;
    }

    public void setAvatarObject(String avatarObject) {
        this.avatarObject = avatarObject;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getPinterestUrl() {
        return pinterestUrl;
    }

    public void setPinterestUrl(String pinterestUrl) {
        this.pinterestUrl = pinterestUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getUniqueUrl() {
        return uniqueUrl;
    }

    public void setUniqueUrl(String uniqueUrl) {
        this.uniqueUrl = uniqueUrl;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isUseKnStorage() {
        return useKnStorage;
    }

    public void setUseKnStorage(boolean useKnStorage) {
        this.useKnStorage = useKnStorage;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public UserData getUserDataObject() {
        return userDataObject;
    }

    public void setUserDataObject(UserData userDataObject) {
        this.userDataObject = userDataObject;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
