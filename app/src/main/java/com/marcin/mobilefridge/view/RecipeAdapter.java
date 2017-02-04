package com.marcin.mobilefridge.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.marcin.mobilefridge.R;
import com.marcin.mobilefridge.activities.RecipeActivity;
import com.marcin.mobilefridge.model.Recipe;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Marcin on 30.01.2017.
 */
public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private final Context context;
    private int resource;
    private ArrayList<Recipe> items;

    public RecipeAdapter(Context context, @LayoutRes int resource, ArrayList<Recipe> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecipeAdapter.ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new RecipeAdapter.ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.title);
            holder.pictureView = (ImageView) row.findViewById(R.id.recipeImg);
            holder.ratingBar = (RatingBar) row.findViewById(R.id.ratingBar);

            row.setTag(holder);
        } else {
            holder = (RecipeAdapter.ViewHolder) row.getTag();
        }

        final Recipe recipe = items.get(position);

        holder.titleTextView.setText(recipe.getTitle());
        if (recipe.getImage() != null)
            holder.pictureView.setImageBitmap(recipe.getImage());
        if (recipe.getRating() != null)
            holder.ratingBar.setRating(recipe.getRating());
        holder.ratingBar.setEnabled(false);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecipeActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putLong("id", recipe.getId());
                mBundle.putString("title", recipe.getTitle());
                mBundle.putString("description", recipe.getDescription());
                mBundle.putString("components", recipe.getComponentsOfRecipe());
                mBundle.putString("image", recipe.getPicture());
                mBundle.putInt("rating", recipe.getRating());
                intent.putExtras(mBundle);
                startActivity(context, intent, mBundle);
            }
        });

        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView pictureView;
        RatingBar ratingBar;

    }
}
