package com.marcin.mobilefridge.services;

import android.util.Base64;
import com.marcin.mobilefridge.model.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Marcin on 11.12.2016.
 */
class RestConnectionManagerService {

    private static final String GET_PRODUCTS_PATH = "http://192.168.0.241:8080/api/get_products/user_id/";
    private static String EXAMPLE_URL = "http://192.168.0.241:8080/api/get_all_fridges";
    private Logger logger = Logger.getLogger(RestConnectionManagerService.class.getName());

    String tryToLogInAndReturnOauthKey(String username, String password) throws Exception {
        String oAuthKeyValue;
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(EXAMPLE_URL);
            oAuthKeyValue = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + oAuthKeyValue);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setConnectTimeout(5000);
            logger.info("trying TO LOG IN");
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200)
                throw new IOException();
            logger.info("log in succesfully wit code " + responseCode);

        } catch (SocketTimeoutException e) {
            logger.info("could not connect to server");
            throw new SocketTimeoutException("Nie mogę połączyć się z serwerem, sprawdź swoje połączenie z internetem");
        } catch (IOException e) {
            logger.info("bad credentials");
            throw new IOException("Nieprawidłowy login bądź hasło");
        } catch (Exception e) {
            logger.info("unknown Excpetion " + e.getCause());
            throw new SocketTimeoutException("Wystąpił nieoczekiwany błąd, spróbuj ponownie");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return oAuthKeyValue;
    }

    ArrayList<Product> getProductsFromServer(String oAuthKeyValue, String username) throws Exception {

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(GET_PRODUCTS_PATH + username);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + oAuthKeyValue);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setConnectTimeout(5000);
            logger.info("trying TO LOG IN");
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) {
                logger.info("problem with connection with code " + responseCode);
                throw new IOException();
            }
            logger.info("connection success");
            return convertToArrayListOfProducts(new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
        } catch (SocketTimeoutException e) {
            logger.info("could not connect to server");
            throw new SocketTimeoutException("Nie mogę połączyć się z serwerem, sprawdź swoje połączenie z internetem");
        } catch (IOException e) {
            logger.info("bad credentials");
            throw new IOException("Nieprawidłowy login bądź hasło");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Wystąpił nieoczekiwany błąd, spróbuj ponownie");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private ArrayList<Product> convertToArrayListOfProducts(BufferedReader responseMessage) throws JSONException {
        ArrayList<Product> productList = new ArrayList<>();
        String jsonList = convertToString(responseMessage);
        if (responseMessage != null) {
            logger.info("CO TU SIE DZIEJE LOLZ" + jsonList);
            JSONArray jsonArray = new JSONArray(jsonList.
                    substring(jsonList.indexOf("["), jsonList.indexOf("]") + 1));
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject product = jsonArray.getJSONObject(i);
                Product productObj = new Product();
                productObj.setId(Long.valueOf(product.getString("id")));
                productObj.setName(product.getString("name"));
                productObj.setWeight(product.getString("weight"));
                productObj.setWeightUnit(product.getString("weightUnit"));
                productObj.setAddingTime(product.getString("addingTime"));
                productObj.setIconSmall(product.getString("iconSmall"));
                productObj.setIconBig(product.getString("iconBig"));
                productList.add(productObj);
            }
        }
        return productList;
    }

    private String convertToString(BufferedReader responseMessage) {
        String aux;
        StringBuilder builder = new StringBuilder();
        try {
            while ((aux = responseMessage.readLine()) != null) {
                builder.append(aux);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
