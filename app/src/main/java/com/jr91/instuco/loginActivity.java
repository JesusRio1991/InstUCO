package com.jr91.instuco;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;


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


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY_KEY_HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            Profile profile = Profile.getCurrentProfile();

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            sendRegistrationToServer(refreshedToken);

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
                } catch (JSONException e) {
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


    private void login(String userId) throws UnsupportedEncodingException, JSONException {

        Profile profile = Profile.getCurrentProfile();

        String nombre = profile.getFirstName().replace(" ", "");
        String apellidos = profile.getLastName().replace(" ", "");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);

        String url = "http://ucogram.hol.es/setUser.php?username=" + remove(nombre + apellidos) + "&urlfoto=" + "https://graph.facebook.com/" + userId + "/picture?type=large";

        HttpURLConnectionE h = new HttpURLConnectionE();
        try {
            String json = h.sendGet(url);
            JSONObject jsonObj = new JSONObject(json);

            Iterator<String> keys = jsonObj.keys();
            while (keys.hasNext()) {
                String keyValue = (String) keys.next();
                String valueString = jsonObj.getString(keyValue);
                Toast.makeText(this, valueString, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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


    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    public static void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Profile profile = Profile.getCurrentProfile();

        String nombre = profile.getFirstName().replace(" ", "");
        String apellidos = profile.getLastName().replace(" ", "");

        HttpURLConnectionE h = new HttpURLConnectionE();

        String url_noti = "http://ucogram.hol.es/uploadToken.php?username=" + remove(nombre + apellidos) + "&token=" + token;
        try {
            h.sendGet(url_noti);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
