package com.marcin.mobilefridge.model;

/**
 * Created by Marcin on 23.01.2017.
 */
public class Recipe {

    private Long id;

    private String title;

    private String description;

    private Object picture;

    private String componentsOfRecipe;

    private Integer rating;

    public Recipe() {

    }

    public Recipe(String title, String description, Object picture, String componentsOfRecipe) {
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

    public void setPicture(Object picture) {
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
