package com.yalantis.model;

/**
 * Created by Alexander Zaitsev on 02.12.2014.
 */
public class ShareModelFacebook {

    public static String FB_NAME = "name";
    public static String FB_CAPTION = "caption";
    public static String FB_DESCRIPTION = "description";
    public static String FB_LINK = "link";
    public static String FB_PICTURE = "picture";

    private String name;
    private String caption;
    private String description;
    /**
     * This can be link to your site.
     * Attention! You must register a domen in developers.facebook.com before using it!
     */
    private String link;
    /**
     * Link to the picture in the web
     */
    private String picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
