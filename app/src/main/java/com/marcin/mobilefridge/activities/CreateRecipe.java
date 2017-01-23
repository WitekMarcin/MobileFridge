package com.marcin.mobilefridge.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.marcin.mobilefridge.R;

public class CreateRecipe extends AppCompatActivity {

    private EditText titleOfRecipe;
    private EditText componentsOfRecipe;
    private EditText descriptionOfRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        titleOfRecipe = (EditText) findViewById(R.id.titleOfRecipe);
        componentsOfRecipe = (EditText) findViewById(R.id.componentsOfRecipe);
        descriptionOfRecipe = (EditText) findViewById(R.id.descriptionOfRecipe);
        //dodawanie zdj wez z atrakcji xd
    }

    public void addRecipe(View v) {
        Log.d("CRCLASS", "addRecipe: " + titleOfRecipe.getText());
        Log.d("CRCLASS", "addRecipe: " + componentsOfRecipe.getText());
        Log.d("CRCLASS", "addRecipe: " + descriptionOfRecipe.getText());
    }

}
