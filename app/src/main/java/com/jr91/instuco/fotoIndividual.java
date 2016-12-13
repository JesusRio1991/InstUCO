package com.jr91.instuco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class fotoIndividual extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_individual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String q = intent.getExtras().getString("idfoto");

        Profile profile = Profile.getCurrentProfile();

        String nombre = profile.getFirstName().replace(" ", "");
        String apellidos = profile.getLastName().replace(" ", "");


        HttpURLConnectionE h = new HttpURLConnectionE();
        try {
            String json = h.sendGet("http://ucogram.hol.es/getFoto.php?idfoto=" + q + "&username=" + remove(nombre + apellidos));
            JSONObject jsonObj = new JSONObject(json);

            JSONArray fotoPerfil = jsonObj.getJSONArray("foto");
            JSONObject fotoPerfil_c = fotoPerfil.getJSONObject(0);
            ImageView imageUploaded = (ImageView) findViewById(R.id.imageView);
            Picasso.with(this).load(fotoPerfil_c.getString("url")).into(imageUploaded);

            final String user = fotoPerfil_c.getString("username");


            JSONArray megustas = jsonObj.getJSONArray("like_photo");
            final JSONObject[] megustas_c = {megustas.getJSONObject(0)};
            final TextView mtxt = (TextView) findViewById(R.id.textView24);
            mtxt.setText(megustas_c[0].getString("value") + " Me gusta");

            mtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(fotoIndividual.this, likePictureUsers.class);
                    i.putExtra("idfoto", q);
                    startActivity(i);
                }
            });


            final int[] co = {Integer.parseInt(megustas_c[0].getString("value"))};

            JSONObject comentario_c = fotoPerfil.getJSONObject(0);
            TextView txtPublicaciones = (TextView) findViewById(R.id.comment);
            txtPublicaciones.setText(comentario_c.getString("comment"));

            JSONArray comentarios = jsonObj.getJSONArray("comentarios");

            String[] names = new String[comentarios.length()];
            String[] comenta = new String[comentarios.length()];
            String[] urls = new String[comentarios.length()];

            // Getting JSON Array node
            for (int i = 0; i < comentarios.length(); i++) {
                JSONObject c = comentarios.getJSONObject(i);

                names[i] = c.getString("username");

                if (names[i].compareTo("none") == 0) {
                    names[i] = null;
                    break;
                }


                names[i] = c.getString("username");
                comenta[i] = c.getString("comments");
                urls[i] = c.getString("urlfoto");


            }

            final ImageView cmt = (ImageView) findViewById(R.id.imageView4);

            cmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(fotoIndividual.this, writeComent.class);
                    i.putExtra("idfoto", q);
                    i.putExtra("username", user);
                    startActivity(i);
                }
            });

            ListView lst = (ListView) findViewById(R.id.listComment);
            lst.setAdapter(new adapterComment(urls, names, comenta, fotoIndividual.this));

            JSONArray like = jsonObj.getJSONArray("like");
            final JSONObject like_c = like.getJSONObject(0);

            final ImageView imageC = (ImageView) findViewById(R.id.imageView3);
            imageC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Profile profile = Profile.getCurrentProfile();

                    String nombre = profile.getFirstName().replace(" ", "");
                    String apellidos = profile.getLastName().replace(" ", "");

                    String url = "";
                    HttpURLConnectionE h = new HttpURLConnectionE();


                    try {
                        if (like_c.getString("value").compareTo("0") == 0) {
                            imageC.setImageResource(R.mipmap.ic_b_corazon);
                            url = "http://ucogram.hol.es/dislike.php?username=" + remove(nombre + apellidos) + "&idfoto=" + q;
                            like_c.put("value", 1);

                            co[0]--;

                            mtxt.setText(co[0] + " Me gusta");

                        } else {

                            String url_noti = "http://ucogram.hol.es/getToken.php?username=" + user;
                            try {
                                String jsonStr = h.sendGet(url_noti);
                                JSONObject jsonObj = new JSONObject(jsonStr);

                                // Getting JSON Array node
                                JSONArray object = jsonObj.getJSONArray("token");
                                JSONObject tk_obj = object.getJSONObject(0);


                                String text = remove(nombre + apellidos) + " ha dado a me gusta a una foto.";
                                String tittle = "¡Nuevos me gusta!";
                                url_noti = "http://ucogram.hol.es/sendNotification.php?token=" + tk_obj.getString("token") + "&text=" + URLEncoder.encode(text, "UTF-8") + "&tittle=" + URLEncoder.encode(tittle, "UTF-8");

                                h.sendGet(url_noti);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            imageC.setImageResource(R.mipmap.ic_r_corazon);
                            url = "http://ucogram.hol.es/like.php?username=" + remove(nombre + apellidos) + "&idfoto=" + q;
                            like_c.put("value", 0);

                            co[0]++;

                            mtxt.setText(co[0] + " Me gusta");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {

                        h.sendGet(url);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            if (like_c.getString("value").compareTo("0") == 0) {
                imageC.setImageResource(R.mipmap.ic_r_corazon);
            } else {
                imageC.setImageResource(R.mipmap.ic_b_corazon);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
