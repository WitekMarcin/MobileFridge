package com.marcin.mobilefridge.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.marcin.mobilefridge.R;
import com.marcin.mobilefridge.model.Recipe;
import com.marcin.mobilefridge.services.RecipeService;
import com.marcin.mobilefridge.util.SharedPreferencesUtil;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static com.marcin.mobilefridge.util.SharedPreferencesUtil.SHARED_PREFERENCES_FILE_PATH;

public class CreateRecipe extends AppCompatActivity {

    private EditText titleOfRecipe;
    private EditText componentsOfRecipe;
    private EditText descriptionOfRecipe;
    private RecipeService recipeService;
    private View view;
    private View mProgressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtil sharedPreferences = new SharedPreferencesUtil(
                this.getSharedPreferences(SHARED_PREFERENCES_FILE_PATH, Context.MODE_PRIVATE));
        recipeService = new RecipeService(sharedPreferences.restoreData(
                SharedPreferencesUtil.O_AUTH_KEY), sharedPreferences.restoreData
                (SharedPreferencesUtil.LOGIN_PREFERENCES_PATH));
        setContentView(R.layout.activity_create_recipe);
        titleOfRecipe = (EditText) findViewById(R.id.titleOfRecipe);
        componentsOfRecipe = (EditText) findViewById(R.id.componentsOfRecipe);
        descriptionOfRecipe = (EditText) findViewById(R.id.descriptionOfRecipe);

        mProgressView = findViewById(R.id.recipe_add_progress);
        view = findViewById(R.id.create_recipe_id);
        //dodawanie zdj wez z atrakcji xd
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            view.setVisibility(show ? View.GONE : View.VISIBLE);
            view.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void addRecipe(View v) {
        Log.d("CRCLASS", "addRecipe: " + titleOfRecipe.getText());
        Log.d("CRCLASS", "addRecipe: " + componentsOfRecipe.getText());
        Log.d("CRCLASS", "addRecipe: " + descriptionOfRecipe.getText());

        Recipe recipe = new Recipe(titleOfRecipe.getText().toString(), descriptionOfRecipe.getText().toString(),
                null, componentsOfRecipe.getText().toString());
        System.out.println(recipe.getTitle() + " " + recipe.getComponentsOfRecipe() + " " + recipe.getDescription());
        showProgress(true);
        SendRecipeTask sendRecipeTask = new SendRecipeTask(this, recipe);
        sendRecipeTask.execute();
    }

    private class SendRecipeTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private String errorMessage;
        private Recipe recipe;

        SendRecipeTask(Context context, Recipe recipe) {
            this.context = context;
            this.recipe = recipe;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                recipeService.sendNewRecipe(recipe);
            } catch (SocketTimeoutException e) {
                errorMessage = getString(R.string.error_connection_failed);
                return false;
            } catch (IOException e) {
                errorMessage = getString(R.string.error_invalid_credentials);
                return false;
            } catch (Exception e) {
                errorMessage = getString(R.string.error_unexpected_error);
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if (success) {
                try {
                    Toast.makeText(context, "Udało się dodać przepis", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(context, "Nie udało się dodać przepisu : " + errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
