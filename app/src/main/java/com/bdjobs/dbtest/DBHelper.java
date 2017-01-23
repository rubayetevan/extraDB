package com.bdjobs.dbtest;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Rubayet on 22-Jan-17.
 */


class DBHelper extends SQLiteOpenHelper {

  public static final String DB_PATH = "/data/data/com.bdjobs.dbtest/databases/";
  public static final String DB_NAME = "test.sqlite";
  public static final String TABLE_NAME_EMPLOYEE="employee";
  public static final int DATABASE_VERSION=1;

  public static final String EMPLOYEE_COL_ID="id";
  public static final String EMPLOYEE_COL_NAME="name";
  public static final String EMPLOYEE_COL_MOBILE_NUMBER="mobile";




  private SQLiteDatabase myDataBase;
  private final Context myContext;

  public DBHelper (Context context) {
    super(context, DB_NAME, null, DATABASE_VERSION);
    this.myContext = context;
  }

  public void crateDatabase() throws IOException {
    boolean vtVarMi = isDatabaseExist();

    if (!vtVarMi) {
      this.getReadableDatabase();

      try {
        copyDataBase();
      } catch (IOException e) {
        throw new Error("Error copying database");
      }
    }
  }

  private void copyDataBase() throws IOException {

    // Open your local db as the input stream
    InputStream myInput = myContext.getAssets().open(DB_NAME);

    // Path to the just created empty db
    String outFileName = DB_PATH + DB_NAME;

    // Open the empty db as the output stream
    OutputStream myOutput = new FileOutputStream(outFileName);

    // transfer bytes from the inputfile to the outputfile
    byte[] buffer = new byte[1024];
    int length;
    while ((length = myInput.read(buffer)) > 0) {
      myOutput.write(buffer, 0, length);
    }

    // Close the streams
    myOutput.flush();
    myOutput.close();
    myInput.close();
  }

  private boolean isDatabaseExist() {
    SQLiteDatabase kontrol = null;

    try {
      String myPath = DB_PATH + DB_NAME;
      kontrol = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    } catch (SQLiteException e) {
      kontrol = null;
    }

    if (kontrol != null) {
      kontrol.close();
    }
    return kontrol != null ? true : false;
  }

  public void openDataBase() throws SQLException {

    // Open the database
    String myPath = DB_PATH + DB_NAME;
    myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

  }

  public Cursor getCursor(String query) {

    Cursor cursor = myDataBase.rawQuery(query, null);

    return cursor;
  }

  @Override
  public synchronized void close() {
    if (myDataBase != null)
      myDataBase.close();
    super.close();
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }
}