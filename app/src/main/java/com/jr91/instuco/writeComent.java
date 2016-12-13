package com.jr91.instuco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class writeComent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_coment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String q = intent.getExtras().getString("idfoto");
        final String user = intent.getExtras().getString("username");


        final HttpURLConnectionE h = new HttpURLConnectionE();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Profile profile = Profile.getCurrentProfile();
                String nombre = profile.getFirstName().replace(" ", "");
                String apellidos = profile.getLastName().replace(" ", "");

                String url_noti = "http://ucogram.hol.es/getToken.php?username=" + user;
                try {
                    String jsonStr = h.sendGet(url_noti);
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray object = jsonObj.getJSONArray("token");
                    JSONObject tk_obj = object.getJSONObject(0);


                    String text = remove(nombre + apellidos) + " Ha comentado una foto.";
                    String tittle = "¡Nuevos comentarios!";
                    url_noti = "http://ucogram.hol.es/sendNotification.php?token=" + tk_obj.getString("token") + "&text=" + URLEncoder.encode(text, "UTF-8") + "&tittle=" + URLEncoder.encode(tittle, "UTF-8");

                    h.sendGet(url_noti);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                ProgressDialog p = new ProgressDialog(writeComent.this);
                p.setMessage("Enviando comentario...");
                p.show();


                EditText edt = (EditText) findViewById(R.id.editText2);
                try {
                    h.sendGet("http://ucogram.hol.es/insertComent.php?idfoto=" + q + "&comments=" + URLEncoder.encode(edt.getText().toString()) + "&username=" + remove(nombre + apellidos));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(writeComent.this, fotoIndividual.class);
                i.putExtra("idfoto", q);
                startActivity(i);
                p.dismiss();
                finish();
            }
        });


        try {
            String json = h.sendGet("http://ucogram.hol.es/getCommentsFoto.php?idfoto=" + q);


            JSONObject jsonObj = new JSONObject(json);


            JSONArray comentarios = jsonObj.getJSONArray("foto");

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

            ListView lst = (ListView) findViewById(R.id.listComent);
            lst.setAdapter(new adapterComment(urls, names, comenta, this));

        } catch (JSONException e) {
            e.printStackTrace();
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

