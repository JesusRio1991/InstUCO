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
 * Created by Usuario on 16/11/16.
 */

class adapterListView extends BaseAdapter {

    Context context;
    String[] data;
    String[] urls;
    String[] userpictures;
    int sizeX;

    private static LayoutInflater inflater = null;

    public adapterListView(Context context, String[] data, String[] urls, String[] userpictures, int size) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        this.urls = urls;
        this.sizeX = size;
        this.userpictures = userpictures;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.rowlistview, null);
        }

        ImageView image_view = (ImageView) vi.findViewById(R.id.imageView);
        image_view.getLayoutParams().width = sizeX;
        image_view.getLayoutParams().height = sizeX;

        TextView text = (TextView) vi.findViewById(R.id.username);
        text.setText(data[position]);

        ImageView imageUploaded = (ImageView) vi.findViewById(R.id.imageView);
        Picasso.with(context).load(urls[position]).into(imageUploaded);

        ImageView imageIcon = (ImageView) vi.findViewById(R.id.imageView2);
        Picasso.with(context).load(userpictures[position]).into(imageIcon);


        return vi;
    }
}
