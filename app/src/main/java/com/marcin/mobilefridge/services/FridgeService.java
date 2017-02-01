package com.marcin.mobilefridge.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.marcin.mobilefridge.model.Product;

import java.io.InputStream;
import java.util.ArrayList;

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

    public ArrayList<Product> getProducts() throws Exception {

        try {
            return restConnectionManagerService.getProductsFromServer(oAuthKey, username);
        } catch (Exception e) {
            //TODO show message
            throw new Exception();
        }
    }

    public void getProductsImages(ArrayList<Product> productList) {

        for (Product product : productList) {

            String imageURL = product.getIconSmall();

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            product.setSmallIconBitmap(bitmap);
        }
    }

}
