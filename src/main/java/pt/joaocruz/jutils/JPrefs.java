package pt.joaocruz.jutils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by BEWARE S.A. on 21/02/14.
 */
public class JPrefs {

    private static String APP_PREFS;


    public static void init(String preferences_name) {
        APP_PREFS = preferences_name;
    }


    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_PREFS, 0);
    }

    public static void setParameter(Context context, String key, String value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setParameter(Context context, String key, boolean value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setParameter(Context context, String key, int value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setParameter(Context context, String key, float value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void setParameter(Context context, String key, Calendar value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String date = df.format(value.getTime());
        editor.putString(key, date);
        editor.commit();
    }

    public static String getParameter(Context context, String key, String defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getString(key, defValue);
    }

    public static float getParameter(Context context, String key, float defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getFloat(key, defValue);
    }

    public static long getParameter(Context context, String key, long defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getLong(key, defValue);
    }

    public static int getParameter(Context context, String key, int defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getInt(key, defValue);
    }

    public static boolean getParameter(Context context, String key, boolean defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getBoolean(key, defValue);
    }

    public static Calendar getParameter(Context context, String key, Calendar defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        String date = prefs.getString(key, "01-01-2012-00-00-00");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        Date d = new Date();
        try {
            d = df.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance(java.util.Locale.getDefault());
        cal.setTime(d);
        return cal;
    }

}
