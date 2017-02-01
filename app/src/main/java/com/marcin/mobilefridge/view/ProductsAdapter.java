package com.marcin.mobilefridge.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.marcin.mobilefridge.R;
import com.marcin.mobilefridge.model.Product;

import java.util.ArrayList;

/**
 * Created by Marcin on 23.01.2017.
 */
public class ProductsAdapter extends ArrayAdapter<Product> {

    private final int resource;
    private final ArrayList<Product> items;
    Context context;

    public ProductsAdapter(Context context, @LayoutRes int resource, ArrayList<Product> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);


            holder = new ViewHolder();
            holder.nameTextView = (TextView) row.findViewById(R.id.name);
            holder.imageView = (ImageView) row.findViewById(R.id.smallProductImg);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final Product product = items.get(position);

        holder.nameTextView.setText(product.getName());
        holder.imageView.setImageBitmap(product.getSmallIconBitmap());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "W lodówce od" + product.getAddingTime(), Snackbar.LENGTH_LONG).show();
            }
        });

        return row;
    }

    static class ViewHolder {
        TextView nameTextView;
        ImageView imageView;
    }
}
