package com.jr91.instuco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class userProfile extends AppCompatActivity {

    ArrayList<String> publicaciones = new ArrayList<String>();
    ArrayList<String> seguidores = new ArrayList<String>();
    ArrayList<String> siguiendo = new ArrayList<String>();
    ArrayList<String> imagenPerfil = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String q = intent.getExtras().getString("username");

        Profile profile = Profile.getCurrentProfile();

        String nombre = profile.getFirstName().replace(" ", "");
        String apellidos = profile.getLastName().replace(" ", "");

        HttpURLConnectionE h = new HttpURLConnectionE();
        try {
            String json = h.sendGet("http://ucogram.hol.es/getProfile.php?username=" + q + "&username2=" + remove(nombre + apellidos));

            JSONObject jsonObj = new JSONObject(json);

            JSONArray count_img = jsonObj.getJSONArray("count_img");
            JSONObject count_img_c = count_img.getJSONObject(0);
            TextView txtPublicaciones = (TextView) findViewById(R.id.profilePublicaciones);
            txtPublicaciones.setText(count_img_c.getString("n_img"));


            JSONArray n_followers = jsonObj.getJSONArray("seguidores");
            JSONObject n_followers_c = n_followers.getJSONObject(0);
            TextView txtSeguidores = (TextView) findViewById(R.id.profileSeguidores);
            txtSeguidores.setText(n_followers_c.getString("n_followers"));


            JSONArray n_following = jsonObj.getJSONArray("siguiendo");
            JSONObject n_following_c = n_following.getJSONObject(0);
            TextView txtSiguiendo = (TextView) findViewById(R.id.profileSiguiendo);
            txtSiguiendo.setText(n_following_c.getString("n_following"));


            JSONArray fotoPerfil = jsonObj.getJSONArray("fotoPerfil");
            final JSONObject fotoPerfil_c = fotoPerfil.getJSONObject(0);
            ImageView imageUploaded = (ImageView) findViewById(R.id.profileImg);
            Picasso.with(this).load(fotoPerfil_c.getString("urlfoto")).into(imageUploaded);


            String[] urls_grid = new String[500];
            String[] ids = new String[500];
            JSONArray object = jsonObj.getJSONArray("pictures");

            for (int i = 0; i < object.length(); i++) {
                JSONObject c = object.getJSONObject(i);
                urls_grid[i] = c.getString("url");
                ids[i] = c.getString("idfoto");

            }

            GridView gridView = (GridView) findViewById(R.id.gridProfile);
            gridView.setNumColumns(3);
            gridView.setAdapter(new adapterProfile(ids, urls_grid, this));

            final Button btn = (Button) findViewById(R.id.profileSeguir);

            JSONArray fri = jsonObj.getJSONArray("friends");
            final JSONObject fri_c = fri.getJSONObject(0);

            int friend = 0;

            if (fri_c.getString("value").compareTo("0") == 0) {
                btn.setText("SIGUIENDO");
                friend = 0;
            } else {
                btn.setText("SEGUIR");
                friend = 1;
            }


            final int[] finalFriend = {friend};
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Profile profile = Profile.getCurrentProfile();

                    String nombre = profile.getFirstName().replace(" ", "");
                    String apellidos = profile.getLastName().replace(" ", "");
                    String url = "";

                    if (finalFriend[0] == 0) {
                        btn.setText("SEGUIR");
                        url = "http://ucogram.hol.es/setNotFriends.php?username1=" + remove(nombre + apellidos) + "&username2=" + q;
                        finalFriend[0] = 1;
                    } else {
                        btn.setText("SIGUIENDO");
                        url = "http://ucogram.hol.es/setFriends.php?username1=" + remove(nombre + apellidos) + "&username2=" + q;
                        finalFriend[0] = 0;
                    }


                    HttpURLConnectionE h = new HttpURLConnectionE();
                    try {
                        h.sendGet(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });


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
