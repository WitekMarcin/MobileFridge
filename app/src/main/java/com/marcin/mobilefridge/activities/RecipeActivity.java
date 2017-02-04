package com.marcin.mobilefridge.activities;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.marcin.mobilefridge.R;
import com.marcin.mobilefridge.model.Recipe;
import com.marcin.mobilefridge.services.RecipeService;
import com.marcin.mobilefridge.util.SharedPreferencesUtil;

import java.io.File;
import java.io.IOException;

import static com.marcin.mobilefridge.util.SharedPreferencesUtil.SHARED_PREFERENCES_FILE_PATH;

public class RecipeActivity extends AppCompatActivity {

    private Recipe recipe;
    private RatingBar ratingOfRecipeBar;
    private RecipeService recipeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        SharedPreferencesUtil sharedPreferences = new SharedPreferencesUtil(
                this.getSharedPreferences(SHARED_PREFERENCES_FILE_PATH, Context.MODE_PRIVATE));
        recipeService = new RecipeService(sharedPreferences.restoreData(
                SharedPreferencesUtil.O_AUTH_KEY), sharedPreferences.restoreData
                (SharedPreferencesUtil.LOGIN_PREFERENCES_PATH));

        createRecipeObj();

        insertDataToLayout();

        setOnClickActionToSendButton();

    }

    private void setOnClickActionToSendButton() {
        Button sendButton = (Button) findViewById(R.id.markButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkRecipeTask markRecipeTask = new MarkRecipeTask(getApplicationContext(),
                        (int) ratingOfRecipeBar.getRating(), recipe.getId());
                markRecipeTask.execute();
            }
        });
    }

    private void insertDataToLayout() {
        TextView titleTextView = (TextView) findViewById(R.id.titleOfRecipe);
        TextView componentsTextView = (TextView) findViewById(R.id.productsForRecipe);
        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionRecipeAct);
        ratingOfRecipeBar = (RatingBar) findViewById(R.id.ratingBarRecipe);
        ImageView imageOfRecipe = (ImageView) findViewById(R.id.imageRecipeActivity);

        titleTextView.setText(recipe.getTitle());
        componentsTextView.setText(recipe.getComponentsOfRecipe());
        descriptionTextView.setText(recipe.getDescription());
        System.out.println((float) recipe.getRating());
        ratingOfRecipeBar.setRating((float) recipe.getRating());
        imageOfRecipe.setImageBitmap(recipe.getImage());
    }

    private void createRecipeObj() {
        if (true) {

        }
        recipe = new Recipe();
        recipe.setId(getIntent().getExtras().getLong("id"));
        recipe.setTitle(getIntent().getExtras().getString("title"));
        recipe.setDescription(getIntent().getExtras().getString("description"));
        recipe.setComponentsOfRecipe(getIntent().getExtras().getString("components"));
        recipe.setPicture(getIntent().getExtras().getString("image"));
        recipe.setRating(getIntent().getExtras().getInt("rating"));

        File folder = new File(getFilesDir() + File.separator + "images");
        File downloadFile2 = new File(folder.getAbsolutePath(), "recipe" + recipe.getId() + ".png");
        recipe.setImage(BitmapFactory.decodeFile(downloadFile2.getAbsolutePath()));
    }

    private class MarkRecipeTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private final Integer mark;
        private final Long id;

        MarkRecipeTask(Context context, Integer mark, Long id) {
            this.context = context;
            this.mark = mark;
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                recipeService.sendMarkToRecipe(mark, id);
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                try {
                    Toast.makeText(context, "Udało się dodać przepis", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Nie udało się dodać przepisu sprawdź połączenie z internetem",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(context, "Nie udało się dodać przepisu ", Toast.LENGTH_SHORT).show();
        }
    }
}
