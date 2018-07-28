package com.example.jayson.resolvecitycode.utils;

import android.content.Context;
import android.content.SharedPreferences;


import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";
    public static Set<String> EXCLUDE_FILEDS = new HashSet<>();


    static {
        /*
        EXCLUDE_FILEDS.add("location_tag");
        EXCLUDE_FILEDS.add("theme_tag");
        EXCLUDE_FILEDS.add("reg_type");
        EXCLUDE_FILEDS.add("theme_tag");
        EXCLUDE_FILEDS.add("location_tag");
        EXCLUDE_FILEDS.add("authorizer");
        EXCLUDE_FILEDS.add("province");
        EXCLUDE_FILEDS.add("town");
        EXCLUDE_FILEDS.add("country");
        EXCLUDE_FILEDS.add("school_card_num");
        EXCLUDE_FILEDS.add("biz_card_num");
        EXCLUDE_FILEDS.add("card_default");
        EXCLUDE_FILEDS.add("buddy");
        EXCLUDE_FILEDS.add("card_default");
        EXCLUDE_FILEDS.add("department");
        EXCLUDE_FILEDS.add("city_current");
        EXCLUDE_FILEDS.add("artists_card_num");
        EXCLUDE_FILEDS.add("alias_pingyin");
        EXCLUDE_FILEDS.add("name_pingyin");
        EXCLUDE_FILEDS.add("supertag_buddy_num");
        EXCLUDE_FILEDS.add("city_edit_time");
        EXCLUDE_FILEDS.add("user_id");
        EXCLUDE_FILEDS.add("country");
        EXCLUDE_FILEDS.add("theme_tag");*/

//        EXCLUDE_FILEDS.add("city_future");
//        EXCLUDE_FILEDS.add("if_plan");

        EXCLUDE_FILEDS.add("phone");
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, String object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, object);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static String get(Context context, String key, String defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultObject);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }


    }


}
