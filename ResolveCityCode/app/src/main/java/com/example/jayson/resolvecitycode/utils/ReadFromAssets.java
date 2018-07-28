package com.example.jayson.resolvecitycode.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jayson on 2017/1/3.
 */

public class ReadFromAssets {

    public static String ReadJsonFile(String name, Context context) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            InputStream is = context.getAssets().open(name);
            InputStreamReader inputStreamReader = new InputStreamReader(is, "utf-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

}
