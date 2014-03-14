package pt.joaocruz.jutils;

import android.app.Activity;
import android.view.View;
import android.view.Window;

/**
 * Created by BEWARE S.A. on 07/03/14.
 */
public class JWindow {

    public static int getScreenHeight(Activity a) {
        View content = a.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        return content.getHeight();
        /*
        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        int height;
        if (Build.VERSION.SDK_INT < 13) {
            height = display.getHeight();
        } else {
            display.getSize(size);
            height = size.y;
        }
        return height;
        */
    }

    public static int getScreenWidth(Activity a) {
        View content = a.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        return content.getHeight();
        /*
        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        int height;
        if (Build.VERSION.SDK_INT < 13) {
            height = display.getWidth();
        } else {
            display.getSize(size);
            height = size.x;
        }
        return height;
        */
    }
}
