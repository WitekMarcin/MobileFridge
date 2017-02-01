package com.marcin.mobilefridge.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.marcin.mobilefridge.R;
import com.marcin.mobilefridge.model.Recipe;

import java.util.ArrayList;

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
            holder.descriptionTextView = (TextView) row.findViewById(R.id.description);
            holder.componentsTextView = (TextView) row.findViewById(R.id.components);
            row.setTag(holder);
        } else {
            holder = (RecipeAdapter.ViewHolder) row.getTag();
        }

        Recipe product = items.get(position);

        holder.titleTextView.setText(product.getTitle());
        holder.descriptionTextView.setText(product.getDescription());
        holder.componentsTextView.setText(String.valueOf(product.getComponentsOfRecipe()));

        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView componentsTextView;
    }
}
