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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Marcin on 23.01.2017.
 */
public class ProductsAdapter extends ArrayAdapter<HashMap<String, String>> {

    private final int resource;
    private final ArrayList<HashMap<String, String>> items;
    Context context;

    public ProductsAdapter(Context context, @LayoutRes int resource, ArrayList<HashMap<String, String>> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new ViewHolder();
            holder.nameTextView = (TextView) row.findViewById(R.id.id);
            holder.weightTextView = (TextView) row.findViewById(R.id.name);
            holder.idTextView = (TextView) row.findViewById(R.id.weight);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        HashMap<String, String> product = items.get(position);

        holder.nameTextView.setText(product.get("name"));
        holder.weightTextView.setText(product.get("weight"));
        holder.idTextView.setText(product.get("id"));

        return row;
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView weightTextView;
        TextView idTextView;
    }
}
