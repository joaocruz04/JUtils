package pt.joaocruz.jutils;

import android.content.Context;

/**
 * Created by BEWARE S.A. on 10/03/14.
 */
public class JSize {

    private static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPx(Context context, int dp) {
        return (int)(dp*getDensity(context));
    }

}
