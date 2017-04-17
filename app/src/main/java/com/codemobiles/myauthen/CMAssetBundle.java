package com.codemobiles.myauthen;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CMAssetBundle
{

  public static String getAppPackagePath(Context _context){
    final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    final String PACKAGE_NAME =_context.getPackageName();
    final String PACKAGE_PATH = "/Android/data/" + PACKAGE_NAME;
    final String APP_PATH = SDCARD_PATH + PACKAGE_PATH;
    File directory = new File(APP_PATH);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    return APP_PATH;
  }


  public static void copyAssetFile(String fileName, boolean _isReplace, Context _context){

    final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    final String PACKAGE_NAME =_context.getPackageName();
    final String PACKAGE_PATH = "/Android/data/" + PACKAGE_NAME;
    final String APP_PATH = SDCARD_PATH + PACKAGE_PATH;
    final String FILE_PATH = APP_PATH + "/" + fileName;

    // Create the folder if it doesn't exist:
    File directory = new File(APP_PATH);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    // Copy the database file if it doesn't exist:
    File file = new File(FILE_PATH);
    if (!file.exists() || _isReplace) {
      try {
        InputStream in = _context.getAssets().open(fileName);
        OutputStream out = new FileOutputStream(FILE_PATH);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        in.close();
        out.close();

      } catch (Exception e) {
      }
    }

  }
}
