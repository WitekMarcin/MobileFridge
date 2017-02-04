package com.marcin.mobilefridge.services;

import com.marcin.mobilefridge.model.Recipe;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Marcin on 30.01.2017.
 */
public class RecipeService {

    private final String username;
    private final String oAuthKey;
    private RestConnectionManagerService restConnectionManagerService;

    public RecipeService(String oAuthKey, String username) {
        this.username = username;
        this.oAuthKey = oAuthKey;
        restConnectionManagerService = new RestConnectionManagerService();
    }

    public Long sendNewRecipe(Recipe recipe) throws Exception {
        return restConnectionManagerService.sendNewRecipeToServer(oAuthKey, username, recipe);
    }

    public ArrayList<Recipe> getRecipes() throws Exception {
        return restConnectionManagerService.getAllRecipes(oAuthKey);
    }

    public void updateRecipePicture(Recipe recipe, String pathToPicture, String id) throws IOException {
        recipe.setPicture(pathToPicture);
        restConnectionManagerService.updateRecipePicture(oAuthKey, recipe, id);
    }

    public void sendMarkToRecipe(Integer mark, Long id) throws IOException {
        restConnectionManagerService.sendMarkForRecipe(oAuthKey, mark, id);
    }
}
