package com.marcin.mobilefridge.services;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Marcin on 16.12.2016.
 */
public class FridgeService {

    private final String oAuthKey;
    private String username;
    RestConnectionManagerService restConnectionManagerService;

    public FridgeService(String oAuthKey, String username) {
        this.username = username;
        this.oAuthKey = oAuthKey;
        restConnectionManagerService = new RestConnectionManagerService();
    }

    public ArrayList<HashMap<String, String>> getProducts() throws Exception {

        try {
            return restConnectionManagerService.getProductsFromServer(oAuthKey, username);
        } catch (Exception e) {
            //TODO show message
            throw new Exception();
        }
    }

}
