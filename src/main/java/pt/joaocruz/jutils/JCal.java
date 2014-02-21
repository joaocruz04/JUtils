package pt.joaocruz.jutils;

import java.util.Calendar;

/**
 * Created by BEWARE S.A. on 01/11/13.
 */
public class JCal {

    public static int differenceInDays(Calendar c1, Calendar c2) {
        long milis1 = c1.getTimeInMillis();
        long milis2 = c2.getTimeInMillis();
        return (int) Math.floor(Math.abs(milis1-milis2) / (1000 * 60 * 60 * 24));
    }

}
