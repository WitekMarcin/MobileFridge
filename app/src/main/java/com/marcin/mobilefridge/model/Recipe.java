package com.marcin.mobilefridge.model;

import android.graphics.Bitmap;

/**
 * Created by Marcin on 23.01.2017.
 */
public class Recipe {

    private Long id;

    private String title;

    private String description;

    private String picture;

    private String componentsOfRecipe;

    private Integer rating;

    private Bitmap image;

    public Recipe() {

    }

    public Recipe(String title, String description, String picture, String componentsOfRecipe) {
        this.description = description;
        this.picture = picture;
        this.title = title;
        this.componentsOfRecipe = componentsOfRecipe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setComponentsOfRecipe(String componentsOfRecipe) {
        this.componentsOfRecipe = componentsOfRecipe;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getComponentsOfRecipe() {
        return componentsOfRecipe;
    }

    @Override
    public String toString() {
        String recipe = "{ \"title\" :" + "\"" + getTitle() + "\"," + "\"description\" :" + "\"" + getDescription() + "\","
                + "\"picture\" :" + "\"" + getPicture() + "\"," +
                "\"componentsOfRecipe\" :" + "\"" + getComponentsOfRecipe() + "\"}";
        return recipe;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
