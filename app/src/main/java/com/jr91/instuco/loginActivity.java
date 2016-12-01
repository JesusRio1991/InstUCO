package com.jr91.instuco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class loginActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String id = "idUser";

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        callbackManager = CallbackManager.Factory.create();

        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    login(loginResult.getAccessToken().getUserId());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancel() {
                logout();
            }

            @Override
            public void onError(FacebookException e) {
                logout();
            }
        });
    }

    private void login(String userId) throws UnsupportedEncodingException {

        Profile profile = Profile.getCurrentProfile();

        String nombre = profile.getFirstName().replace(" ", "");
        String apellidos = profile.getLastName().replace(" ", "");

        String url = "http://ucogram.hol.es/setUser.php?username=" + nombre + apellidos + "&urlfoto=";

        url = remove(url);

        url = url + URLEncoder.encode("https://graph.facebook.com/" + userId + "/picture?type=large", "UTF-8");

        HttpHandler sh = new HttpHandler();
        sh.makeServiceCall(url.trim());

        System.out.println("[" + url.trim() + "]");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void logout() {
        LoginManager.getInstance().logOut();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
