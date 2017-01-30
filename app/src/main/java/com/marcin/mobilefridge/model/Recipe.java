package com.marcin.mobilefridge.model;

/**
 * Created by Marcin on 23.01.2017.
 */
public class Recipe {

    private String title;

    private String description;

    private Object picture;

    private String componentsOfRecipe;

    public Recipe(String title, String description, Object picture, String componentsOfRecipe) {
        this.description = description;
        this.picture = picture;
        this.title = title;
        this.componentsOfRecipe = componentsOfRecipe;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Object getPicture() {
        return picture;
    }

    public String getComponentsOfRecipe() {
        return componentsOfRecipe;
    }

    @Override
    public String toString() {
        return "{ \"title\" :" + "\"" + getTitle() + "\"," + "\"description\" :" + "\"" + getDescription() + "\","
                + "\"picture\" :" + "\"" + getPicture() + "\"," +
                "\"componentsOfRecipe\" :" + "\"" + getComponentsOfRecipe() + "\"}";
    }

}
