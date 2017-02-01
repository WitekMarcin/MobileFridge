package com.marcin.mobilefridge.services;

import com.marcin.mobilefridge.model.Recipe;

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

    public void sendNewRecipe(Recipe recipe) throws Exception {
        restConnectionManagerService.sendNewRecipeToServer(oAuthKey, username, recipe);
    }

    public ArrayList<Recipe> getRecipes() throws Exception {
        return restConnectionManagerService.getAllRecipes(oAuthKey);
    }
}
