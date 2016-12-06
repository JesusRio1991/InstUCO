package com.jr91.instuco;

import android.content.Context;
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

public class adapterProfile extends BaseAdapter {

    String[] publicaciones;
    String[] urls;
    String[] siguendo;
    String[] seguidores;
    Context cxt;
    private static LayoutInflater inflater = null;


    public adapterProfile(String[] publica, String[] seguid, String[] siguend, String[] url, Context c) {
        this.cxt = c;
        this.publicaciones = publica;
        this.siguendo = siguend;
        this.seguidores = seguid;
        this.urls = url;
        inflater = (LayoutInflater) this.cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return publicaciones.length;
    }

    @Override
    public Object getItem(int position) {
        return publicaciones[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if (vi == null) {
            vi = inflater.inflate(R.layout.rowlistsearch, null);
        }

        ImageView image_view = (ImageView) vi.findViewById(R.id.imageSearch);
        Picasso.with(cxt).load(urls[position]).into(image_view);

        TextView text = (TextView) vi.findViewById(R.id.nameSearch);
        //text.setText(names[position]);

        //System.out.println("DENTRO -> " + names[position]);

        return vi;
    }
}
