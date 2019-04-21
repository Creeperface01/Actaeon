package me.onebone.actaeon.runnable;

import cn.nukkit.InterruptibleThread;
import cn.nukkit.Server;
import cn.nukkit.utils.MainLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author CreeperFace
 */
public class TaskWatchDog extends Thread implements InterruptibleThread {

    public static List<RouteFinderSearchAsyncTask> tasks = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void run() {
        Server server = Server.getInstance();

        while (server.isRunning()) {
            while (!tasks.isEmpty()) {
                try {
                    RouteFinderSearchAsyncTask task = tasks.get(0);
                    long time = System.currentTimeMillis();

                    if (time - task.started > 1000 && task.thread != null) {
                        task.thread.interrupt();
                        tasks.remove(0);
                    } else if (task.isFinished()) {
                        tasks.remove(0);
                    }
                } catch (Throwable t) {
                    MainLogger.getLogger().error("", t);
                }
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }
}
