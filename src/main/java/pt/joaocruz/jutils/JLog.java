package pt.joaocruz.jutils;

import android.util.Log;

/**
 * Created by BEWARE S.A. on 26/02/14.
 */
public class JLog {

    /**
     * If true, all the prints will be shown
     */
    public static final boolean debug = true;

    /**
     * For IDEs like Android Studio, where the TAG and MESSAGE are not aligned, this makes all tags the same length
     */
    public static final boolean prettyPrinting = true;


    /**
     * Prints a message in the console
     * @param tag tag of the message
     * @param message the message
     */
    public static void print(String tag, String message) {
        if (debug)
            Log.v(tag, message);
    }

    /**
     * Prints a message in the console with the default tag "App"
     * @param message the message
     */
    public static void print(String message) {
        if (prettyPrinting)
            print("App_______", message);
        else
            print("App", message);
    }
}
