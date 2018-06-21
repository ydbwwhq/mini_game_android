package com.hale.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by shinezone on 2018/6/19.
 */

public class FileUtil {
    public static  void save(String key, String content, Context context)
    {
        File dir = Environment.getExternalStorageDirectory();
        String fileName = key + ".txt";
        File file = new File(dir, fileName);
        try {
            if (!file.exists())
            {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            SharedPreferences sharedPreferences = context.getSharedPreferences("setting",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key,content);
            editor.commit();
        }
    }
    public static String read(String key,Context context)
    {
        File dir = Environment.getExternalStorageDirectory();
        String fileName = key + ".txt";
        File file = new File(dir, fileName);
        try {
            if (!file.exists())
            {
                SharedPreferences sharedPreferences = context.getSharedPreferences("setting",0);
                return sharedPreferences.getString(key,"");
            }
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            return  new String(bytes);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            SharedPreferences sharedPreferences = context.getSharedPreferences("setting",0);
           return sharedPreferences.getString(key,"");
        }
    }
}
