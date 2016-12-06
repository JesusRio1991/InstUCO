package com.jr91.instuco;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class uploadPicture extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private Uri imageUri;
    Bitmap thumbnail = null;
    EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEdit = (EditText) findViewById(R.id.editText);

        this.imageView = (ImageView) this.findViewById(R.id.imageView5);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_REQUEST);


        } else {
            Toast.makeText(this, "Debes dar permiso de cámara", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;
            AssetFileDescriptor fileDescriptor = null;

            try {
                fileDescriptor = this.getContentResolver().openAssetFileDescriptor(imageUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    thumbnail = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                    fileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            imageView.setImageBitmap(thumbnail);


            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            imageView.getLayoutParams().width = size.x;
            imageView.getLayoutParams().height = size.x;

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProgressDialog p = new ProgressDialog(uploadPicture.this);
                    p.setMessage("Subiendo foto...");
                    p.show();
                    try {
                        upload(thumbnail);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    p.dismiss();
                }
            });


        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void upload(Bitmap data) throws UnsupportedEncodingException {


        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getApplicationContext(), data);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));

        System.out.println("--> " + getRealPathFromURI(tempUri));

        String filename = finalFile.getAbsolutePath();


        try {
            Profile profile = Profile.getCurrentProfile();

            String nombre = profile.getFirstName().replace(" ", "");
            String apellidos = profile.getLastName().replace(" ", "");

            FileInputStream fis = new FileInputStream(filename);
            HttpFileUploader htfu = new HttpFileUploader("http://ucogram.hol.es/uploadFoto.php?comment=" + URLEncoder.encode(mEdit.getText().toString()) + "&username=" + remove(nombre + apellidos), "uploadedfile", filename);
            htfu.doStart(fis);
            fis.close();

            System.out.println("URL -> " + "http://ucogram.hol.es/uploadFoto.php?comment=" + URLEncoder.encode(mEdit.getText().toString()) + "&username=" + remove(nombre + apellidos));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Intent intent = new Intent(uploadPicture.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(uploadPicture.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    System.out.println("Error in http connection " + e.toString());
                }
            }
        });
        t.start();

        if (thumbnail != null && !thumbnail.isRecycled()) {
            thumbnail.recycle();
            thumbnail = null;
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