package com.marcin.mobilefridge.model;

/**
 * Created by Marcin on 23.01.2017.
 */
public class Recipe {

    private String title;

    private String description;

    private Object picture;

    public Recipe(String title, String description, Object picture) {
        this.description = description;
        this.picture = picture;
        this.title = title;
    }

}
