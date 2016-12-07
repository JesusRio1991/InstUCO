package com.jr91.instuco;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Usuario on 5/12/16.
 */

public class adapterProfile extends BaseAdapter {

    String[] urls;
    String[] ids;
    Context cxt;
    private static LayoutInflater inflater = null;


    public adapterProfile(String[] id, String[] url, Context c) {
        this.cxt = c;
        this.urls = url;
        this.ids = id;
        inflater = (LayoutInflater) this.cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ids.length;
    }

    @Override
    public Object getItem(int position) {
        return ids[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.rowgridview, null);
        }

        ImageView tv = (ImageView) vi.findViewById(R.id.imageGridView);

        WindowManager wm = (WindowManager) cxt.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        tv.getLayoutParams().width = size.x / 3;
        tv.getLayoutParams().height = size.x / 3;

        Picasso.with(cxt).load(urls[position]).into(tv);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cxt, fotoIndividual.class);
                i.putExtra("idfoto", ids[position]);
                cxt.startActivity(i);
            }
        });


        return vi;
    }

}