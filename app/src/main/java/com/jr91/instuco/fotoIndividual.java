package com.jr91.instuco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class fotoIndividual extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

            JSONObject comentario_c = fotoPerfil.getJSONObject(0);
            TextView txtPublicaciones = (TextView) findViewById(R.id.comment);
            txtPublicaciones.setText(comentario_c.getString("comment"));

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

                    try {
                        if (like_c.getString("value").compareTo("0") == 0) {
                            imageC.setImageResource(R.mipmap.ic_b_corazon);
                            url = "http://ucogram.hol.es/dislike.php?username=" + remove(nombre + apellidos) + "&idfoto=" + q;
                            like_c.put("value", 1);
                        } else {
                            imageC.setImageResource(R.mipmap.ic_r_corazon);
                            url = "http://ucogram.hol.es/like.php?username=" + remove(nombre + apellidos) + "&idfoto=" + q;
                            like_c.put("value", 0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // int position = listview.getPositionForView(arg0);

                    HttpURLConnectionE h = new HttpURLConnectionE();
                    try {

                        String json = h.sendGet(url);
                        JSONObject jsonObj = new JSONObject(json);

                        Iterator<String> keys = jsonObj.keys();
                        while (keys.hasNext()) {
                            String keyValue = (String) keys.next();
                            String valueString = jsonObj.getString(keyValue);
                            Toast.makeText(fotoIndividual.this, valueString, Toast.LENGTH_LONG).show();
                        }

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("fotoIndividual Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
