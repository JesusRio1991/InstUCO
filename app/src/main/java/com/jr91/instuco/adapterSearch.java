package com.jr91.instuco;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Usuario on 5/12/16.
 */

public class adapterSearch extends BaseAdapter {

    String[] names;
    String[] urls;
    Context cxt;
    private static LayoutInflater inflater = null;


    public adapterSearch(String[] name, String[] url, Context c) {
        this.cxt = c;
        this.names = name;
        this.urls = url;
        inflater = (LayoutInflater) this.cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if (vi == null) {
            vi = inflater.inflate(R.layout.rowlistsearch, null);
        }

        ImageView image_view = (ImageView) vi.findViewById(R.id.imageSearch);
        Picasso.with(cxt).load(urls[position]).into(image_view);

        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(cxt, userProfile.class);
                i.putExtra("username", names[position]);
                cxt.startActivity(i);
            }
        });

        TextView text = (TextView) vi.findViewById(R.id.nameSearch);
        text.setText(names[position]);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cxt, userProfile.class);
                i.putExtra("username", names[position]);
                cxt.startActivity(i);
            }
        });

        return vi;
    }
}
