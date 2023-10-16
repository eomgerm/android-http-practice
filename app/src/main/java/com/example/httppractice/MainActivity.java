package com.example.httppractice;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ImageView imgView;

    String imageUrl = "http://kiokahn.synology.me:30000/";
    Bitmap bmImg = null;
    CLoadImage task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activiy);

        imgView = (ImageView) findViewById(R.id.imgView);
        task = new CLoadImage();

        Log.e("Main", "Started");
    }

    public void onClickForLoad(View v) {
        task.execute(imageUrl + "uploads/-/system/appearance/logo/1/Gazzi_Labs_CI_type_B_-_big_logo.png");

        Toast.makeText(getApplicationContext(), "Load", Toast.LENGTH_LONG).show();
    }

    public void onClickForSave(View v) {
        saveBitmapToJpeg(bmImg, "DCIM", "image");

        Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_LONG).show();
    }

    public static void saveBitmapToJpeg(Bitmap bitmap, String folder, String name) {
        String exStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderName = "/" + folder + "/";
        String fileName = name + ".jpg";
        String path = exStorage + folderName;

        File filePath;
        filePath = new File(path);

        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        try {
            FileOutputStream out = new FileOutputStream(path + fileName);

            bitmap.compress(CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
    }

    private class CLoadImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            Log.e("Async", Arrays.toString(urls));
            try {
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmImg;
        }

        @Override
        protected void onPostExecute(Bitmap img) {
            imgView.setImageBitmap(img);
        }
    }
}

