package net.sarangnamu.nvapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import net.sarangnamu.nvapp.model.DataManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 */
public class MainApp extends Application {
    private static final Logger mLog = LoggerFactory.getLogger(MainApp.class);

    public static Context context;

    public static int screenX;
    public static int screenY;

    @Override
    public void onCreate() {
        crashWatchDog();

        super.onCreate();
        context = this;

        DataManager.get();
        screenSize();
    }

    private void screenSize() {
        Point pt = new Point();
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getSize(pt);

        screenX = pt.x;
        screenY = pt.y;
    }

    private void crashWatchDog() {
        final Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream s = new PrintStream(os);
            e.printStackTrace(s);
            s.flush();

            mLog.error("ERROR: !!!! CRASH !!!!\n" + os.toString());

            if (handler != null) {
                handler.uncaughtException(t, e);
            } else {
                mLog.error("ERROR: EXCEPTION HANDLER == null");

                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }
}
