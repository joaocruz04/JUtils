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


    /**
     * Initialization method
     * @param preferences_name name of the preferences file (tip: give the name: <APP_NAME>_prefs)
     */
    public static void init(String preferences_name) {
        APP_PREFS = preferences_name;
    }

    /**
     * Gets the SharedPreferences
     * @param context
     * @return the SharedPreferes object identified by the name given in the {@code init} method
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_PREFS, 0);
    }

    /**
     * Sets a String parameter
     * @param context A context
     * @param key key representing the pair
     * @param value String value for the given key
     */
    public static void setParameter(Context context, String key, String value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Sets a boolean parameter
     * @param context A context
     * @param key key representing the pair
     * @param value boolean value for the given key
     */
    public static void setParameter(Context context, String key, boolean value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Sets a integer parameter
     * @param context A context
     * @param key key representing the pair
     * @param value int value for the given key
     */
    public static void setParameter(Context context, String key, int value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Sets a float parameter
     * @param context A context
     * @param key key representing the pair
     * @param value float value for the given key
     */
    public static void setParameter(Context context, String key, float value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * Sets a long parameter
     * @param context A context
     * @param key key representing the pair
     * @param value long value for the given key
     */
    public static void setParameter(Context context, String key, long value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Sets a Calendar parameter
     * @param context A context
     * @param key key representing the pair
     * @param value Calendar value for the given key
     */
    public static void setParameter(Context context, String key, Calendar value) {
        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String date = df.format(value.getTime());
        editor.putString(key, date);
        editor.commit();
    }

    /**
     * Returns the String value of the given key
     * @param context A context
     * @param key key representing the pair
     * @return String value for the given key
     */
    public static String getParameter(Context context, String key, String defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getString(key, defValue);
    }

    /**
     * Returns the float value of the given key
     * @param context A context
     * @param key key representing the pair
     * @return float value for the given key
     */

    public static float getParameter(Context context, String key, float defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getFloat(key, defValue);
    }

    /**
     * Returns the long value of the given key
     * @param context A context
     * @param key key representing the pair
     * @return long value for the given key
     */
    public static long getParameter(Context context, String key, long defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getLong(key, defValue);
    }

    /**
     * Returns the integer value of the given key
     * @param context A context
     * @param key key representing the pair
     * @return int value for the given key
     */
    public static int getParameter(Context context, String key, int defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getInt(key, defValue);
    }


    /**
     * Returns the boolean value of the given key
     * @param context A context
     * @param key key representing the pair
     * @return boolean value for the given key
     */
    public static boolean getParameter(Context context, String key, boolean defValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getBoolean(key, defValue);
    }

    /**
     * Returns the Calendar value of the given key
     * @param context A context
     * @param key key representing the pair
     * @return Calendar value for the given key
     */
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
