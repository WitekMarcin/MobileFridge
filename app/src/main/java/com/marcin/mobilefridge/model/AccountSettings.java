package com.marcin.mobilefridge.model;

import android.graphics.Bitmap;

/**
 * Created by Marcin on 04.02.2017.
 */
public class AccountSettings {

    private String firstName;
    private String secondName;
    private Integer age;
    private String img;
    private Bitmap imageBitmap;

    public String getFirstName() {
        if (firstName.equals("null"))
            return "";
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        if (secondName.equals("null"))
            return "";
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        String settings = "{ \"firstName\" :" + "\"" + getFirstName() + "\"," + "\"lastName\" :" + "\"" + getSecondName() + "\","
                + "\"age\" :" + "\"" + getAge() + "\"," +
                "\"imageUrl\" :" + "\"" + getImg() + "\"}";
        settings = settings.replace("\n", "\\n").replace("\r", "\\r");
        return settings;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
