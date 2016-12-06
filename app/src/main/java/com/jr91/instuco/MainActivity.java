package com.jr91.instuco;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static String url = "http://ucogram.hol.es/";
    ProgressDialog progressDialog;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String id = "idUser";
    SwipeRefreshLayout refreshLayout;
    int width = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        refreshLayout.setOnRefreshListener(this);

        if (AccessToken.getCurrentAccessToken() == null || Profile.getCurrentProfile() == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading images ...");
        progressDialog.show();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        } else {

            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 123);
                //Toast.makeText(this, "Requesting permissions", Toast.LENGTH_LONG).show();
            } else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "The permissions are already granted ", Toast.LENGTH_LONG).show();
            }

            int hasWriteSDPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (hasWriteSDPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);
                // Toast.makeText(this, "Requesting permissions", Toast.LENGTH_LONG).show();
            } else if (hasWriteSDPermission == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "The permissions are already granted ", Toast.LENGTH_LONG).show();
            }
        }


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Intent intent = new Intent(MainActivity.this, uploadPicture.class);
                    startActivity(intent);
                } else {

                    int hasWriteSDPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (hasWriteSDPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);
                        // Toast.makeText(this, "Requesting permissions", Toast.LENGTH_LONG).show();
                    } else if (hasWriteSDPermission == PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(this, "The permissions are already granted ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, uploadPicture.class);
                        startActivity(intent);
                    }
                }
            }
        });

        ListView listview = (ListView) findViewById(R.id.imageListView);
        listview.setAdapter(new adapterListView(new String[]{}, this, new String[]{}, new String[]{}, new String[]{}, new String[]{}, null, size.x, new String[]{}));

        width = size.x;

        Profile profile = Profile.getCurrentProfile();

        String nombre = profile.getFirstName().replace(" ", "");
        String apellidos = profile.getLastName().replace(" ", "");

        new getInfo(progressDialog, this, url + "getMain.php?username=" + remove(nombre + apellidos), "main", width).execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_list, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // See above
        //MenuItemCompat.setOnActionExpandListener(searchItem, new SearchViewExpandListener(this));
        //MenuItemCompat.setActionView(searchItem, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(MainActivity.this, "You searched " + s, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainActivity.this, searchActivity.class);
                i.putExtra("q", s);
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.salir) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        Profile profile = Profile.getCurrentProfile();

        String nombre = profile.getFirstName().replace(" ", "");
        String apellidos = profile.getLastName().replace(" ", "");

        new getInfo(progressDialog, this, url + "getMain.php?username=" + remove(nombre + apellidos), "main", width).execute();
        refreshLayout.setRefreshing(false);

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
