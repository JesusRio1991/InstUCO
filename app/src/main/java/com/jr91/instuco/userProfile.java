package com.jr91.instuco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

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
        String q = intent.getExtras().getString("username");

        HttpURLConnectionE h = new HttpURLConnectionE();
        try {
            String json = h.sendGet("http://ucogram.hol.es/getProfile.php?username=" + q);

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
            JSONObject fotoPerfil_c = fotoPerfil.getJSONObject(0);
            ImageView imageUploaded = (ImageView) findViewById(R.id.profileImg);
            Picasso.with(this).load(fotoPerfil_c.getString("urlfoto")).into(imageUploaded);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
