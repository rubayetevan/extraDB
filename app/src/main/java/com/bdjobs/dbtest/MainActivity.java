package com.bdjobs.dbtest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  DataStorage dataStorage;
  ArrayList<ProfileModel> profileModels=new ArrayList<>();
  final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
  final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
  Button updateBTN;
  String link ="http://creativemine.net/rubayet/test.sqlite";
  int lenghtOfFile;
  private AsyncTask<String, String, String> mTask1;
  ProgressDialog progressDialog;

  ProgressDialog pd ;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    updateBTN = (Button) findViewById(R.id.updateBTN);

    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED)
    {
      Rqpr();
    }
    else{
      dataStorage = new DataStorage(this);
      profileModels = dataStorage.getAllProfile();
      Toast.makeText(this, String.valueOf(profileModels.size()), Toast.LENGTH_SHORT).show();
      updateBTN.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          mTask1 = new DownloadFileFromURL().execute(link);
        }
      });
    }
  }

  private void Rqpr() {
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {

      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
          Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        // Show an expanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

      } else {

        // No explanation needed, we can request the permission.

        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
        // app-defined int constant. The callback method gets the
        // result of the request.
      }

    }
  }


  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

          // permission was granted, yay! Do the
          // contacts-related task you need to do.
          dataStorage = new DataStorage(this);
          profileModels = dataStorage.getAllProfile();
          Toast.makeText(this, profileModels.get(0).getName(), Toast.LENGTH_SHORT).show();
          updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
              mTask1 = new DownloadFileFromURL().execute(link);

            }
          });

        } else {
                Rqpr();
        }
        return;
      }

      // other 'case' lines to check for other
      // permissions this app might request
    }
  }




  class DownloadFileFromURL extends AsyncTask<String, String, String> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = new ProgressDialog(MainActivity.this);
      progressDialog.show();

    }

    @Override
    protected String doInBackground(String... f_url) {
      int count;
      try {
        URL url = new URL(f_url[0]);
        URLConnection conection = url.openConnection();
        conection.connect();
        // getting file length
        int lenghtOfFile = conection.getContentLength();

        System.out.println("lenghtOfFile: " + lenghtOfFile);

        // input stream to read file - with 8k buffer
        InputStream input = new BufferedInputStream(url.openStream(), 8192);

        // Output stream to write file
        OutputStream output = new FileOutputStream(DBHelper.DB_PATH+DBHelper.DB_NAME);

        byte data[] = new byte[1024];

        long total = 0;

        while ((count = input.read(data)) != -1) {
          total += count;
          // publishing the progress....
          // After this onProgressUpdate will be called
          publishProgress("" + (int) ((total * 100) / lenghtOfFile));


          // writing data to file
          output.write(data, 0, count);

          System.out.println("total: " + total);
        }

        // flushing output
        output.flush();

        // closing streams
        output.close();
        input.close();

      } catch (Exception e) {
        Log.e("Error: ", e.getMessage());


      }

      return null;
    }

    /**
     * Updating progress bar
     */
    @Override
    protected void onProgressUpdate(String... progress) {
      // setting progress percentage

    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {
      // dismiss the dialog after the file was downloaded
      //dismissDialog(progress_bar_type);
      //button.setText("Set as Wallpaper");
      if(progressDialog!=null) {
        progressDialog.dismiss();
      }
      profileModels = dataStorage.getAllProfile();
      Toast.makeText(MainActivity.this, String.valueOf(profileModels.size()), Toast.LENGTH_SHORT).show();


    }

  }

}
