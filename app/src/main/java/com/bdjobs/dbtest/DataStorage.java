package com.bdjobs.dbtest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Rubayet on 22-Jan-17.
 */

public class DataStorage {

  private DBHelper dbHelper;

  public DataStorage(Context context)
  {
    dbHelper=new DBHelper(context);
    try {
      dbHelper.crateDatabase();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



  public ArrayList<ProfileModel> getAllProfile()
  {

    ArrayList<ProfileModel> profileModels=new ArrayList<>();
    dbHelper.openDataBase();

    String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_NAME_EMPLOYEE;

    Cursor cursor = dbHelper.getCursor(selectQuery);

    if (cursor!=null && cursor.getCount()>0){

      cursor.moveToFirst();

      for (int i=0; i<cursor.getCount(); i++){
        ProfileModel profileModel = new ProfileModel();

        profileModel.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.EMPLOYEE_COL_ID)));
        profileModel.setName(cursor.getString(cursor.getColumnIndex(DBHelper.EMPLOYEE_COL_NAME)));
        profileModel.setMobile(cursor.getString(cursor.getColumnIndex(DBHelper.EMPLOYEE_COL_MOBILE_NUMBER)));

        profileModels.add(profileModel);
        cursor.moveToNext();
      }
    }

    dbHelper.close();
    return profileModels;
  }


}
