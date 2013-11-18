package pt.joaocruz.jutils;

/**
 * Created by BEWARE S.A. on 15/11/13.
 */
public class JString {

    public static String getLastNameFromURL(String url) {
        String[] split = url.split("/");
        if (split.length > 0) {
            return  split[split.length-1];
        }
        return null;
    }

}
