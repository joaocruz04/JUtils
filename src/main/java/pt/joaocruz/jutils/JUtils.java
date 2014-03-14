package pt.joaocruz.jutils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by BEWARE S.A. on 01/11/13.
 */
public class JUtils {

    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();


    public static void runAfter(Runnable task, int millis) {
        worker.schedule(task, millis, TimeUnit.MILLISECONDS);
    }



}
