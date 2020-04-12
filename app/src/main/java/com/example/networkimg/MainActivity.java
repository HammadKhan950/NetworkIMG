package com.example.networkimg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager mConnMgr;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnMgr=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        imageView=(ImageView)findViewById(R.id.resultImageView);
    }
    public void onRefreshImage(View v) {
        final String TAG = "Refresh";
        String imagePath = "http://lorempixel.com/640/480/";
        if (mConnMgr != null) {
            NetworkInfo networkinfo = mConnMgr.getActiveNetworkInfo();
            if (networkinfo != null && networkinfo.isConnected()) {

                new DownloadImageTask().execute(imagePath);
            } else {
                Toast.makeText(this, "Network not Available", Toast.LENGTH_LONG).show();
            }

        }
    }
    private class DownloadImageTask extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            return downloadImage(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }
    }
        public Bitmap downloadImage(String path){
            final String TAG="Download Task";
            Bitmap bitmap=null;
            InputStream inStream;
            try {
                URL url=new URL(path);
                HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
                urlConn.setConnectTimeout(1000000);
                urlConn.setReadTimeout(100000);
                urlConn.setRequestMethod("GET");
                urlConn.setDoInput(true);
                urlConn.connect();
                 inStream=urlConn.getInputStream();
                 bitmap= BitmapFactory.decodeStream(inStream);
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

