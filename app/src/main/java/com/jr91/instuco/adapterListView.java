package com.jr91.instuco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Usuario on 16/11/16.
 */

class adapterListView extends BaseAdapter {

    Context context;
    String[] data;
    String[] urls;
    String[] userpictures;
    String[] idFoto;
    String[] like;
    String[] like_photo;
    ArrayList<String> id;
    int sizeX;
    String[] comment;
    ScaleGestureDetector SGD;

    private static LayoutInflater inflater = null;

    public adapterListView(String[] likes, String[] likes_photos, Context context, String[] data, String[] urls, String[] userpictures, String[] idfotos, ArrayList<String> id, int size, String[] comentario) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        this.urls = urls;
        this.sizeX = size;
        this.like_photo = likes_photos;
        this.userpictures = userpictures;
        this.comment = comentario;
        this.idFoto = idfotos;
        this.id = id;
        this.like = likes;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
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

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, userProfile.class);
                i.putExtra("username", data[position]);
                context.startActivity(i);
            }
        });

        final ImageView imageC = (ImageView) vi.findViewById(R.id.imageView3);
        if (like[position].compareTo("0") == 0) {
            imageC.setImageResource(R.mipmap.ic_r_corazon);
        } else {
            imageC.setImageResource(R.mipmap.ic_b_corazon);

        }
        //Picasso.with(context).load(urls[position]).into(imageUploaded);

        ImageView imageUploaded = (ImageView) vi.findViewById(R.id.imageView);
        Picasso.with(context).load(urls[position]).into(imageUploaded);

        final TextView lik_ph = (TextView) vi.findViewById(R.id.textView);
        lik_ph.setText(like_photo[position] + " Me gusta");

        ImageView imageIcon = (ImageView) vi.findViewById(R.id.imageView2);
        Picasso.with(context).load(userpictures[position]).into(imageIcon);

        final ImageView likeView = (ImageView) vi.findViewById(R.id.imageView3);


        final ImageView cmt = (ImageView) vi.findViewById(R.id.imageView4);

        cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, writeComent.class);
                i.putExtra("idfoto", id.get(position));
                i.putExtra("username", data[position]);
                context.startActivity(i);
            }
        });

        TextView coment = (TextView) vi.findViewById(R.id.comment);
        coment.setText(comment[position]);

        ImageView b = (ImageView) vi.findViewById(R.id.imageView3);

        final ListView listview = (ListView) ((Activity) context).findViewById(R.id.imageListView);

        imageUploaded.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Descargar Imagen")
                        .setMessage("¿Desea descarga la imagen?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ProgressDialog mProgressDialog;

                                // instantiate it within the onCreate method
                                mProgressDialog = new ProgressDialog(context);
                                mProgressDialog.setMessage("Descargando...");
                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                mProgressDialog.setCancelable(true);

                                // execute this when the downloader must be fired
                                final DownloadTask downloadTask = new DownloadTask(context, mProgressDialog);
                                downloadTask.execute(urls[position]
                                );

                                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        downloadTask.cancel(true);
                                    }
                                });
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();


                return true;
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Profile profile = Profile.getCurrentProfile();

                String nombre = profile.getFirstName().replace(" ", "");
                String apellidos = profile.getLastName().replace(" ", "");

                String url;
                HttpURLConnectionE h = new HttpURLConnectionE();

                if (like[position].compareTo("0") == 0) {
                    likeView.setImageResource(R.mipmap.ic_b_corazon);
                    url = "http://ucogram.hol.es/dislike.php?username=" + remove(nombre + apellidos) + "&idfoto=" + id.get(position);
                    like[position] = "1";

                    int co = Integer.parseInt(like_photo[position]);
                    co--;
                    like_photo[position] = Integer.toString(co);

                    lik_ph.setText(co + " Me gusta");
                } else {
                    likeView.setImageResource(R.mipmap.ic_r_corazon);
                    url = "http://ucogram.hol.es/like.php?username=" + remove(nombre + apellidos) + "&idfoto=" + id.get(position);
                    like[position] = "0";

                    int co = Integer.parseInt(like_photo[position]);
                    co++;
                    like_photo[position] = Integer.toString(co);

                    lik_ph.setText(co + " Me gusta");

                    String url_noti = "http://ucogram.hol.es/getToken.php?username=" + data[position];
                    try {
                        String jsonStr = h.sendGet(url_noti);
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray object = jsonObj.getJSONArray("token");
                        JSONObject tk_obj = object.getJSONObject(0);


                        String text = remove(nombre + apellidos) + " ha dado a me gusta a una foto.";
                        String tittle = "Nuevos me gusta.";
                        url_noti = "http://ucogram.hol.es/sendNotification.php?token=" + tk_obj.getString("token") + "&text=" + URLEncoder.encode(text, "UTF-8") + "&tittle=" + URLEncoder.encode(tittle, "UTF-8");

                        h.sendGet(url_noti);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                int position = listview.getPositionForView(arg0);


                try {

                    String json = h.sendGet(url);
                    JSONObject jsonObj = new JSONObject(json);

                    Iterator<String> keys = jsonObj.keys();
                    while (keys.hasNext()) {
                        String keyValue = (String) keys.next();
                        String valueString = jsonObj.getString(keyValue);
                        Toast.makeText(context, valueString, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        return vi;
    }

    public static String remove(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }//remove1


}
