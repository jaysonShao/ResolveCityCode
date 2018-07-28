package com.example.jayson.resolvecitycode.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.jayson.resolvecitycode.bean.FourCityBean;
import com.github.promeg.pinyinhelper.Pinyin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 * Created by jayson on 2018/2/28.
 */

public class DataUtil {

    public static void updateHistoryCityName(Context paramContext, LinkedList<FourCityBean> cityList, String numTag, String itemTag) {
        SharedPreferences sharedPreferences = paramContext.getSharedPreferences("historyCityList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(numTag, cityList.size());
        for (int i = 0; i < cityList.size(); i++) {
            editor.putString(itemTag + i,Object2String (cityList.get(i)));
        }
        editor.commit();
    }

    public static LinkedList<FourCityBean> getHistoryCityName(Context paramContext, String numTag, String itemTag) {
        LinkedList<FourCityBean> citysList = new LinkedList<FourCityBean>();
        SharedPreferences sharedPreferences = paramContext.getSharedPreferences("historyCityList", Context.MODE_PRIVATE);
        int cityNums = sharedPreferences.getInt(numTag, 0);
        for (int i = 0; i < cityNums; i++) {
            String cityItem = sharedPreferences.getString(itemTag + i, null);
            citysList.add((FourCityBean) String2Object(cityItem));
        }
        return citysList;
    }

    /**
     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param object 待加密的转换为String的对象
     * @return String   加密后的String
     */
    private static String Object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }




    public static void setDatabaseConfig(Context paramContext, Boolean is,String key) {
        SharedPreferences sharedPreferences = paramContext.getSharedPreferences("historyCityList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, is);
        editor.commit();
    }

    public static Boolean getDatabaseConfig(Context paramContext,String key) {
        SharedPreferences sharedPreferences = paramContext.getSharedPreferences("historyCityList", Context.MODE_PRIVATE);
        Boolean is = sharedPreferences.getBoolean(key, false);
        return is;
    }


    public static String getPinyin(String chinese){
        StringBuilder pySb = new StringBuilder();
        //遍历target的每个char得到它的全拼音
        for(int i=0;i<chinese.length();i++){
            //利用TinyPinyin将char转成拼音
            //查看源码，方法内 如果char为汉字，则返回大写拼音
            //如果c不是汉字，则返回String.valueOf(c)
            pySb.append(Pinyin.toPinyin(chinese.charAt(i)).toUpperCase());
        }
        return pySb.toString();
    }


}
