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
 * Created by Usuario on 9/12/16.
 */

public class adapterComment extends BaseAdapter {

    String[] names;
    String[] coment;
    String[] urls;
    Context cxt;
    private static LayoutInflater inflater = null;


    public adapterComment(String[] urls, String[] names, String[] commen, Context c) {
        this.cxt = c;
        this.names = names;
        this.coment = commen;
        this.urls = urls;
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
            vi = inflater.inflate(R.layout.rowcoments, null);
        }

        TextView name = (TextView) vi.findViewById(R.id.nameCom);
        name.setText(names[position]);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cxt, userProfile.class);
                intent.putExtra("username", names[position]);
                cxt.startActivity(intent);
            }
        });

        TextView comentario = (TextView) vi.findViewById(R.id.comentario);
        comentario.setText(coment[position]);

        ImageView im = (ImageView) vi.findViewById(R.id.imageView6);
        Picasso.with(cxt).load(urls[position]).into(im);


        return vi;
    }
}
